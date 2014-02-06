package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class SizeComponent implements IComponent<Integer> {

    Integer value;

    public SizeComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.SizeComponent";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
}
