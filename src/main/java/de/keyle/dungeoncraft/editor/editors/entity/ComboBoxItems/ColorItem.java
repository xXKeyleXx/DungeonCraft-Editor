package de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems;

import java.util.Vector;

public class ColorItem implements IComponentItem<Byte> {

    byte color_id;
    String color;
    Vector colors;

    public ColorItem(byte color_id, String color) {
        this.color = color;
        this.color_id = color_id;
    }

    @SuppressWarnings("unchecked")
    public ColorItem() {
        colors = new Vector();
        colors.add(new ColorItem((byte) 0, "Black"));
        colors.add(new ColorItem((byte) 1, "Dark Blue"));
        colors.add(new ColorItem((byte) 2, "Dark Green"));
        colors.add(new ColorItem((byte) 3, "Dark Aqua"));
        colors.add(new ColorItem((byte) 4, "Dark Red"));
        colors.add(new ColorItem((byte) 5, "Dark Purple"));
        colors.add(new ColorItem((byte) 6, "Gold"));
        colors.add(new ColorItem((byte) 7, "Grey"));
        colors.add(new ColorItem((byte) 8, "Dark Grey"));
        colors.add(new ColorItem((byte) 9, "Blue"));
        colors.add(new ColorItem((byte) 10, "Green"));
        colors.add(new ColorItem((byte) 11, "Aqua"));
        colors.add(new ColorItem((byte) 12, "Red"));
        colors.add(new ColorItem((byte) 13, "Light Purple"));
        colors.add(new ColorItem((byte) 14, "Yellow"));
        colors.add(new ColorItem((byte) 15, "White"));
    }

    public Vector getColors() {
        return colors;
    }

    public String toString() {
        return color;
    }

    @Override
    public Byte getValue() {
        return color_id;
    }
}
