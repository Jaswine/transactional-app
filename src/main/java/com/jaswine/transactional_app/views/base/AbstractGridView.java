package com.jaswine.transactional_app.views.base;

import com.jaswine.transactional_app.db.entity.Account;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

public abstract class AbstractGridView<T> extends VerticalLayout {
    protected Grid<T> grid;

    protected final TextField searchField = new TextField();

    protected Integer currentPage = 0;
    protected Boolean nextStatus = true;
    protected Integer BASE_PAGE_SIZE = 10;

    protected AbstractGridView(Class<T> clazz) {
        this.grid = new Grid<>(clazz, false);

        setSizeFull();
        setFlexGrow(1, grid);
    }

    /**
     * Инициализация фильтрова
     */
    protected void initLayout() {
        add(createFilterSortLayout(), grid);
    }

    /**
     * Метод для обновления данных - обязан реализовать наследник
     */
    protected abstract void refreshData();

    /**
     * Метод для конфигурации колонок - обязан реализовать наследник
     */
    protected abstract void configureGrid();

    /**
     * Метод для layout фильтров
     */
    protected abstract HorizontalLayout createFilteringLayout();

    /**
     * Метод для layout панели с поисковиком, фильтрами и пагинацией
     */
    protected HorizontalLayout createFilterSortLayout() {
        // Search Field
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(value -> {refreshData();});

        HorizontalLayout layout = createHorizontalLayout(searchField, createFilteringLayout());
        layout.setJustifyContentMode(JustifyContentMode.START);

        return createHorizontalLayout(layout, createPaginationButtons());
    }

    /**
     *  Кнопки пагинации
     */
    protected HorizontalLayout createPaginationButtons() {
        Button leftButton = new Button(VaadinIcon.CHEVRON_LEFT.create(), e -> {
            if (currentPage != 0) {
                currentPage--;
                refreshData();
            }
        });
        leftButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button rightButton = new Button(VaadinIcon.CHEVRON_RIGHT.create(), e -> {
            if (nextStatus) {
                currentPage++;
                refreshData();
            }
        });
        rightButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout layout = createHorizontalLayout(leftButton, rightButton);
        layout.setJustifyContentMode(JustifyContentMode.END);
        layout.setAlignItems(Alignment.END);
        layout.setWidthFull();

        return layout;
    }

    protected HorizontalLayout createHorizontalLayout(Component... components) {
        HorizontalLayout layout = new HorizontalLayout(components);
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layout.setAlignItems(Alignment.CENTER);
        layout.setWidthFull();
        return layout;
    }

    protected VerticalLayout notFoundHint() {
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
}
