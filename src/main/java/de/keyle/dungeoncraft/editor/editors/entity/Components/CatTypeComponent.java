package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class CatTypeComponent implements IComponent<Integer>{

    Integer value;

    public CatTypeComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.CatComponent";
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
