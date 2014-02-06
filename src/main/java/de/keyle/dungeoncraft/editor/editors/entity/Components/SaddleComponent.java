package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class SaddleComponent implements IComponent<Boolean> {

    Boolean value;

    public SaddleComponent(Boolean value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.SaddleComponent";
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
        return "saddle";
    }
}
