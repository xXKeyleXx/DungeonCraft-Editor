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


import de.keyle.dungeoncraft.editor.editors.entity.Components.*;
import de.keyle.dungeoncraft.editor.util.ConfigurationJson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                List<IComponent> componentList = new ArrayList<IComponent>();
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
                                        IComponent comp = null;
                                        String className = (String) component.get("class");

                                        if (className.contains("MeeleDamage")) {
                                            comp = new MeeleDamageComponent(Double.parseDouble(parameter.get("damage").toString()));
                                        } else if (className.contains("RangedDamage")) {
                                            Map<String,Double> tmpMap = new HashMap<String, Double>();
                                            tmpMap.put(parameter.get("projectile").toString(),Double.parseDouble(parameter.get("damage").toString()));
                                            comp = new RangeDamageComponent(tmpMap);
                                        } else if (className.contains("AgeCom")) {
                                            comp = new AgeComponent(Integer.parseInt(parameter.get("age").toString()));
                                        } else if (className.contains("AngryCom")) {
                                            comp = new AngryComponent(Boolean.parseBoolean(parameter.get("angry").toString()));
                                        } else if (className.contains("ArmorCom") && !className.contains("Equip")) {
                                            comp = new ArmorComponent(Integer.parseInt(parameter.get("armor").toString()));
                                        } else if (className.contains("BabyCom")) {
                                            comp = new BabyComponent(Boolean.parseBoolean(parameter.get("baby").toString()));
                                        } else if (className.contains("CatCom")) {
                                            comp = new CatTypeComponent(Integer.parseInt(parameter.get("catType").toString()));
                                        } else if (className.contains("ChestCom")) {
                                            comp = new ChestComponent(Boolean.parseBoolean(parameter.get("chest").toString()));
                                        } else if (className.contains("ColorCom")) {
                                            comp = new ColorComponent(Byte.parseByte(parameter.get("color").toString()));
                                        } else if (className.contains("EquipmentArmorCom")) {
                                            List<JSONObject> tmpList = new ArrayList<JSONObject>();
                                            tmpList.add((JSONObject) parameter.get("helmet"));
                                            tmpList.add((JSONObject) parameter.get("chestplate"));
                                            tmpList.add((JSONObject) parameter.get("leggins"));
                                            tmpList.add((JSONObject) parameter.get("boots"));
                                            comp = new EquipmentArmorComponent(tmpList);
                                        } else if (className.contains("EquipmentWeaponCom")) {
                                            comp = new EquipmentWeaponComponent((JSONObject) parameter.get("weapon"));
                                        } else if (className.contains("FireCom")) {
                                            comp = new FireComponent(Boolean.parseBoolean(parameter.get("fire").toString()));
                                        } else if (className.contains("HorseType")) {
                                            comp = new HorseTypeComponent(Byte.parseByte(parameter.get("horseType").toString()));
                                        } else if (className.contains("PoweredCom")) {
                                            comp = new PoweredComponent(Boolean.parseBoolean(parameter.get("powered").toString()));
                                        } else if (className.contains("ProfessionCom")) {
                                            comp = new ProfessionComponent(Integer.parseInt(parameter.get("profession").toString()));
                                        } else if (className.contains("SaddleCom")) {
                                            comp = new SaddleComponent(Boolean.parseBoolean(parameter.get("saddle").toString()));
                                        } else if (className.contains("SitCom")) {
                                            comp = new SittingComponent(Boolean.parseBoolean(parameter.get("sitting").toString()));
                                        } else if (className.contains("SizeCom")) {
                                            comp = new SizeComponent(Integer.parseInt(parameter.get("size").toString()));
                                        } else if (className.contains("TamedCom")) {
                                            comp = new TamedComponent(Boolean.parseBoolean(parameter.get("tamed").toString()));
                                        } else if (className.contains("VariantCom")) {
                                            comp = new VariantComponent(Integer.parseInt(parameter.get("variant").toString()));
                                        } else if (className.contains("WitherCom")) {
                                            comp = new WitherComponent(Boolean.parseBoolean(parameter.get("wither").toString()));
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

