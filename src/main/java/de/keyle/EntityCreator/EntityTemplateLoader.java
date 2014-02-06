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

package de.keyle.EntityCreator;


import de.keyle.EntityCreator.JsonConfig.ConfigurationJson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EntityTemplateLoader {


    public EntityTemplateLoader() {

    }

    public List<EntityTemplate> loadCreaturesInTemplate(File templateFile) {

        if (templateFile.exists()) {
            ConfigurationJson jsonConfig = new ConfigurationJson(templateFile);
            jsonConfig.load();

            JSONObject root = jsonConfig.getJSONObject();
            List<EntityTemplate> templates_list = new ArrayList<EntityTemplate>();
            if (root.containsKey("templates")) {
                JSONArray templates = (JSONArray) root.get("templates");

                for (Object templateObject : templates) {
                    if (templateObject instanceof JSONObject) {
                        JSONObject template = (JSONObject) templateObject;
                        EntityTemplate entityTemplate = new EntityTemplate();
                        if (!template.containsKey("template-id") || !template.containsKey("entity-type")) {
                            //DebugLogger.warning("\"template-id\" node is missing in a template!", "DC]b:[" + dungeonBase.getName());
                            continue;
                        }
                        String templateId = (String) template.get("template-id");
                        String entityTypeName = (String) template.get("entity-type");
                        //EntityType entityType = EntityType.getEntityTypeByName(entityTypeName);

                        entityTemplate.setTemplateId(templateId);
                        entityTemplate.setEntityType(entityTypeName);

                        if (template.containsKey("max-health") /*&& Util.isDouble(template.get("max-health").toString())*/) {
                            double maxHealth = Double.parseDouble(template.get("max-health").toString());
                            entityTemplate.setMaxHealth(maxHealth);
                        }
                        if (template.containsKey("walk-speed") /*&& Util.isDouble(template.get("walk-speed").toString())*/) {
                            float walkSpeed = Float.parseFloat(template.get("walk-speed").toString());
                            entityTemplate.setWalkSpeed(walkSpeed);
                        }
                        if (template.containsKey("display-name")) {
                            String displayName = template.get("display-name").toString();
                            entityTemplate.setDisplayName(displayName);
                        }

                        if (template.containsKey("components")) {
                            Object componentsObject = template.get("components");
                            if (componentsObject instanceof JSONArray) {
                                JSONArray componentsArray = (JSONArray) componentsObject;
                                List<Component> componentList = new ArrayList<Component>();
                                for (Object componentObject : componentsArray) {
                                    if (componentObject instanceof JSONObject) {
                                        JSONObject component = (JSONObject) componentObject;
                                        if (!component.containsKey("class")) {
                                            //DebugLogger.warning("Component \"class\" node is missing in template with id: " + templateId, "DC]b:[" + dungeonBase.getName());
                                            continue;
                                        }
                                        JSONObject parameter = null;
                                        if (component.containsKey("parameter")) {
                                            Object parameterObject = component.get("parameter");
                                            if (parameterObject instanceof JSONObject) {
                                                parameter = (JSONObject) parameterObject;
                                            }
                                        }
                                        if (parameter == null) {
                                            parameter = new JSONObject();
                                        }
                                        Component comp = new Component();
                                        String className = (String) component.get("class");
                                        comp.setComponentName(className);

                                        if (comp.getComponentName().contains("MeeleDamage")) {
                                            comp.setMeeleDamage(Double.parseDouble(parameter.get("damage").toString()));
                                        } else if (comp.getComponentName().contains("RangedDamage")) {
                                            comp.setRangeDamage(Double.parseDouble(parameter.get("damage").toString()));
                                            comp.setProjectile(parameter.get("projectile").toString());
                                        } else if (comp.getComponentName().contains("AgeCom")) {
                                            comp.setAge(Integer.parseInt(parameter.get("age").toString()));
                                        } else if (comp.getComponentName().contains("AngryCom")) {
                                            comp.setAngry(Boolean.parseBoolean(parameter.get("angry").toString()));
                                        } else if (comp.getComponentName().contains("ArmorCom")) {
                                            //comp.setArmor(Integer.parseInt(parameter.get("armor").toString()));
                                        } else if (comp.getComponentName().contains("BabyCom")) {
                                            comp.setBaby(Boolean.parseBoolean(parameter.get("baby").toString()));
                                        } else if (comp.getComponentName().contains("CatCom")) {
                                            comp.setCatType(Integer.parseInt(parameter.get("catType").toString()));
                                        } else if (comp.getComponentName().contains("ChestCom")) {
                                            comp.setChest(Boolean.parseBoolean(parameter.get("chest").toString()));
                                        } else if (comp.getComponentName().contains("ColorCom")) {
                                            comp.setColor(Byte.parseByte(parameter.get("color").toString()));
                                        } else if (comp.getComponentName().contains("EquipmentArmorCom")) {
                                            comp.setHelmet((JSONObject) parameter.get("helmet"));
                                            comp.setChestplate((JSONObject) parameter.get("chestplate"));
                                            comp.setLeggins((JSONObject) parameter.get("leggins"));
                                            comp.setBoots((JSONObject) parameter.get("boots"));
                                        } else if (comp.getComponentName().contains("EquipmentWeaponCom")) {
                                            comp.setWeapon((JSONObject) parameter.get("weapon"));
                                        } else if (comp.getComponentName().contains("FireCom")) {
                                            comp.setFire(Boolean.parseBoolean(parameter.get("fire").toString()));
                                        } else if (comp.getComponentName().contains("HorseType")) {
                                            comp.setHorseType(Byte.parseByte(parameter.get("horseType").toString()));
                                        } else if (comp.getComponentName().contains("PoweredCom")) {
                                            comp.setPowered(Boolean.parseBoolean(parameter.get("powered").toString()));
                                        } else if (comp.getComponentName().contains("ProfessionCom")) {
                                            comp.setProfession(Integer.parseInt(parameter.get("profession").toString()));
                                        } else if (comp.getComponentName().contains("SaddleCom")) {
                                            comp.setSaddle(Boolean.parseBoolean(parameter.get("saddle").toString()));
                                        } else if (comp.getComponentName().contains("SitCom")) {
                                            comp.setSitting(Boolean.parseBoolean(parameter.get("sitting").toString()));
                                        } else if (comp.getComponentName().contains("SizeCom")) {
                                            comp.setSize(Integer.parseInt(parameter.get("size").toString()));
                                        } else if (comp.getComponentName().contains("TamedCom")) {
                                            comp.setTamed(Boolean.parseBoolean(parameter.get("tamed").toString()));
                                        } else if (comp.getComponentName().contains("VariantCom")) {
                                            comp.setVariant(Integer.parseInt(parameter.get("variant").toString()));
                                        } else if (comp.getComponentName().contains("WitherCom")) {
                                            comp.setWither(Boolean.parseBoolean(parameter.get("wither").toString()));
                                        }
                                        componentList.add(comp);
                                    }

                                }
                                entityTemplate.setComponents(componentList);
                            }
                        }
                        templates_list.add(entityTemplate);
                    }

                }
                return templates_list;
            } else {
                return null;
            }
        }
        return null;
    }
}

 /*class DungeonNode extends DefaultMutableTreeNode {
    //private SkillTree skillTree;

    public SkillTreeNode(String id, String type) {
        super(id + " - MobType: " + type);
        //.skillTree = skillTree;
    }

   /* public SkillTree getSkillTree() {
        return skillTree;
    }
}
*/

