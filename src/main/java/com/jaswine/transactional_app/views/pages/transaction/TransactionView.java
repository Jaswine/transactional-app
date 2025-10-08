package com.jaswine.transactional_app.views.pages.transaction;

import com.jaswine.transactional_app.db.entity.Transaction;
import com.jaswine.transactional_app.services.TransactionService;
import com.jaswine.transactional_app.views.MainLayout;
import com.jaswine.transactional_app.views.base.AbstractGridView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

import static com.jaswine.transactional_app.views.utils.FieldCreateUtils.NumberFieldConfiguration;

@Route(value = "transactions", layout = MainLayout.class)
@PageTitle("Transactions")
@PermitAll

public class TransactionView extends AbstractGridView<Transaction> {

    private final transient TransactionService transactionService;

    private final NumberField amountMinFilter = new NumberField();
    private final NumberField amountMaxFilter = new NumberField();

    public TransactionView(TransactionService transactionService) {
        super(Transaction.class);
        this.transactionService = transactionService;

        configureGrid();
        initLayout();

        refreshData();
    }

    /**
     * Создаются дополнительные поля фильтрации
     */
    @Override
    protected HorizontalLayout createFilteringLayout() {
        // Min amount
        NumberField minComponent = NumberFieldConfiguration(amountMinFilter,
                "Min. amount", "Min. amount", "120px",
                VaadinIcon.EURO.create());
        minComponent.addValueChangeListener(value -> {refreshData();});
        // Max amount
        NumberField maxComponent = NumberFieldConfiguration(amountMaxFilter,
                "Max. amount", "Max. amount", "120px",
                VaadinIcon.EURO.create());
        maxComponent.addValueChangeListener(value -> {refreshData();});

        HorizontalLayout filteringLayoutLeft = createHorizontalLayout(amountMinFilter, amountMaxFilter);
        filteringLayoutLeft.setJustifyContentMode(JustifyContentMode.START);
        return filteringLayoutLeft;
    }

    /**
     * Конфигурация вида таблицы
     */
    @Override
    protected void configureGrid() {
        // Колонка откуда деньги
        grid.addColumn(createTransactionAccountRenderer("From"))
                .setHeader("From")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(transaction -> transaction.getAccount().getUser().getUsername());

        // Колонка куда деньги
        grid.addColumn(createTransactionAccountRenderer("To"))
                .setHeader("To")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(transaction -> transaction.getAnotherAccount() != null
                                && transaction.getAnotherAccount().getUser() != null
                                ? transaction.getAnotherAccount().getUser().getUsername() : "");

        grid.addColumn(Transaction::getAmount).setHeader("Amount(€)");
        grid.addColumn(Transaction::getSignature).setHeader("Signature");
        grid.addColumn(Transaction::getStatus).setHeader("Status");
        grid.addColumn(Transaction::getType).setHeader("Type");
        grid.addColumn(Transaction::getExternalSource).setHeader("External Source");

        grid.getDataProvider().refreshAll();
        grid.setDetailsVisibleOnClick(true);

        // Надпись если проектов нет
        grid.setEmptyStateComponent(notFoundHint());
    }

    private static Renderer<Transaction> createTransactionAccountRenderer(String accountType) {
        return LitRenderer.<Transaction> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", transaction ->
                        Objects.equals(accountType, "From") ? transaction.getAccount().getUser().getUsername()
                                :  transaction.getAnotherAccount() != null ?
                                transaction.getAnotherAccount().getUser().getUsername() : "")
                .withProperty("email", transaction ->
                        Objects.equals(accountType, "From") ? transaction.getAccount().getUser().getEmail()
                                :  transaction.getAnotherAccount() != null ?
                                transaction.getAnotherAccount().getUser().getEmail() : "");
    }

    private void setItems(ListDataProvider<Transaction> transactions) {
        grid.setItems(transactions);
    }

    /**
     * Вывод данных
     */
    @Override
    protected void refreshData() {
        // Get and convert values
        String searchingText = searchField.getValue();
        Double minVal = amountMinFilter.getValue();
        Float amountMinFloatValue = (minVal != null) ? minVal.floatValue() : null;
        Double maxVal = amountMaxFilter.getValue();
        Float amountMaxFloatValue = (maxVal != null) ? maxVal.floatValue() : null;

        // Get accounts
        Page<Transaction> accountPage = transactionService.findActiveTransactionsByUserFilter(currentPage, BASE_PAGE_SIZE,
                searchingText, amountMinFloatValue, amountMaxFloatValue);
        List<Transaction> accounts = accountPage.getContent();
        // Define next status
        nextStatus = accounts.stream().count() >= BASE_PAGE_SIZE;
        // Set items
        setItems(DataProvider.ofCollection(accounts.stream().toList()));
    }
}
