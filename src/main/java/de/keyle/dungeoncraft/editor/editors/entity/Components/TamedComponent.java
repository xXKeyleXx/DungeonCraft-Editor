package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class TamedComponent implements IComponent<Boolean> {

    Boolean value;

    public TamedComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.TamedComponent";
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
