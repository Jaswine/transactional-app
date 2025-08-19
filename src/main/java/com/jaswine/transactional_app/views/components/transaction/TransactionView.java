package com.jaswine.transactional_app.views.components.transaction;

import com.jaswine.transactional_app.db.entity.Transaction;
import com.jaswine.transactional_app.services.TransactionService;
import com.jaswine.transactional_app.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasPlaceholder;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.HasClearButton;
import com.vaadin.flow.component.shared.HasSuffix;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

@Route(value = "transactions", layout = MainLayout.class)
@PageTitle("Transactions")
@PermitAll

public class TransactionView extends VerticalLayout {

    private final transient TransactionService transactionService;

    private final Grid<Transaction> transactionGrid;

    private final TextField searchField = new TextField();
    private final NumberField amountMinFilter = new NumberField();
    private final NumberField amountMaxFilter = new NumberField();

    private Integer currentPage = 0;
    private Boolean nextStatus = true;
    private static final Integer PAGE_SIZE = 10;


    public TransactionView(TransactionService transactionService) {
        this.transactionService = transactionService;

        transactionGrid = new Grid<>(Transaction.class, false);
        HorizontalLayout filterTransactionGrid = createFilteringLayout();

        configureGrid();

        setSizeFull();
        setFlexGrow(1, transactionGrid);

        add(filterTransactionGrid, transactionGrid);
        refreshData();

    }

    private HorizontalLayout createFilteringLayout() {
        // Search field
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        configureField(searchField, "Search", null, null, null);

        // Min amount
        configureField(amountMinFilter, "Min. amount", "Min. amount", "120px", VaadinIcon.EURO.create());
        // Max amount
        configureField(amountMaxFilter, "Max. amount", "Max. amount", "120px", VaadinIcon.EURO.create());


        HorizontalLayout filteringLayoutLeft = createHorizontalLayout(searchField,
                amountMinFilter, amountMaxFilter);
        filteringLayoutLeft.setJustifyContentMode(JustifyContentMode.START);

        // Left || Right buttons
        Button leftButton = new Button();
        leftButton.setIcon(VaadinIcon.CHEVRON_LEFT.create());
        leftButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        leftButton.addClickListener(e -> {
            if (currentPage != 0) {
                currentPage -= 1;
            }
            refreshData();
        });

        Button rightButton = new Button();
        rightButton.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        rightButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        rightButton.addClickListener(e -> {
            if (nextStatus) {
                currentPage += 1;
            }
            refreshData();
        });

        HorizontalLayout filteringLayoutRight = createHorizontalLayout(leftButton, rightButton);
        filteringLayoutRight.setJustifyContentMode(JustifyContentMode.END);
        filteringLayoutRight.setAlignItems(Alignment.END);

        return createHorizontalLayout(filteringLayoutLeft, filteringLayoutRight);
    }

    private void configureField(Component field, String placeholder, String tooltip, String width, Component suffix) {
        if (field instanceof HasPlaceholder p) {
            p.setPlaceholder(placeholder);
        }
        if (field instanceof HasTooltip t) {
            t.setTooltipText(tooltip);
        }
        if (width != null && field instanceof HasSize s) {
            s.setWidth(width);
        }
        if (field instanceof HasClearButton c) {
            c.setClearButtonVisible(true);
        }
        if (suffix != null && field instanceof HasSuffix sfx) {
            sfx.setSuffixComponent(suffix);
        }
        if (field instanceof HasValue<?, ?> v) {
            v.addValueChangeListener(e -> refreshData());
        }
    }

    private HorizontalLayout createHorizontalLayout(Component... components) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(components);

        horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setWidthFull();
        return horizontalLayout;
    }

    private void configureGrid() {
        transactionGrid.addColumn(createTransactionAccountRenderer("From"))
                .setHeader("From")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(transaction -> transaction.getAccount().getUser().getUsername());

        transactionGrid.addColumn(createTransactionAccountRenderer("To"))
                .setHeader("To")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(transaction -> transaction.getAnotherAccount() != null
                                && transaction.getAnotherAccount().getUser() != null
                                ? transaction.getAnotherAccount().getUser().getUsername() : "");

        transactionGrid.addColumn(Transaction::getAmount).setHeader("Amount");
        transactionGrid.addColumn(Transaction::getSignature).setHeader("Signature");
        transactionGrid.addColumn(Transaction::getStatus).setHeader("Status");
        transactionGrid.addColumn(Transaction::getComment).setHeader("Comment");

        transactionGrid.getDataProvider().refreshAll();
        transactionGrid.setDetailsVisibleOnClick(true);

        // Надпись если проектов нет
        transactionGrid.setEmptyStateComponent(notFoundHint());
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

    private VerticalLayout notFoundHint() {
        VerticalLayout notFoundHint = new VerticalLayout();
        notFoundHint.setWidth("100%");
        notFoundHint.setAlignItems(Alignment.CENTER);
        notFoundHint.setJustifyContentMode(JustifyContentMode.CENTER);

        Icon vaadinIcon = VaadinIcon.COMPILE.create();
        vaadinIcon.getStyle().setFontSize("var(--lumo-size-xl)");

        H2 header = new H2("This place is empty");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        Paragraph text = new Paragraph("Enter other data in the search.");

        notFoundHint.add(vaadinIcon, header, text);

        return notFoundHint;
    }

    private void setItems(ListDataProvider<Transaction> transactions) {
        transactionGrid.setItems(transactions);
    }

    protected void refreshData() {
        // Get and convert values
        String searchingText = searchField.getValue();
        Double minVal = amountMinFilter.getValue();
        Float amountMinFloatValue = (minVal != null) ? minVal.floatValue() : null;
        Double maxVal = amountMaxFilter.getValue();
        Float amountMaxFloatValue = (maxVal != null) ? maxVal.floatValue() : null;

        // Get accounts
        Page<Transaction> accountPage = transactionService.findActiveTransactionsByUserFilter(currentPage, PAGE_SIZE,
                searchingText, amountMinFloatValue, amountMaxFloatValue);
        List<Transaction> accounts = accountPage.getContent();
        // Define next status
        nextStatus = accounts.stream().count() >= PAGE_SIZE;
        // Set items
        setItems(DataProvider.ofCollection(accounts.stream().toList()));
    }
}
