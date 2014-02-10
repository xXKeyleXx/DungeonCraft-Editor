package de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems;

import java.util.Vector;

public class ProfessionItem implements IComponentItem<Integer> {

    int profession;
    String description;
    Vector professions;

    public ProfessionItem(int profession, String description) {
        this.profession = profession;
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public ProfessionItem() {
        professions = new Vector();
        professions.add(new ProfessionItem(0, "Farmer"));
        professions.add(new ProfessionItem(1, "Librarian"));
        professions.add(new ProfessionItem(2, "Priest"));
        professions.add(new ProfessionItem(3, "Blacksmith"));
        professions.add(new ProfessionItem(4, "Butcher"));
        professions.add(new ProfessionItem(5, "Generic Villager"));
    }

    public Vector getProfessions() {
        return professions;
    }

    public String toString() {
        return description;
    }

    @Override
    public Integer getValue() {
        return profession;
    }
}
