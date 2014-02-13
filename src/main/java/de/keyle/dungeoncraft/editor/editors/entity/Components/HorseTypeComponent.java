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

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof IComponent)) {
            return false;
        }
        return ((IComponent) o).getClassName().equals(this.getClassName()) && ((IComponent) o).getValue().equals(this.getValue());
    }
}
