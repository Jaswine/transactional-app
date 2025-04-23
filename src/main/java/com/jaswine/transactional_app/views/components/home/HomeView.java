package com.jaswine.transactional_app.views.components.home;

import com.jaswine.transactional_app.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "/", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll

public class HomeView extends VerticalLayout {

    public HomeView() {
        addClassName("main-view");
        setSizeFull();
    }
}
