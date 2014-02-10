package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class AgeComponent implements IComponent<Integer> {

    Integer value;

    public AgeComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.AgeComponent";
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
        return "age";
    }
}
