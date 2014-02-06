package de.keyle.dungeoncraft.editor.editors.entity.Components;

import org.json.simple.JSONObject;

import java.util.Map;

public class EquipmentArmorComponent implements IComponent<Map<String,JSONObject>> {

    Map<String,JSONObject> value;

    public EquipmentArmorComponent(Map<String,JSONObject> value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.EquipmentArmorComponent";
    }

    @Override
    public Map<String,JSONObject> getValue() {
        return value;
    }

    @Override
    public void setValue(Map<String,JSONObject> value) {
         this.value = value;
    }

    @Override
    public String getParameterName() {
        return null;
    }
}
