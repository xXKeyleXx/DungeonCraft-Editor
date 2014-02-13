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

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
