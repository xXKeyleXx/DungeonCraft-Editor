package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class SittingComponent implements IComponent<Boolean> {

    Boolean value;

    public SittingComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.SitComponent";
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
