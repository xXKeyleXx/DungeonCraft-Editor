package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class MeeleDamageComponent implements IComponent<Double> {

    Double value;

    public MeeleDamageComponent(Double value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.MeeleDamageComponent";
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return "damage";
    }
}
