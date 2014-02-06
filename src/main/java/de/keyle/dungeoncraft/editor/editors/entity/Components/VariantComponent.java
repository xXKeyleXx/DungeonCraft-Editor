package de.keyle.dungeoncraft.editor.editors.entity.Components;

public class VariantComponent implements IComponent<Integer>{

    Integer value;

    public VariantComponent(Integer value) {
         this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.VariantComponent";
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
          this.value = value;
    }
}
