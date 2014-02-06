package de.keyle.dungeoncraft.editor.editors.entity.Components;

import java.util.Map;

public class RangeDamageComponent implements IComponent<Map<String,Double>> {

    Map<String,Double> value;

    public RangeDamageComponent(Map<String,Double> value) {
        this.value = value;
    }

    @Override
    public String getName() {
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
}
