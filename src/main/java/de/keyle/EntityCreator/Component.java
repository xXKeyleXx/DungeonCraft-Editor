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


import org.json.simple.JSONObject;

public class Component {

    private String componentName;

    private Integer age = null;
    private Boolean angry = null;
    private Integer armor = null;
    private Boolean baby = null;
    private Integer catType = null;
    private Boolean chest = null;
    private Byte color = null;
    private JSONObject helmet = null;
    private JSONObject chestplate = null;
    private JSONObject leggins = null;
    private JSONObject boots = null;
    private JSONObject weapon = null;
    private Boolean fire = null;
    private Byte horseType = null;
    private Double rangeDamage = null;
    private Double meeleDamage = null;
    private Boolean powered = null;
    private Integer profession = null;
    private String projectile = null;
    private Boolean saddle = null;
    private Boolean sitting = null;
    private Integer size = null;
    private Boolean tamed = null;
    private Integer variant = null;
    private Boolean wither = null;

    public String getComponentName() {
        return componentName;
    }

    public Double getMeeleDamage() {
        return meeleDamage;
    }

    public void setMeeleDamage(Double meeleDamage) {
        this.meeleDamage = meeleDamage;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getAngry() {
        return angry;
    }

    public void setAngry(Boolean angry) {
        this.angry = angry;
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public Boolean getBaby() {
        return baby;
    }

    public void setBaby(Boolean baby) {
        this.baby = baby;
    }

    public Integer getCatType() {
        return catType;
    }

    public void setCatType(Integer catType) {
        this.catType = catType;
    }

    public Boolean getChest() {
        return chest;
    }

    public void setChest(Boolean chest) {
        this.chest = chest;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }

    public JSONObject getHelmet() {
        return helmet;
    }

    public void setHelmet(JSONObject helmet) {
        this.helmet = helmet;
    }

    public JSONObject getChestplate() {
        return chestplate;
    }

    public void setChestplate(JSONObject chestplate) {
        this.chestplate = chestplate;
    }

    public JSONObject getLeggins() {
        return leggins;
    }

    public void setLeggins(JSONObject leggins) {
        this.leggins = leggins;
    }

    public JSONObject getBoots() {
        return boots;
    }

    public void setBoots(JSONObject boots) {
        this.boots = boots;
    }

    public JSONObject getWeapon() {
        return weapon;
    }

    public void setWeapon(JSONObject weapon) {
        this.weapon = weapon;
    }

    public Boolean getFire() {
        return fire;
    }

    public void setFire(Boolean fire) {
        this.fire = fire;
    }

    public Byte getHorseType() {
        return horseType;
    }

    public void setHorseType(Byte horseType) {
        this.horseType = horseType;
    }

    public Double getRangeDamage() {
        return rangeDamage;
    }

    public void setRangeDamage(Double rangeDamage) {
        this.rangeDamage = rangeDamage;
    }

    public Boolean getPowered() {
        return powered;
    }

    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public Integer getProfession() {
        return profession;
    }

    public void setProfession(Integer profession) {
        this.profession = profession;
    }

    public String getProjectile() {
        return projectile;
    }

    public void setProjectile(String projectile) {
        this.projectile = projectile;
    }

    public Boolean getSaddle() {
        return saddle;
    }

    public void setSaddle(Boolean saddle) {
        this.saddle = saddle;
    }

    public Boolean getSitting() {
        return sitting;
    }

    public void setSitting(Boolean sitting) {
        this.sitting = sitting;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getTamed() {
        return tamed;
    }

    public void setTamed(Boolean tamed) {
        this.tamed = tamed;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    public Boolean getWither() {
        return wither;
    }

    public void setWither(Boolean wither) {
        this.wither = wither;
    }
}
