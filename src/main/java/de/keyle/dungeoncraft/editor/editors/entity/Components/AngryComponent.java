package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class AngryComponent implements IComponent<Boolean>{

    Boolean value;

    public AngryComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.AngryComponent";
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
