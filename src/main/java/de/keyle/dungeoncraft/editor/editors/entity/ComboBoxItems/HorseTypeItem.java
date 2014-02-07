package de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems;

import java.util.Vector;

public class HorseTypeItem {

    private byte type;
    private String description;
    private Vector types;

    public HorseTypeItem(byte type, String description) {
        this.type = type;
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public HorseTypeItem() {
        types = new Vector();
        types.add(new HorseTypeItem((byte)0,"Horse"));
        types.add(new HorseTypeItem((byte)1,"Donkey"));
        types.add(new HorseTypeItem((byte)2,"Mule"));
        types.add(new HorseTypeItem((byte)3,"Undead horse"));
        types.add(new HorseTypeItem((byte)4,"Skeleton horse"));
    }

    public byte getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return description;
    }

    public Vector getTypes() {
        return types;
    }
}
