/*
 * This file is part of DungeonCraft-Editor
 *
 * Copyright (C) 2011-2014 Keyle
 * DungeonCraft-Editor is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class EnchantmentTable extends Block {
    protected TextureDimensions top;
    protected TextureDimensions side;
    protected TextureDimensions bottom;

    public EnchantmentTable(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (!textures.containsKey("0")) {
            throw new BlockTypeLoadException(id + " is missing \"0\" data.");
        }
        if (textures.get("0") instanceof JSONObject) {
            JSONObject textureObject = (JSONObject) textures.get("0");
            if (textureObject.containsKey("BOTTOM")) {
                JSONArray feedEnd = (JSONArray) textureObject.get("BOTTOM");
                bottom = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
            }
            if (textureObject.containsKey("TOP")) {
                JSONArray feedEnd = (JSONArray) textureObject.get("TOP");
                top = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
            }
            if (textureObject.containsKey("SIDE")) {
                JSONArray feedEnd = (JSONArray) textureObject.get("SIDE");
                side = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y * 0.75f, false);
            }
        }
    }

    @Override
    public String getBlockType() {
        return "ENCHANTMENT_TABLE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        if (x > 0 && !chunk.getBlockType(x - 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(side, x, y, z, x, y + 0.75f, z + 1);
        }
        if (x < chunk.getMaxWidth() && !chunk.getBlockType(x + 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(side, x + 1, y, z, x + 1, y + 0.75f, z + 1);
        }
        if (y > 0 && !chunk.getBlockType(x, y - 1, z).isSolid()) {
            Renderer.renderHorizontal(bottom, x, z, x + 1, z + 1, y, false);
        }
        if (y < chunk.getMaxHeight() && !chunk.getBlockType(x, y + 1, z).isSolid()) {
            Renderer.renderHorizontal(top, x, z, x + 1, z + 1, y + 0.75f, false);
        }
        if (z > 0 && !chunk.getBlockType(x, y, z - 1).isSolid()) {
            Renderer.renderNonstandardVertical(side, x, y, z, x + 1, y + 0.75f, z);
        }
        if (z < chunk.getMaxLength() && !chunk.getBlockType(x, y, z + 1).isSolid()) {
            Renderer.renderNonstandardVertical(side, x, y, z + 1, x + 1, y + 0.75f, z + 1);
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}