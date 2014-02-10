package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class WitherComponent implements IComponent<Boolean> {

    Boolean value;

    public WitherComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.WitherComponent";
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
        return "wither";
    }
}
