package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class PoweredComponent implements IComponent<Boolean> {

    Boolean value;

    public PoweredComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.PoweredComponent";
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
