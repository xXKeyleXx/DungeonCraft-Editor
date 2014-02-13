package de.keyle.dungeoncraft.editor.editors.entity.Components;

import java.util.Map;

public class RangeDamageComponent implements IComponent<Map<String, Double>> {

    Map<String, Double> value;

    public RangeDamageComponent(Map<String, Double> value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.RangedDamageComponent";
    }

    @Override
    public Map<String, Double> getValue() {
        return value;
    }

    @Override
    public void setValue(Map<String, Double> value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return "damage";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
