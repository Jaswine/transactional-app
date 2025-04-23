package com.jaswine.transactional_app.views.components.transaction;

import com.jaswine.transactional_app.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "transactions", layout = MainLayout.class)
@PageTitle("Transactions")
@PermitAll

public class TransactionView extends VerticalLayout {

    public TransactionView() {

    }
}
