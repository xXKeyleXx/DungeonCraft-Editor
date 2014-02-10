package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class HorseTypeComponent implements IComponent<Byte> {

    Byte value;

    public HorseTypeComponent(Byte value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.HorseTypeComponent";
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
        return "horseType";
    }
}
