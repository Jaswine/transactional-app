package com.jaswine.transactional_app.views.pages.account;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.services.AccountService;
import com.jaswine.transactional_app.views.MainLayout;
import com.jaswine.transactional_app.views.base.AbstractGridView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.jaswine.transactional_app.views.utils.FieldCreateUtils.NumberFieldConfiguration;

@Route(value = "accounts", layout = MainLayout.class)
@PageTitle("Account")
@PermitAll

public class AccountView extends AbstractGridView<Account> {
    private final transient AccountService accountService;

    private final NumberField amountMinFilter = new NumberField();
    private final NumberField amountMaxFilter = new NumberField();

    public AccountView(AccountService accountService) {
        super(Account.class);
        this.accountService = accountService;

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
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(createEmployeeRenderer()).setHeader("User")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(account -> account.getUser().getUsername()).setResizable(true);
        grid.addColumn(Account::getAddress).setHeader("Address").setResizable(true);
        grid.addColumn(Account::getAmount).setHeader("Amount(€)").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(account -> {
            Checkbox checkbox = new Checkbox(account.getIsActive());
            checkbox.setEnabled(true);
            return checkbox;
        })).setHeader("Is Active").setResizable(true);
        grid.addColumn(createEditButton()).setFrozenToEnd(true)
                .setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(createDeleteButton()).setFrozenToEnd(true)
                .setAutoWidth(true).setFlexGrow(0);

        grid.getDataProvider().refreshAll();
        grid.setDetailsVisibleOnClick(true);

        // Надпись если проектов нет
        grid.setEmptyStateComponent(notFoundHint());
    }

    private static Renderer<Account> createEditButton() {
        return LitRenderer.<Account> of(
                "<vaadin-button theme=\"tertiary\">Edit</vaadin-button>");
    }

    private static Renderer<Account> createDeleteButton() {
        return LitRenderer.<Account> of(
                "<vaadin-button theme=\"error\">Delete</vaadin-button>");
    }

    private static Renderer<Account> createEmployeeRenderer() {
        return LitRenderer.<Account> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", account -> account.getUser().getUsername())
                .withProperty("email", account -> account.getUser().getEmail());
    }


    private void setItems(ListDataProvider<Account> accounts) {
        grid.setItems(accounts);
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
        Page<Account> accountPage = accountService.findAllAccounts(currentPage, BASE_PAGE_SIZE,
                searchingText, amountMinFloatValue, amountMaxFloatValue);
        List<Account> accounts = accountPage.getContent();
        // Define next status
        nextStatus = accounts.stream().count() >= BASE_PAGE_SIZE;
        // Set items
        setItems(DataProvider.ofCollection(accounts.stream().toList()));
    }

}
