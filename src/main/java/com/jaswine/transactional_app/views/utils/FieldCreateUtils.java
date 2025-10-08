package com.jaswine.transactional_app.views.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;

public class FieldCreateUtils {

    /**
     * Конфигурация числового поля
     */
    public static NumberField NumberFieldConfiguration(NumberField field,
                                             String placeholder, String tooltip, String width,
                                             Component suffix) {
        field.setPlaceholder(placeholder);
        field.setTooltipText(tooltip);
        field.setWidth(width);
        field.setClearButtonVisible(true);
        field.setSuffixComponent(suffix);
        return field;
    }

}
