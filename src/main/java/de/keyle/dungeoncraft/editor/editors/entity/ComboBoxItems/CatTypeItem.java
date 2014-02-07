package de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems;

import java.util.Vector;

public class CatTypeItem {

    int type;
    String description;
    Vector types;

    public CatTypeItem(int type, String description) {
        this.type = type;
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public CatTypeItem() {
        types = new Vector();
        types.add(new CatTypeItem(0,"Wild Cat"));
        types.add(new CatTypeItem(1,"Black Cat"));
        types.add(new CatTypeItem(2,"Red Cat"));
        types.add(new CatTypeItem(3,"Siamese Cat"));
    }


    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Vector getTypes() {
        return types;
    }

    public String toString() {
        return description;
    }

}
