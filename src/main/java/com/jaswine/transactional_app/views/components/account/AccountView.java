package com.jaswine.transactional_app.views.components.account;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.services.AccountService;
import com.jaswine.transactional_app.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

@Route(value = "accounts", layout = MainLayout.class)
@PageTitle("Account")
@PermitAll

public class AccountView extends VerticalLayout {
    private final transient AccountService accountService;
    private final Grid<Account> accountGrid;

    private final TextField searchField = new TextField();
    private final NumberField amountMinFilter = new NumberField();
    private final NumberField amountMaxFilter = new NumberField();

    private Integer currentPage = 0;
    private Boolean nextStatus = true;
    private static final Integer PAGE_SIZE = 10;

    public AccountView(AccountService accountService) {
        this.accountService = accountService;

        accountGrid = new Grid<>(Account.class, false);
        HorizontalLayout filterAccountGrid = createFilteringLayout();

        configureGrid();

        setSizeFull();
        setFlexGrow(1, accountGrid);

        add(filterAccountGrid, accountGrid);
        refreshData();
    }


    private HorizontalLayout createFilteringLayout() {
        // Search Field
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(value -> {
            refreshData();
        });

        amountMinFilter.setPlaceholder("Min. amount");
        amountMinFilter.setTooltipText("Min. amount");
        amountMinFilter.setClearButtonVisible(true);
        amountMinFilter.setPrefixComponent(new Span("€"));
        amountMinFilter.setSuffixComponent(VaadinIcon.EURO.create());

        amountMaxFilter.setPlaceholder("Max. amount");
        amountMaxFilter.setTooltipText("Max. amount");
        amountMaxFilter.setClearButtonVisible(true);
        amountMaxFilter.setPrefixComponent(new Span("€"));
        amountMaxFilter.setSuffixComponent(VaadinIcon.EURO.create());

        HorizontalLayout filteringLayoutLeft = createHorizontalLayout(searchField);
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

    private HorizontalLayout createHorizontalLayout(Component... components) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(components);

        horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setWidthFull();
        return horizontalLayout;
    }

    private void configureGrid() {
        accountGrid.addColumn(createEmployeeRenderer()).setHeader("User")
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(account -> account.getUser().getUsername());
        accountGrid.addColumn(Account::getAddress).setHeader("Address");
        accountGrid.addColumn(Account::getAmount).setHeader("Amount");
        accountGrid.addColumn(Account::getIsActive).setHeader("Status");

        accountGrid.getDataProvider().refreshAll();
        accountGrid.setDetailsVisibleOnClick(true);

        // Надпись если проектов нет
        accountGrid.setEmptyStateComponent(notFoundHint());
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

    private void setItems(ListDataProvider<Account> accounts) {
        accountGrid.setItems(accounts);
    }

    protected void refreshData() {
        // Get and convert values
        String searchingText = searchField.getValue();
        Double minVal = amountMinFilter.getValue();
        Float amountMinFloatValue = (minVal != null) ? minVal.floatValue() : null;
        Double maxVal = amountMaxFilter.getValue();
        Float amountMaxFloatValue = (maxVal != null) ? maxVal.floatValue() : null;

        // Get accounts
        Page<Account> accountPage = accountService.findAllAccounts(currentPage, PAGE_SIZE,
                searchingText, amountMinFloatValue, amountMaxFloatValue);
        List<Account> accounts = accountPage.getContent();
        // Define next status
        nextStatus = accounts.stream().count() >= PAGE_SIZE;
        // Set items
        setItems(DataProvider.ofCollection(accounts.stream().toList()));
    }

}
