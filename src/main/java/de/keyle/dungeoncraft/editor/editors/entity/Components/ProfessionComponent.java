package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class ProfessionComponent implements IComponent<Integer> {

    Integer value;

    public ProfessionComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.ProfessionComponent";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return "profession";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
