package com.jaswine.transactional_app.views.components.account;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.services.AccountService;
import com.jaswine.transactional_app.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "accounts", layout = MainLayout.class)
@PageTitle("Account")
@PermitAll

public class AccountView extends VerticalLayout {
    private final transient AccountService accountService;
    private final Grid<Account> accountGrid;

    private final TextField searchField = new TextField();

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

        HorizontalLayout filteringLayoutLeft = createHorizontalLayout(searchField);
        filteringLayoutLeft.setJustifyContentMode(JustifyContentMode.START);

        // Left || Right buttons
        Button leftButton = new Button();
        leftButton.setIcon(VaadinIcon.CHEVRON_LEFT.create());
        leftButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button rightButton = new Button();
        rightButton.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        rightButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout filteringLayoutRight = createHorizontalLayout(leftButton, rightButton);
        filteringLayoutRight.setJustifyContentMode(JustifyContentMode.END);
        filteringLayoutRight.setAlignItems(Alignment.END);

        return createHorizontalLayout(filteringLayoutLeft, filteringLayoutRight);
    }

    private void configureGrid() {
        accountGrid.addColumn(account -> account.getUser().getUsername()).setHeader("User");
        accountGrid.addColumn(Account::getAmount).setHeader("Amount");
        accountGrid.addColumn(Account::getAddress).setHeader("Address");
        accountGrid.addColumn(Account::getIsActive).setHeader("Status");

        accountGrid.getDataProvider().refreshAll();
        accountGrid.setDetailsVisibleOnClick(true);

        // Надпись если проектов нет
        accountGrid.setEmptyStateComponent(notFoundHint());
    }

    private Div notFoundHint() {
        Div notFoundHint = new Div();
        notFoundHint.setWidth("100%");
        notFoundHint.getStyle().set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center");

        Icon vaadinIcon = VaadinIcon.COMPILE.create();

        H2 header = new H2("This place is empty");
        header.addClassNames(LumoUtility.Margin.Top.XLARGE, LumoUtility.Margin.Bottom.MEDIUM);
        Paragraph text = new Paragraph("It’s a place where you can grow your own UI 🤗");

        notFoundHint.add(vaadinIcon, header, text);

        return notFoundHint;
    }

    private void setItems(ListDataProvider<Account> accounts) {
        accountGrid.setItems(accounts);
    }

    protected void refreshData() {
        List<Account> accounts = accountService.findAllAccounts("", "");
        System.out.println("ACCOUNTS: " + accounts);
        setItems(DataProvider.ofCollection(accounts.stream().toList()));
    }

    private HorizontalLayout createHorizontalLayout(Component... components) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(components);

        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setWidthFull();
        return horizontalLayout;
    }

}
