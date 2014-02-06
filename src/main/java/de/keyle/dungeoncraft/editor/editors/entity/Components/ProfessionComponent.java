package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class ProfessionComponent implements IComponent<Integer> {

    Integer value;

    public ProfessionComponent(Integer value) {
        this.value = value;
    }

    @Override
    public String getName() {
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
}
