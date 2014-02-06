package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class BabyComponent implements IComponent<Boolean>{

    Boolean value;

    public BabyComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.BabyComponent";
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
        return "baby";
    }
}
