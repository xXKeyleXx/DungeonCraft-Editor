package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class ArmorComponent implements IComponent<Integer> {

    Integer value;

    public ArmorComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.ArmorComponent";
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
        return "armor";
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
