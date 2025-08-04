package com.jaswine.transactional_app.views;

import com.jaswine.transactional_app.services.VaadinSecurityService;
import com.jaswine.transactional_app.views.components.account.AccountView;
import com.jaswine.transactional_app.views.components.home.HomeView;
import com.jaswine.transactional_app.views.components.transaction.TransactionView;
import com.jaswine.transactional_app.views.utils.ThemeModesUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Base Page")
@AnonymousAllowed

public class MainLayout extends AppLayout {
    private final transient VaadinSecurityService securityService;

    public MainLayout(
            VaadinSecurityService securityService
    ) {
        this.securityService = securityService;
//        Optional<User> optionalUser = securityService.getAuthenticatedUser();
//        System.out.println(optionalUser.isEmpty() ? "User not found" : optionalUser.get().getUsername());

        ThemeModesUtils.detectThemeMode();
        createHeader();
    }

    private void createHeader() {
        HorizontalLayout headerButtons = createhorizontalLayout(
                createHeaderButton("Home", HomeView.class),
                createHeaderButton("Accounts", AccountView.class),
                createHeaderButton("Transactions", TransactionView.class),
                createExternalLinkButton("API", "/swagger-ui/")
        );
        headerButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        Button logoutButton = new Button("Logout", e -> securityService.logout());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout headerLayout = createhorizontalLayout(
                headerButtons,
                logoutButton);
        headerLayout.getStyle().setPaddingRight("10px").setPaddingLeft("10px")
                .setBackgroundColor("transparent");

        addToNavbar(headerLayout);

        setPrimarySection(Section.DRAWER);
    }

    private RouterLink createHeaderButton(String viewName, Class<? extends Component> clazz) {
        Span text = new Span(viewName);
        RouterLink link = new RouterLink();
        link.add(text);
        link.getElement().getThemeList().add("contrast");

        link.setRoute(clazz);
        link.setTabIndex(-1);
        link.getStyle().setColor("var(--lumo-header-text-color)");
        return link;
    }

    private Anchor createExternalLinkButton(String viewName, String url) {
        Span text = new Span(viewName);
        Anchor anchor = new Anchor(url, text);
        anchor.setTarget("_blank");
        anchor.getElement().getThemeList().add("contrast");
        anchor.getStyle().set("color", "var(--lumo-header-text-color)");
        return anchor;
    }


    private HorizontalLayout createhorizontalLayout(Component... components) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(components);

        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setWidthFull();
        return horizontalLayout;
    }

}
