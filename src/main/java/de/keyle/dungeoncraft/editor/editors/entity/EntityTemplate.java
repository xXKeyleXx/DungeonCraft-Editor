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

import de.keyle.dungeoncraft.editor.editors.entity.Components.IComponent;

import java.util.ArrayList;
import java.util.List;

public class EntityTemplate {

    private String templateId;
    private String entityType;
    private Double maxHealth;
    private Double walkSpeed;
    private String displayName;

    private List<IComponent> components = new ArrayList<IComponent>();

    public List<IComponent> getComponents() {
        return components;
    }

    public void setComponents(List<IComponent> components) {
        this.components = components;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Double getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityTemplate template = (EntityTemplate) o;

        if (components != null ? !components.equals(template.components) : template.components != null) return false;
        if (!displayName.equals(template.displayName)) return false;
        if (!entityType.equals(template.entityType)) return false;
        if (!maxHealth.equals(template.maxHealth)) return false;
        if (!templateId.equals(template.templateId)) return false;
        if (!walkSpeed.equals(template.walkSpeed)) return false;

        return true;
    }
}
