package de.keyle.dungeoncraft.editor.editors.entity.Components;

import org.json.simple.JSONObject;

import java.util.List;

public class EquipmentArmorComponent implements IComponent<List<JSONObject>> {

    List<JSONObject> value;

    public EquipmentArmorComponent(List<JSONObject> value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "de.keyle.dungeoncraft.entity.template.components.EquipmentArmorComponent";
    }

    @Override
    public List<JSONObject> getValue() {
        return value;
    }

    @Override
    public void setValue(List<JSONObject> value) {
         this.value = value;
    }
}
