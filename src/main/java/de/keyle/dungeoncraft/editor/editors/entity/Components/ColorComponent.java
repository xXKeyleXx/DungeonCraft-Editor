package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class ColorComponent implements IComponent<Byte> {

    Byte value;

    public ColorComponent(Byte value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.ColorComponent";
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public void setValue(Byte value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return "color";
    }
}
