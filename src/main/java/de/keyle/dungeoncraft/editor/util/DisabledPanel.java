package de.keyle.dungeoncraft.editor.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisabledPanel extends JPanel {
    private List<JComponent> enabledComponents = new ArrayList<JComponent>();

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            enablePanel();
        } else {
            disablePanel();
        }
    }

    protected void disablePanel() {
        List<JComponent> components = getDescendantsOfType(JComponent.class, this, true);
        for (JComponent component : components) {
            if (component.isEnabled()) {
                this.enabledComponents.add(component);
                component.setEnabled(false);
            }
        }
    }

    protected void enablePanel() {
        for (JComponent component : enabledComponents) {
            component.setEnabled(true);
        }
        enabledComponents.clear();
    }

    public static <T extends JComponent> List<T> getDescendantsOfType(Class<T> clazz, Container container, boolean nested) {
        List<T> tList = new ArrayList<T>();
        for (Component component : container.getComponents()) {
            if (clazz.isAssignableFrom(component.getClass())) {
                tList.add(clazz.cast(component));
            }
            if (nested || !clazz.isAssignableFrom(component.getClass())) {
                tList.addAll(getDescendantsOfType(clazz, (Container) component, nested));
            }
        }
        return tList;
    }
}