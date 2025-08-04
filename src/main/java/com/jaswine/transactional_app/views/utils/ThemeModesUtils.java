package com.jaswine.transactional_app.views.utils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ThemeModesUtils {

    /**
     * Detect theme mode
     */
    public void detectThemeMode(Component ... components) {
        UI.getCurrent().getPage()
            .executeJs("return window.matchMedia('(prefers-color-scheme: dark)').matches;")
            .then(value -> {
                UI.getCurrent().getElement().getThemeList().add(
                        value != null && (value).asBoolean() ? Lumo.DARK : Lumo.LIGHT
                );
            });
    }

}
