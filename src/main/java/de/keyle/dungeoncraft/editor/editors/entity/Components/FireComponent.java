package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class FireComponent implements IComponent<Boolean> {

    Boolean value;

    public FireComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.FireComponent";
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
        return "fire";
    }
}
