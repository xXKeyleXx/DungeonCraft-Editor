package de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems;

import java.util.Vector;

public class VariantItem implements IComboBoxtItem<Integer> {

    int variant;
    String description;
    Vector variants;

    public VariantItem(int variant, String description) {
        this.variant = variant;
        this.description = description;
    }

    @SuppressWarnings("unchecked")
    public VariantItem() {
        variants = new Vector();
        variants.add(new VariantItem(0, "White"));
        variants.add(new VariantItem(1, "Creamy"));
        variants.add(new VariantItem(2, "Chestnut"));
        variants.add(new VariantItem(3, "Brown"));
        variants.add(new VariantItem(4, "Black"));
        variants.add(new VariantItem(5, "Gray"));
        variants.add(new VariantItem(6, "Dark Brown"));
        variants.add(new VariantItem(256, "White - White"));
        variants.add(new VariantItem(257, "White - Creamy"));
        variants.add(new VariantItem(258, "White - Chestnut"));
        variants.add(new VariantItem(259, "White - Brown"));
        variants.add(new VariantItem(260, "White - Black"));
        variants.add(new VariantItem(261, "White - Gray"));
        variants.add(new VariantItem(262, "White - Dark Brown"));
        variants.add(new VariantItem(512, "White Field - White"));
        variants.add(new VariantItem(513, "White Field - Creamy"));
        variants.add(new VariantItem(514, "White Field - Chestnut"));
        variants.add(new VariantItem(515, "White Field - Brown"));
        variants.add(new VariantItem(516, "White Field - Black"));
        variants.add(new VariantItem(517, "White Field - Gray"));
        variants.add(new VariantItem(518, "White Field - Dark Brown"));
        variants.add(new VariantItem(768, "White Dots - White"));
        variants.add(new VariantItem(769, "White Dots - Creamy"));
        variants.add(new VariantItem(770, "White Dots - Chestnut"));
        variants.add(new VariantItem(771, "White Dots - Brown"));
        variants.add(new VariantItem(772, "White Dots - Black"));
        variants.add(new VariantItem(773, "White Dots - Gray"));
        variants.add(new VariantItem(774, "White Dots - Dark Brown"));
        variants.add(new VariantItem(1024, "Black Dots - White"));
        variants.add(new VariantItem(1025, "Black Dots - Creamy"));
        variants.add(new VariantItem(1026, "Black Dots - Chestnut"));
        variants.add(new VariantItem(1027, "Black Dots - Brown"));
        variants.add(new VariantItem(1028, "Black Dots - Black"));
        variants.add(new VariantItem(1029, "Black Dots - Gray"));
        variants.add(new VariantItem(1030, "Black Dots - Dark Brown"));
    }


    public String getDescription() {
        return description;
    }

    public Vector getVariants() {
        return variants;
    }

    public String toString() {
        return description;
    }

    @Override
    public Integer getValue() {
        return variant;
    }
}
