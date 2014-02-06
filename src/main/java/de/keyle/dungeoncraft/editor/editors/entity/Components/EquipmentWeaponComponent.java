package de.keyle.dungeoncraft.editor.editors.entity.Components;

import org.json.simple.JSONObject;

public class EquipmentWeaponComponent implements IComponent<JSONObject> {

    JSONObject value;

    public EquipmentWeaponComponent(JSONObject value) {
        this.value = value;
    }

    @Override
    public String getClassName() {
        return "de.keyle.dungeoncraft.entity.template.components.EquipmentWeaponComponent";
    }

    @Override
    public JSONObject getValue() {
        return value;
    }

    @Override
    public void setValue(JSONObject value) {
        this.value = value;
    }

    @Override
    public String getParameterName() {
        return null;
    }
}
