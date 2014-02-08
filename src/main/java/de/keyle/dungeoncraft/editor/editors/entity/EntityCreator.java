/*
 * This file is part of DungeonCraft
 *
 * Copyright (C) 2013-2014 Keyle & xXLupoXx
 * DungeonCraft is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.dungeoncraft.editor.editors.entity;


import de.keyle.dungeoncraft.editor.editors.Editor;
import de.keyle.dungeoncraft.editor.editors.entity.ComboBoxItems.*;
import de.keyle.dungeoncraft.editor.editors.entity.Components.*;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntityCreator implements Editor {
    private JTree entityTemplateTree;
    private JButton createButton;
    private JButton editButton;
    private DefaultTreeModel entityTemplateTreeModel;
    private EntityTemplateLoader loader;
    private JPanel entityCreatorPanel;
    private JComboBox mobTypeComboBox;
    private JCheckBox onFireCheckBox;
    private JButton saveButton1;
    private JComboBox combobox_Projectile;
    private JTextField field_Range_Damage;
    private JTextField field_Meele_Damage;
    private JTextField field_Template_ID;
    private JTextField field_Age;
    private JCheckBox isAngryCheckBox;
    private JTextField field_Armor;
    private JCheckBox isBabyCheckBox;
    private JComboBox catType_comboBox;
    private JCheckBox hasChestCheckBox;
    private JComboBox color_comboBox;
    private JComboBox horseType_comboBox;
    private JCheckBox isPoweredCheckBox;
    private JComboBox profession_comboBox;
    private JCheckBox hasSaddleCheckBox;
    private JCheckBox isSittingCheckBox;
    private JCheckBox isTamedCheckBox;
    private JCheckBox isWitherCheckBox;
    private JComboBox variant_comboBox;
    private JTextField field_Max_Health;
    private JTextField field_Walk_Speed;
    private JTextField field_Display_Name;
    private JTextField field_Size;
    private JTextField field_weapon_id;
    private JTextField field_weapon_data;
    private JTextField field_weapon_tag;
    private JTextField field_helmet_id;
    private JTextField field_helmet_data;
    private JTextField field_helmet_tag;
    private JTextField field_chest_id;
    private JTextField field_chest_data;
    private JTextField field_chest_tag;
    private JTextField field_leggins_id;
    private JTextField field_leggins_data;
    private JTextField field_leggins_tag;
    private JTextField field_boots_id;
    private JTextField field_boots_data;
    private JTextField field_boots_tag;
    private JButton deleteButton;

    String selectedMobtype;
    boolean createMode = false;
    EntityTemplate editTemplate;
    List<EntityTemplate> templateList;
    File templateFile;

    public static String[] MOB_TYPES = new String[]{"Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "Enderman", "Ghast", "Giant", "Horse", "IronGolem", "MagmaCube", "Mooshroom", "Ocelot", "Pig", "PigZombie", "Sheep", "Silverfish", "Skeleton", "Slime", "Snowman", "Spider", "Squid", "Witch", "Wither", "Wolf", "Villager", "Zombie"};

    public EntityCreator() {
        mobTypeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedMobtype = mobTypeComboBox.getSelectedItem().toString();
                    deactivateAllTemplatePanel();
                    activateMaxHealth(false, null);
                    activateWalkSpeed(false, null);
                    activeEntityTemplatID(false, null);
                    activateDisplayName(false,null);
                    activateMeeleDamage(false, null);
                    activateRangeDamage(false, null);
                    activateArmor(false, null);
                    activateSaveButton();
                    if (selectedMobtype.equals("Blaze")) {
                        activateFire(false, null);
                    } else if (selectedMobtype.equals("Creeper")) {
                        activatePowered(false, null);
                    } else if (selectedMobtype.equals("Enderman")) {
                        activateAngry(false, null);
                        activateWeapon(false, null);
                    } else if (selectedMobtype.equals("Horse")) {
                        activateAge(false, null);
                        activateBaby(false, null);
                        activateTamed(false, null);
                        activateVariant(false, null);
                        activateChest(false, null);
                        activateSaddle(false, null);
                        activateHorseType(false, null);
                    } else if (selectedMobtype.equals("MagmaCube") || selectedMobtype.equals("Slime")) {
                        activateSize(false, null);
                    } else if (selectedMobtype.equals("Ocelot")) {
                        activateCatType(false, null);
                        activateSit(false, null);
                    } else if (selectedMobtype.equals("Pig") && !selectedMobtype.contains("Zombie")) {
                        activateSaddle(false, null);
                    } else if (selectedMobtype.equals("PigZombie")) {
                        activateArmorEquip(false, null);
                        activateWeapon(false, null);
                    } else if (selectedMobtype.equals("Sheep")) {
                        activateColor(false, null);
                    } else if (selectedMobtype.equals("Skeleton")) {
                        activateArmorEquip(false, null);
                        activateWeapon(false, null);
                        activateWither(false, null);
                    } else if (selectedMobtype.equals("Villager")) {
                        activateProfession(false, null);
                    } else if (selectedMobtype.equals("Wither")) {
                        activateWither(false, null);
                    } else if (selectedMobtype.equals("Wolf")) {
                        activateColor(false, null);
                        activateSit(false, null);
                        activateTamed(false, null);
                        activateAngry(false, null);
                    } else if (selectedMobtype.equals("Zombie") && !selectedMobtype.contains("Pig")) {
                        activateArmorEquip(false, null);
                        activateWeapon(false, null);
                    }
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mobTypeComboBox.setEnabled(false);
                if (entityTemplateTree.getSelectionPath() != null) {
                    if (entityTemplateTree.getSelectionPath().getPath().length == 2) {
                        if (entityTemplateTree.getSelectionPath().getPathComponent(1) instanceof TemplateNode) {
                            EntityTemplate template = ((TemplateNode) entityTemplateTree.getSelectionPath().getPathComponent(1)).getEntityTemplate();
                            editTemplate(template);
                        }
                    }
                }
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateAllTemplatePanel();
                mobTypeComboBox.setEnabled(true);
                createMode = true;
            }
        });
        saveButton1.addActionListener(new ActionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent e) {
                boolean save = true;

                if(createMode) {
                    editTemplate = new EntityTemplate();
                }
                //EntityTemplate toDelete = editTemplate;
                if (createMode) {
                    editTemplate.setEntityType((String) mobTypeComboBox.getSelectedItem());
                }
                if (field_Max_Health.getText().equals("")) {
                    field_Max_Health.setText("You have to set the health value");
                    save = false;
                }
                if (!Util.isDouble(field_Max_Health.getText())) {
                    field_Max_Health.setText("This has to be a number");
                    save = false;
                }
                if (field_Walk_Speed.getText().equals("")) {
                    field_Walk_Speed.setText("You have to set the speed value");
                    save = false;
                }
                if (!Util.isDouble(field_Walk_Speed.getText())) {
                    field_Walk_Speed.setText("This has to be a number");
                    save = false;
                }
                if (!Util.isDouble(field_Meele_Damage.getText()) && !field_Meele_Damage.getText().equals("")) {
                    field_Meele_Damage.setText("This has to be a number");
                    save = false;
                }
                if (!Util.isDouble(field_Range_Damage.getText()) && !field_Range_Damage.getText().equals("")) {
                    field_Range_Damage.setText("This has to be a number");
                    save = false;
                }
                if(!Util.isInt(field_Size.getText()) && !field_Size.getText().equals("")) {
                    field_Size.setText("This has to be a number");
                    save = false;
                }

                if (save) {

                    editTemplate.setMaxHealth(Double.parseDouble(field_Max_Health.getText()));
                    editTemplate.setWalkSpeed(Double.parseDouble(field_Walk_Speed.getText()));
                    editTemplate.setTemplateId(field_Template_ID.getText());
                    editTemplate.setDisplayName(field_Display_Name.getText());

                    //Update or create components
                    for (IComponent iComponent : editTemplate.getComponents()) {
                        if (iComponent instanceof MeeleDamageComponent) {
                            if (field_Meele_Damage.isEnabled() && field_Meele_Damage.getText().equals("")) {
                                editTemplate.getComponents().remove(iComponent);
                            } else if (field_Meele_Damage.isEnabled()) {
                                ((MeeleDamageComponent) iComponent).setValue(Double.parseDouble(field_Meele_Damage.getText()));
                            }
                        } else if (iComponent instanceof RangeDamageComponent) {
                            if (field_Range_Damage.isEnabled() && field_Range_Damage.getText().equals("")) {
                                editTemplate.getComponents().remove(iComponent);
                            } else if (field_Range_Damage.isEnabled()) {
                                Map<String, Double> tmpMap = new HashMap<String, Double>();
                                tmpMap.put(((String) combobox_Projectile.getSelectedItem()).replace(" ", ""), Double.parseDouble(field_Range_Damage.getText()));
                                ((RangeDamageComponent) iComponent).setValue(tmpMap);
                            }
                        } else if (iComponent instanceof ArmorComponent) {
                            if (field_Armor.isEnabled() && field_Armor.getText().equals("")) {
                                editTemplate.getComponents().remove(iComponent);
                            } else if (field_Armor.isEnabled()) {
                                ((ArmorComponent) iComponent).setValue(Integer.parseInt(field_Armor.getText()));
                            }
                        } else if (iComponent instanceof AgeComponent) {
                            if (field_Age.isEnabled() && field_Age.getText().equals("")) {
                                editTemplate.getComponents().remove(iComponent);
                            } else if (field_Age.isEnabled()) {
                                ((AgeComponent) iComponent).setValue(Integer.parseInt(field_Age.getText()));
                            }
                        } else if (iComponent instanceof BabyComponent) {
                            if (isBabyCheckBox.isEnabled()) {
                                ((BabyComponent) iComponent).setValue(isBabyCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof AngryComponent) {
                            if (isAngryCheckBox.isEnabled()) {
                                ((AngryComponent) iComponent).setValue(isAngryCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof FireComponent) {
                            if (onFireCheckBox.isEnabled()) {
                                ((FireComponent) iComponent).setValue(onFireCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof ChestComponent) {
                            if (hasChestCheckBox.isEnabled()) {
                                ((ChestComponent) iComponent).setValue(hasChestCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof PoweredComponent) {
                            if (isPoweredCheckBox.isEnabled()) {
                                ((PoweredComponent) iComponent).setValue(isPoweredCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof SaddleComponent) {
                            if (hasSaddleCheckBox.isEnabled()) {
                                ((SaddleComponent) iComponent).setValue(hasSaddleCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof SittingComponent) {
                            if (isSittingCheckBox.isEnabled()) {
                                ((SittingComponent) iComponent).setValue(isSittingCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof TamedComponent) {
                            if (isTamedCheckBox.isEnabled()) {
                                ((TamedComponent) iComponent).setValue(isTamedCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof WitherComponent) {
                            if (isWitherCheckBox.isEnabled()) {
                                ((WitherComponent) iComponent).setValue(isWitherCheckBox.isSelected());
                            }
                        } else if (iComponent instanceof HorseTypeComponent) {
                            if (horseType_comboBox.isEnabled()) {
                                ((HorseTypeComponent) iComponent).setValue(((HorseTypeItem)horseType_comboBox.getSelectedItem()).getType());
                            }
                        } else if (iComponent instanceof VariantComponent) {
                            if (variant_comboBox.isEnabled()) {
                                ((VariantComponent) iComponent).setValue(((VariantItem)variant_comboBox.getSelectedItem()).getVariant());
                            }
                        } else if (iComponent instanceof CatTypeComponent) {
                            if (catType_comboBox.isEnabled()) {
                                ((CatTypeComponent) iComponent).setValue(((CatTypeItem)catType_comboBox.getSelectedItem()).getType());
                            }
                        } else if (iComponent instanceof ColorComponent) {
                            if (color_comboBox.isEnabled()) {
                                ((ColorComponent) iComponent).setValue(((ColorItem) color_comboBox.getSelectedItem()).getColor_id());
                            }
                        } else if (iComponent instanceof ProfessionComponent) {
                            if (profession_comboBox.isEnabled()) {
                                ((ProfessionComponent) iComponent).setValue(((ProfessionItem) profession_comboBox.getSelectedItem()).getProfession());
                            }
                        } else if (iComponent instanceof SizeComponent) {
                            if (field_Size.isEnabled()) {
                                ((SizeComponent) iComponent).setValue(Integer.parseInt(field_Size.getText()));
                            }
                        } else if (iComponent instanceof EquipmentWeaponComponent) {
                            if (field_weapon_id.isEnabled() && field_weapon_id.getText().equals("")) {
                                editTemplate.getComponents().remove(iComponent);
                            } else if (field_Range_Damage.isEnabled()) {
                                JSONObject weaponObj = new JSONObject();
                                if(Util.isInt(field_weapon_id.getText())) {
                                    weaponObj.put("material",Integer.parseInt(field_weapon_id.getText()));
                                } else {
                                    weaponObj.put("material",field_weapon_id.getText());
                                }
                                if(!field_weapon_data.getText().equals("")) {
                                    if(Util.isInt(field_weapon_data.getText())) {
                                        weaponObj.put("data",Integer.parseInt(field_weapon_data.getText()));
                                    } else {
                                        weaponObj.put("data",field_weapon_data.getText());
                                    }
                                }
                                if(!field_weapon_tag.getText().equals("")) {
                                    weaponObj.put("tag",field_weapon_tag.getText());
                                }
                                ((EquipmentWeaponComponent) iComponent).setValue(weaponObj);
                            }
                        } else if(iComponent instanceof EquipmentArmorComponent) {
                            if(field_helmet_id.isEnabled() && field_helmet_id.getText().equals("")
                               && field_chest_id.isEnabled() && field_chest_id.getText().equals("")
                               && field_leggins_id.isEnabled() && field_leggins_id.getText().equals("")
                               && field_boots_id.isEnabled() && field_boots_id.getText().equals("")) {
                                  editTemplate.getComponents().remove(iComponent);
                            } else {
                                if (field_helmet_id.isEnabled() && field_helmet_id.getText().equals("")) {
                                    ((EquipmentArmorComponent) iComponent).getValue().remove("helmet");
                                } else if(field_helmet_id.isEnabled()){
                                    JSONObject helmetObj = new JSONObject();
                                    if(Util.isInt(field_helmet_id.getText())) {
                                        helmetObj.put("material",Integer.parseInt(field_helmet_id.getText()));
                                    } else {
                                        helmetObj.put("material",field_helmet_id.getText());
                                    }
                                    if(!field_helmet_data.getText().equals("")) {
                                        if(Util.isInt(field_helmet_data.getText())) {
                                            helmetObj.put("data",Integer.parseInt(field_helmet_data.getText()));
                                        } else {
                                            helmetObj.put("data",field_helmet_data.getText());
                                        }
                                    }
                                    if(!field_helmet_tag.getText().equals("")) {
                                        helmetObj.put("tag",field_helmet_tag.getText());
                                    }
                                    ((EquipmentArmorComponent) iComponent).getValue().put("helmet",helmetObj);
                                }
                                if (field_chest_id.isEnabled() && field_chest_id.getText().equals("")) {
                                    ((EquipmentArmorComponent) iComponent).getValue().remove("chestplate");
                                } else if(field_chest_id.isEnabled()){
                                    JSONObject chestObj = new JSONObject();
                                    if(Util.isInt(field_chest_id.getText())) {
                                        chestObj.put("material", Integer.parseInt(field_chest_id.getText()));
                                    } else {
                                        chestObj.put("material", field_chest_id.getText());
                                    }
                                    if(!field_chest_data.getText().equals("")) {
                                        if(Util.isInt(field_chest_data.getText())) {
                                            chestObj.put("data", Integer.parseInt(field_chest_data.getText()));
                                        } else {
                                            chestObj.put("data", field_chest_data.getText());
                                        }
                                    }
                                    if(!field_chest_tag.getText().equals("")) {
                                        chestObj.put("tag", field_chest_tag.getText());
                                    }
                                    ((EquipmentArmorComponent) iComponent).getValue().put("chestplate",chestObj);
                                }
                                if (field_leggins_id.isEnabled() && field_leggins_id.getText().equals("")) {
                                    ((EquipmentArmorComponent) iComponent).getValue().remove("leggins");
                                } else if(field_leggins_id.isEnabled()){
                                    JSONObject legginsObj = new JSONObject();
                                    if(Util.isInt(field_leggins_id.getText())) {
                                        legginsObj.put("material", Integer.parseInt(field_leggins_id.getText()));
                                    } else {
                                        legginsObj.put("material", field_leggins_id.getText());
                                    }
                                    if(!field_leggins_data.getText().equals("")) {
                                        if(Util.isInt(field_leggins_data.getText())) {
                                            legginsObj.put("data", Integer.parseInt(field_leggins_data.getText()));
                                        } else {
                                            legginsObj.put("data", field_leggins_data.getText());
                                        }
                                    }
                                    if(!field_leggins_tag.getText().equals("")) {
                                        legginsObj.put("tag", field_leggins_tag.getText());
                                    }
                                    ((EquipmentArmorComponent) iComponent).getValue().put("leggins",legginsObj);
                                }
                                if (field_boots_id.isEnabled() && field_boots_id.getText().equals("")) {
                                    ((EquipmentArmorComponent) iComponent).getValue().remove("boots");
                                } else if(field_boots_id.isEnabled()){
                                    JSONObject bootsObj = new JSONObject();
                                    if(Util.isInt(field_boots_id.getText())) {
                                        bootsObj.put("material", Integer.parseInt(field_boots_id.getText()));
                                    } else {
                                        bootsObj.put("material", field_boots_id.getText());
                                    }
                                    if(!field_boots_data.getText().equals("")) {
                                        if(Util.isInt(field_boots_data.getText())) {
                                            bootsObj.put("data", Integer.parseInt(field_boots_data.getText()));
                                        } else {
                                            bootsObj.put("data", field_boots_data.getText());
                                        }
                                    }
                                    if(!field_boots_id.getText().equals("")) {
                                        bootsObj.put("tag", field_boots_tag.getText());
                                    }
                                    ((EquipmentArmorComponent) iComponent).getValue().put("boots",bootsObj);
                                }
                            }
                        }

                    }


                    //Create Components if they don't exist
                    if (field_Meele_Damage.isEnabled() && !field_Meele_Damage.getText().equals("")) {
                        if (!componentExists(editTemplate, MeeleDamageComponent.class)) {
                            editTemplate.getComponents().add(new MeeleDamageComponent(Double.parseDouble(field_Meele_Damage.getText())));
                        }
                    }
                    if (field_Range_Damage.isEnabled() && !field_Range_Damage.getText().equals("")) {
                        if (!componentExists(editTemplate, RangeDamageComponent.class)) {
                            Map<String, Double> tmpMap = new HashMap<String, Double>();
                            tmpMap.put(((String) combobox_Projectile.getSelectedItem()).replace(" ", ""), Double.parseDouble(field_Range_Damage.getText()));
                            editTemplate.getComponents().add(new RangeDamageComponent(tmpMap));
                        }
                    }
                    if (field_Armor.isEnabled() && !field_Armor.getText().equals("")) {
                        if (!componentExists(editTemplate, ArmorComponent.class)) {
                            editTemplate.getComponents().add(new ArmorComponent(Integer.parseInt(field_Armor.getText())));
                        }
                    }
                    if (field_Age.isEnabled() && !field_Age.getText().equals("")) {
                        if (!componentExists(editTemplate, AgeComponent.class)) {
                            editTemplate.getComponents().add(new AgeComponent(Integer.parseInt(field_Age.getText())));
                        }
                    }
                    if (isBabyCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, BabyComponent.class)) {
                            editTemplate.getComponents().add(new BabyComponent(isBabyCheckBox.isSelected()));
                        }
                    }
                    if (isAngryCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, AngryComponent.class)) {
                            editTemplate.getComponents().add(new AngryComponent(isAngryCheckBox.isSelected()));
                        }
                    }
                    if (onFireCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, FireComponent.class)) {
                            editTemplate.getComponents().add(new FireComponent(onFireCheckBox.isSelected()));
                        }
                    }
                    if (hasChestCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, ChestComponent.class)) {
                            editTemplate.getComponents().add(new ChestComponent(hasChestCheckBox.isSelected()));
                        }
                    }
                    if (isPoweredCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, PoweredComponent.class)) {
                            editTemplate.getComponents().add(new PoweredComponent(isPoweredCheckBox.isSelected()));
                        }
                    }
                    if (hasSaddleCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, SaddleComponent.class)) {
                            editTemplate.getComponents().add(new SaddleComponent(hasSaddleCheckBox.isSelected()));
                        }
                    }
                    if (isSittingCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, SittingComponent.class)) {
                            editTemplate.getComponents().add(new SittingComponent(isSittingCheckBox.isSelected()));
                        }
                    }
                    if (isTamedCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, TamedComponent.class)) {
                            editTemplate.getComponents().add(new TamedComponent(isTamedCheckBox.isSelected()));
                        }
                    }
                    if (isWitherCheckBox.isEnabled()) {
                        if (!componentExists(editTemplate, WitherComponent.class)) {
                            editTemplate.getComponents().add(new WitherComponent(isWitherCheckBox.isSelected()));
                        }
                    }
                    if (horseType_comboBox.isEnabled()) {
                        if (!componentExists(editTemplate, HorseTypeComponent.class)) {
                            editTemplate.getComponents().add(new HorseTypeComponent(((HorseTypeItem)horseType_comboBox.getSelectedItem()).getType()));
                        }
                    }
                    if (variant_comboBox.isEnabled()) {
                        if (!componentExists(editTemplate, VariantComponent.class)) {
                            editTemplate.getComponents().add(new VariantComponent(((VariantItem)variant_comboBox.getSelectedItem()).getVariant()));
                        }
                    }
                    if (catType_comboBox.isEnabled()) {
                        if (!componentExists(editTemplate, CatTypeComponent.class)) {
                            editTemplate.getComponents().add(new CatTypeComponent(((CatTypeItem)catType_comboBox.getSelectedItem()).getType()));
                        }
                    }
                    if (color_comboBox.isEnabled()) {
                        if (!componentExists(editTemplate, ColorComponent.class)) {
                            editTemplate.getComponents().add(new ColorComponent(((ColorItem) color_comboBox.getSelectedItem()).getColor_id()));
                        }
                    }
                    if (profession_comboBox.isEnabled()) {
                        if (!componentExists(editTemplate, ProfessionComponent.class)) {
                            editTemplate.getComponents().add(new ProfessionComponent(((ProfessionItem) profession_comboBox.getSelectedItem()).getProfession()));
                        }
                    }
                    if (field_Size.isEnabled() && !field_Size.getText().equals("")) {
                        if (!componentExists(editTemplate, SizeComponent.class)) {
                            editTemplate.getComponents().add(new SizeComponent(Integer.parseInt(field_Size.getText())));
                        }
                    }
                    if (field_weapon_id.isEnabled() && !field_weapon_id.getText().equals("")) {
                        if(!componentExists(editTemplate, EquipmentWeaponComponent.class)) {
                            JSONObject weaponObj = new JSONObject();
                            if(Util.isInt(field_weapon_id.getText())) {
                                weaponObj.put("material",Integer.parseInt(field_weapon_id.getText()));
                            } else {
                                weaponObj.put("material",field_weapon_id.getText());
                            }
                            if(!field_weapon_data.getText().equals("")) {
                                if(Util.isInt(field_weapon_data.getText())) {
                                    weaponObj.put("data",Integer.parseInt(field_weapon_data.getText()));
                                } else {
                                    weaponObj.put("data",field_weapon_data.getText());
                                }
                            }
                            if(!field_weapon_tag.getText().equals("")) {
                                weaponObj.put("tag",field_weapon_tag.getText());
                            }
                            editTemplate.getComponents().add(new EquipmentWeaponComponent(weaponObj));
                        }
                    }
                    if(field_helmet_id.isEnabled() && !field_helmet_id.getText().equals("")
                            || field_chest_id.isEnabled() && !field_chest_id.getText().equals("")
                            || field_leggins_id.isEnabled() && !field_leggins_id.getText().equals("")
                            || field_boots_id.isEnabled() && !field_boots_id.getText().equals("")) {
                        Map<String, JSONObject> newMap = new HashMap<String, JSONObject>();
                        if(field_helmet_id.isEnabled()){
                            JSONObject helmetObj = new JSONObject();
                            if(Util.isInt(field_helmet_id.getText())) {
                                helmetObj.put("material",Integer.parseInt(field_helmet_id.getText()));
                            } else {
                                helmetObj.put("material",field_helmet_id.getText());
                            }
                            if(!field_helmet_data.getText().equals("")) {
                                if(Util.isInt(field_helmet_data.getText())) {
                                    helmetObj.put("data",Integer.parseInt(field_helmet_data.getText()));
                                } else {
                                    helmetObj.put("data",field_helmet_data.getText());
                                }
                            }
                            if(!field_helmet_tag.getText().equals("")) {
                                helmetObj.put("tag",field_helmet_tag.getText());
                            }
                            newMap.put("helmet", helmetObj);
                        }
                        if(field_chest_id.isEnabled()){
                            JSONObject chestObj = new JSONObject();
                            if(Util.isInt(field_chest_id.getText())) {
                                chestObj.put("material", Integer.parseInt(field_chest_id.getText()));
                            } else {
                                chestObj.put("material", field_chest_id.getText());
                            }
                            if(!field_chest_data.getText().equals("")) {
                                if(Util.isInt(field_chest_data.getText())) {
                                    chestObj.put("data", Integer.parseInt(field_chest_data.getText()));
                                } else {
                                    chestObj.put("data", field_chest_data.getText());
                                }
                            }
                            if(!field_chest_tag.getText().equals("")) {
                                chestObj.put("tag", field_chest_tag.getText());
                            }
                            newMap.put("chestplate", chestObj);
                        }
                        if(field_leggins_id.isEnabled()){
                            JSONObject legginsObj = new JSONObject();
                            if(Util.isInt(field_leggins_id.getText())) {
                                legginsObj.put("material", Integer.parseInt(field_leggins_id.getText()));
                            } else {
                                legginsObj.put("material", field_leggins_id.getText());
                            }
                            if(!field_leggins_data.getText().equals("")) {
                                if(Util.isInt(field_leggins_data.getText())) {
                                    legginsObj.put("data", Integer.parseInt(field_leggins_data.getText()));
                                } else {
                                    legginsObj.put("data", field_leggins_data.getText());
                                }
                            }
                            if(!field_leggins_tag.getText().equals("")) {
                                legginsObj.put("tag", field_leggins_tag.getText());
                            }
                            newMap.put("leggins", legginsObj);
                        }
                        if(field_boots_id.isEnabled()){
                            JSONObject bootsObj = new JSONObject();
                            if(Util.isInt(field_boots_id.getText())) {
                                bootsObj.put("material", Integer.parseInt(field_boots_id.getText()));
                            } else {
                                bootsObj.put("material", field_boots_id.getText());
                            }
                            if(!field_boots_data.getText().equals("")) {
                                if(Util.isInt(field_boots_data.getText())) {
                                    bootsObj.put("data", Integer.parseInt(field_boots_data.getText()));
                                } else {
                                    bootsObj.put("data", field_boots_data.getText());
                                }
                            }
                            if(!field_boots_tag.getText().equals("")) {
                                bootsObj.put("tag", field_boots_tag.getText());
                            }
                            newMap.put("boots", bootsObj);
                        }
                        editTemplate.getComponents().add(new EquipmentArmorComponent(newMap));
                    }
                    if(createMode) {
                        //templateList.remove(toDelete);
                        templateList.add(editTemplate);
                    }

                    loadCreaturesInTemplate();
                }

            }
        });

        entityTemplateTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && entityTemplateTree.getSelectionPath() != null) {
                    mobTypeComboBox.setEnabled(false);
                    if (entityTemplateTree.getSelectionPath().getPath().length == 2) {
                        if (entityTemplateTree.getSelectionPath().getPathComponent(1) instanceof TemplateNode) {
                            EntityTemplate template = ((TemplateNode) entityTemplateTree.getSelectionPath().getPathComponent(1)).getEntityTemplate();
                            mobTypeComboBox.setEnabled(false);
                            createMode = false;
                            editTemplate(template);
                        }
                    }
                }
            }
        });

        entityTemplateTree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (entityTemplateTree.getSelectionPath().getPath().length == 2) {
                    if (entityTemplateTree.getSelectionPath().getPathComponent(1) instanceof TemplateNode) {
                        EntityTemplate template = ((TemplateNode) entityTemplateTree.getSelectionPath().getPathComponent(1)).getEntityTemplate();
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_ENTER:
                                mobTypeComboBox.setEnabled(false);
                                createMode = false;
                                editTemplate(template);
                                break;
                            case KeyEvent.VK_DELETE:
                               /* selectedMobtype.removeSkillTree(template.getClassName());
                                skilltreeTreeSetSkilltrees();    */
                                break;
                        }
                    }
                }

            }
        });
        loader = new EntityTemplateLoader();
        templateList = new ArrayList<EntityTemplate>();
        loadCreaturesInTemplate();
    }

    @SuppressWarnings("unchecked")
    public void createUIComponents() {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        entityTemplateTreeModel = new DefaultTreeModel(root);
        entityTemplateTree = new JTree(entityTemplateTreeModel);
        entityTemplateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        mobTypeComboBox = new JComboBox(MOB_TYPES);
        combobox_Projectile = new JComboBox(new String[] {"Arrow", "Wither Skull","Snowball", "Large Fireball", "Small Fireball"});

        horseType_comboBox = new JComboBox(new HorseTypeItem().getTypes());
        variant_comboBox = new JComboBox(new VariantItem().getVariants());
        catType_comboBox = new JComboBox(new CatTypeItem().getTypes());
        color_comboBox = new JComboBox(new ColorItem().getColors());
        profession_comboBox = new JComboBox(new ProfessionItem().getProfessions());

        //deactivateAllTemplatePanel();
    }

    public void templateTreeExpandAll() {
        for (int i = 0; i < entityTemplateTree.getRowCount(); i++) {
            entityTemplateTree.expandRow(i);
        }
    }

    @Override
    public JPanel getPanel() {
        return entityCreatorPanel;
    }

    @Override
    public void openDungeon(File dungeonFolder) {
        templateFile = new File(dungeonFolder, "entity-templates.json");
        if(templateFile.exists()) {
            templateList = loader.loadCreaturesInTemplate(templateFile);
            loadCreaturesInTemplate();
        }
    }

    @Override
    public void saveDungeon() {
        loader.saveAsJson(templateList, templateFile);
    }

    public void loadCreaturesInTemplate() {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Templates");
        entityTemplateTreeModel.setRoot(rootNode);

        for (EntityTemplate entityTemplate : templateList) {
            TemplateNode templateNode = new TemplateNode(entityTemplate);
            rootNode.add(templateNode);
        }
        templateTreeExpandAll();

    }

    @Override
    public String getName() {
        return "Entity Creator";
    }

    private class TemplateNode extends DefaultMutableTreeNode {
        private EntityTemplate template;

        public TemplateNode(EntityTemplate template) {
            super(template.getTemplateId() + " - MobType: " + template.getEntityType());
            this.template = template;
        }

        public EntityTemplate getEntityTemplate() {
            return template;
        }
    }

    public boolean componentExists(EntityTemplate template, Class<? extends IComponent> clazz) {

        for (IComponent com : template.getComponents()) {
            if (clazz.isInstance(com)) {
                return true;
            }
        }
        return false;
    }

    public void editTemplate(EntityTemplate template) {
        editTemplate = template;
        deactivateAllTemplatePanel();
        activateWalkSpeed(true, template);
        activateMaxHealth(true, template);
        activeEntityTemplatID(true, template);
        activateDisplayName(true,template);
        activateMeeleDamage(true, template);
        activateRangeDamage(true, template);
        activateArmor(true, template);
        activateSaveButton();
        if (template.getEntityType().equals("Blaze")) {
            activateFire(true, template);
        } else if (template.getEntityType().equals("Creeper")) {
            activatePowered(true, template);
        } else if (template.getEntityType().equals("Enderman")) {
            activateAngry(true, template);
            activateWeapon(true, template);
        } else if (template.getEntityType().equals("Horse")) {
            activateAge(true, template);
            activateBaby(true, template);
            activateTamed(true, template);
            activateVariant(true, template);
            activateChest(true, template);
            activateSaddle(true, template);
            activateHorseType(true, template);
        } else if (template.getEntityType().equals("MagmaCube") || template.getEntityType().equals("Slime")) {
            activateSize(true, template);
        } else if (template.getEntityType().equals("Ocelot")) {
            activateCatType(true, template);
            activateSit(true, template);
        } else if (template.getEntityType().equals("Pig") && !template.getEntityType().contains("Zombie")) {
            activateSaddle(true, template);
        } else if (template.getEntityType().equals("PigZombie")) {
            activateArmorEquip(true, template);
            activateWeapon(true, template);
        } else if (template.getEntityType().equals("Sheep")) {
            activateColor(true, template);
        } else if (template.getEntityType().equals("Skeleton")) {
            activateArmorEquip(false, template);
            activateWeapon(true, template);
            activateWither(true, template);
        } else if (template.getEntityType().equals("Villager")) {
            activateProfession(true, template);
        } else if (template.getEntityType().equals("Wither")) {
            activateWither(true, template);
        } else if (template.getEntityType().equals("Wolf")) {
            activateColor(true, template);
            activateSit(true, template);
            activateTamed(true, template);
            activateAngry(true, template);
        } else if (template.getEntityType().equals("Zombie") && !template.getEntityType().contains("Pig")) {
            activateArmorEquip(true, template);
            activateWeapon(true, template);
        }
    }


    public void activateMeeleDamage(boolean editMode, EntityTemplate template) {
        field_Meele_Damage.setEnabled(true);
        field_Meele_Damage.setText("");

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof MeeleDamageComponent) {
                    field_Meele_Damage.setText(c.getValue().toString());
                    break;
                }
            }
        }
    }

    public void activeEntityTemplatID(boolean editMode, EntityTemplate template) {
        field_Template_ID.setEnabled(true);
        field_Template_ID.setText("");

        if (editMode) {
            field_Template_ID.setText(template.getTemplateId());
        }
    }

    @SuppressWarnings("unchecked")
    public void activateRangeDamage(boolean editMode, EntityTemplate template) {
        field_Range_Damage.setEnabled(true);
        combobox_Projectile.setEnabled(true);
        field_Range_Damage.setText("");
        combobox_Projectile.setSelectedIndex(0);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof RangeDamageComponent) {
                    for (String key : ((HashMap<String, Double>) c.getValue()).keySet()) {
                        field_Range_Damage.setText(((HashMap<String, Double>) c.getValue()).get(key).toString());
                        for (int i = 0; i < combobox_Projectile.getItemCount(); i++) {
                            if (((String) combobox_Projectile.getItemAt(i)).replace(" ", "").equalsIgnoreCase(key)) {
                                combobox_Projectile.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void activateFire(boolean editMode, EntityTemplate template) {
        onFireCheckBox.setEnabled(true);
        onFireCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof FireComponent) {
                    onFireCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activatePowered(boolean editMode, EntityTemplate template) {
        isPoweredCheckBox.setEnabled(true);
        isPoweredCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof PoweredComponent) {
                    isPoweredCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateWeapon(boolean editMode, EntityTemplate template) {
        field_weapon_id.setEnabled(true);
        field_weapon_data.setEnabled(true);
        field_weapon_tag.setEnabled(true);
        field_weapon_id.setText("");
        field_weapon_data.setText("");
        field_weapon_tag.setText("");

        if (editMode) {
            for(IComponent c : template.getComponents()) {
                if(c instanceof EquipmentWeaponComponent) {
                    JSONObject weaponObj = ((EquipmentWeaponComponent) c).getValue();
                    if(weaponObj.containsKey("material")) {
                        field_weapon_id.setText(weaponObj.get("material").toString());
                    }
                    if(weaponObj.containsKey("data")) {
                        field_weapon_data.setText(weaponObj.get("data").toString());
                    }
                    if(weaponObj.containsKey("tag")) {
                        field_weapon_tag.setText(weaponObj.get("tag").toString());
                    }
                }
            }
        }
    }

    public void activateAge(boolean editMode, EntityTemplate template) {
        field_Age.setEnabled(true);
        field_Age.setText("");

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof AgeComponent) {
                    field_Age.setText(c.getValue().toString());
                    break;
                }
            }
        }
    }

    public void activateTamed(boolean editMode, EntityTemplate template) {
        isTamedCheckBox.setEnabled(true);
        isTamedCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof TamedComponent) {
                    isTamedCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateVariant(boolean editMode, EntityTemplate template) {
        variant_comboBox.setEnabled(true);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof VariantComponent) {
                    for (int i = 0; i < variant_comboBox.getItemCount(); i++) {
                        if (((VariantItem)variant_comboBox.getItemAt(i)).getVariant() == ((VariantComponent)c).getValue()) {
                            variant_comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void activateChest(boolean editMode, EntityTemplate template) {
        hasChestCheckBox.setEnabled(true);
        hasChestCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof ChestComponent) {
                    hasChestCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateSaddle(boolean editMode, EntityTemplate template) {
        hasSaddleCheckBox.setEnabled(true);
        hasSaddleCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof SaddleComponent) {
                    hasSaddleCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateBaby(boolean editMode, EntityTemplate template) {
        isBabyCheckBox.setEnabled(true);
        isBabyCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof BabyComponent) {
                    isBabyCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateSize(boolean editMode, EntityTemplate template) {
        field_Size.setEnabled(true);
        field_Size.setText("");
        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof SizeComponent) {
                    field_Size.setText(c.getValue().toString());
                    break;
                }
            }
        }
    }

    public void activateSit(boolean editMode, EntityTemplate template) {
        isSittingCheckBox.setEnabled(true);
        isSittingCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof SittingComponent) {
                    isSittingCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateCatType(boolean editMode, EntityTemplate template) {
        catType_comboBox.setEnabled(true);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof CatTypeComponent) {
                    for (int i = 0; i < catType_comboBox.getItemCount(); i++) {
                        if (((CatTypeItem)catType_comboBox.getItemAt(i)).getType() == ((CatTypeComponent)c).getValue()) {
                            catType_comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void activateArmorEquip(boolean editMode, EntityTemplate template) {
        field_helmet_data.setEnabled(true);
        field_helmet_id.setEnabled(true);
        field_helmet_tag.setEnabled(true);
        field_leggins_data.setEnabled(true);
        field_leggins_id.setEnabled(true);
        field_leggins_tag.setEnabled(true);
        field_chest_data.setEnabled(true);
        field_chest_id.setEnabled(true);
        field_chest_tag.setEnabled(true);
        field_boots_data.setEnabled(true);
        field_boots_id.setEnabled(true);
        field_boots_tag.setEnabled(true);

        field_helmet_id.setText("");
        field_helmet_data.setText("");
        field_helmet_tag.setText("");
        field_leggins_data.setText("");
        field_leggins_id.setText("");
        field_leggins_tag.setText("");
        field_chest_data.setText("");
        field_chest_id.setText("");
        field_chest_tag.setText("");
        field_boots_data.setText("");
        field_boots_id.setText("");
        field_boots_tag.setText("");

        if (editMode) {
            for(IComponent c : template.getComponents()) {
                if(c instanceof EquipmentArmorComponent) {
                    Map<String,JSONObject> jsonObjectMap = ((EquipmentArmorComponent)c).getValue();

                    for(String key : jsonObjectMap.keySet()) {
                        if(key.equals("helmet")) {
                            if(jsonObjectMap.get(key).containsKey("material")) {
                                field_helmet_id.setText(jsonObjectMap.get(key).get("material").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("data")) {
                                field_helmet_data.setText(jsonObjectMap.get(key).get("data").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("tag")) {
                                field_helmet_tag.setText(jsonObjectMap.get(key).get("tag").toString());
                            }
                        } else if(key.equals("chestplate")) {
                            if(jsonObjectMap.get(key).containsKey("material")) {
                                field_chest_id.setText(jsonObjectMap.get(key).get("material").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("data")) {
                                field_chest_data.setText(jsonObjectMap.get(key).get("data").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("tag")) {
                                field_chest_tag.setText(jsonObjectMap.get(key).get("tag").toString());
                            }
                        } else if(key.equals("leggins")) {
                            if(jsonObjectMap.get(key).containsKey("material")) {
                                field_leggins_id.setText(jsonObjectMap.get(key).get("material").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("data")) {
                                field_leggins_data.setText(jsonObjectMap.get(key).get("data").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("tag")) {
                                field_leggins_tag.setText(jsonObjectMap.get(key).get("tag").toString());
                            }
                        } else if(key.equals("boots")) {
                            if(jsonObjectMap.get(key).containsKey("material")) {
                                field_boots_id.setText(jsonObjectMap.get(key).get("material").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("data")) {
                                field_boots_data.setText(jsonObjectMap.get(key).get("data").toString());
                            }
                            if(jsonObjectMap.get(key).containsKey("tag")) {
                                field_boots_tag.setText(jsonObjectMap.get(key).get("tag").toString());
                            }
                        }
                    }
                }
            }
        }
    }

    public void activateColor(boolean editMode, EntityTemplate template) {
        color_comboBox.setEnabled(true);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof ColorComponent) {
                    for (int i = 0; i < color_comboBox.getItemCount(); i++) {
                        if (((ColorItem)color_comboBox.getItemAt(i)).getColor_id() == ((ColorComponent)c).getValue()) {
                            color_comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void activateWither(boolean editMode, EntityTemplate template) {
        isWitherCheckBox.setEnabled(true);
        isWitherCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof WitherComponent) {
                    isWitherCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateProfession(boolean editMode, EntityTemplate template) {
        profession_comboBox.setEnabled(true);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof ProfessionComponent) {
                    for (int i = 0; i < profession_comboBox.getItemCount(); i++) {
                        if (((ProfessionItem)profession_comboBox.getItemAt(i)).getProfession() == ((ProfessionComponent)c).getValue()) {
                            profession_comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void activateAngry(boolean editMode, EntityTemplate template) {
        isAngryCheckBox.setEnabled(true);
        isAngryCheckBox.setSelected(false);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof AngryComponent) {
                    isAngryCheckBox.setSelected(Boolean.parseBoolean(c.getValue().toString()));
                    break;
                }
            }
        }
    }

    public void activateArmor(boolean editMode, EntityTemplate template) {
        field_Armor.setEnabled(true);
        field_Armor.setText("");

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof ArmorComponent) {
                    field_Armor.setText(c.getValue().toString());
                    break;
                }
            }
        }
    }

    public void activateHorseType(boolean editMode, EntityTemplate template) {
        horseType_comboBox.setEnabled(true);

        if (editMode) {
            for (IComponent c : template.getComponents()) {
                if (c instanceof HorseTypeComponent) {
                    for (int i = 0; i < horseType_comboBox.getItemCount(); i++) {
                        if (((HorseTypeItem)horseType_comboBox.getItemAt(i)).getType() == ((HorseTypeComponent)c).getValue()) {
                            horseType_comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void activateMaxHealth(boolean editMode, EntityTemplate template) {
        field_Max_Health.setEnabled(true);
        field_Max_Health.setText("");

        if (editMode) {
            field_Max_Health.setText(template.getMaxHealth().toString());
        }
    }

    public void activateWalkSpeed(boolean editMode, EntityTemplate template) {
        field_Walk_Speed.setEnabled(true);
        field_Walk_Speed.setText("");

        if (editMode) {
            field_Walk_Speed.setText((template.getWalkSpeed().toString()));
        }
    }

    public void activateDisplayName(boolean editMode, EntityTemplate template) {
        field_Display_Name.setEnabled(true);
        field_Display_Name.setText("");

        if(editMode) {
            field_Display_Name.setText(template.getDisplayName());
        }
    }

    public void activateSaveButton() {
        saveButton1.setEnabled(true);
    }

    public void deactivateAllTemplatePanel() {
        field_Meele_Damage.setEnabled(false);
        field_Range_Damage.setEnabled(false);
        field_Template_ID.setEnabled(false);
        combobox_Projectile.setEnabled(false);
        saveButton1.setEnabled(false);
        onFireCheckBox.setEnabled(false);
        isPoweredCheckBox.setEnabled(false);
        field_weapon_data.setEnabled(false);
        field_weapon_id.setEnabled(false);
        field_weapon_tag.setEnabled(false);
        field_Age.setEnabled(false);
        isTamedCheckBox.setEnabled(false);
        variant_comboBox.setEnabled(false);
        hasChestCheckBox.setEnabled(false);
        hasSaddleCheckBox.setEnabled(false);
        isBabyCheckBox.setEnabled(false);
        field_Size.setEnabled(false);
        isSittingCheckBox.setEnabled(false);
        catType_comboBox.setEnabled(false);
        field_helmet_data.setEnabled(false);
        field_helmet_id.setEnabled(false);
        field_helmet_tag.setEnabled(false);
        field_leggins_data.setEnabled(false);
        field_leggins_id.setEnabled(false);
        field_leggins_tag.setEnabled(false);
        field_chest_data.setEnabled(false);
        field_chest_id.setEnabled(false);
        field_chest_tag.setEnabled(false);
        field_boots_data.setEnabled(false);
        field_boots_id.setEnabled(false);
        field_boots_tag.setEnabled(false);
        color_comboBox.setEnabled(false);
        profession_comboBox.setEnabled(false);
        isAngryCheckBox.setEnabled(false);
        field_Armor.setEnabled(false);
        horseType_comboBox.setEnabled(false);
        field_Walk_Speed.setEnabled(false);
        field_Max_Health.setEnabled(false);
        field_Display_Name.setEnabled(false);
    }
}