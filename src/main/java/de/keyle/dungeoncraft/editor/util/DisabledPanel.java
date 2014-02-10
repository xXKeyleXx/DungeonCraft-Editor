package de.keyle.dungeoncraft.editor.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisabledPanel extends JPanel {
    private List<JComponent> enabledComponents = new ArrayList<JComponent>();
    private static Map<Container, List<JComponent>> containers = new HashMap<Container, List<JComponent>>();

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
            if (nested && component instanceof Container) {
                tList.addAll(getDescendantsOfType(clazz, (Container) component, nested));
            }
        }
        return tList;
    }

    public static void disableContainer(Container container) {
        if (!containers.containsKey(container)) {
            List<JComponent> components = getDescendantsOfType(JComponent.class, container, true);
            for (JComponent component : components) {
                if (component.isEnabled()) {
                    component.setEnabled(false);
                } else {
                    component.remove(component);
                }
            }
            containers.put(container, components);
        }
    }

    public static void enableContainer(Container container) {
        if (containers.containsKey(container)) {
            for (JComponent component : containers.get(container)) {
                component.setEnabled(true);
            }
            containers.remove(container);
        }
    }
}