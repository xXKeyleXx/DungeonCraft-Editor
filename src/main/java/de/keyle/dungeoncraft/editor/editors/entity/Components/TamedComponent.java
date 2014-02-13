package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class TamedComponent implements IComponent<Boolean> {

    Boolean value;

    public TamedComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.TamedComponent";
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return "tamed";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
