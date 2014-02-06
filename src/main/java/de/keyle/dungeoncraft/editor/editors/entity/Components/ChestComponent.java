package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class ChestComponent implements IComponent<Boolean>{

    Boolean value;

    public ChestComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.ChestComponent";
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
        return "chest";
    }
}
