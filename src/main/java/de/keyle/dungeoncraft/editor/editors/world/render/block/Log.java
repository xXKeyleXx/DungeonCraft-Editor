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
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Log extends Block {
    TextureDimensions[] top = new TextureDimensions[4];
    TextureDimensions[] side = new TextureDimensions[4];

    public Log(short id) throws BlockTypeLoadException {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (!textures.containsKey("0")) {
            throw new BlockTypeLoadException(id + " is missing \"0\" data.");
        }
        for (Object key : textures.keySet()) {
            if (textures.get(key) instanceof JSONObject) {
                if (Util.isByte(key.toString())) {
                    byte data = Byte.parseByte(key.toString());
                    JSONObject textureObject = (JSONObject) textures.get(key);
                    if (textureObject.containsKey("TOP")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("TOP");
                        top[data] = new TextureDimensions(id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " is missing TOP texture.");
                    }
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("SIDE");
                        side[data] = new TextureDimensions(id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " is missing SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "LOG";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte woodType = (byte) (blockData % 4);

        if (top[woodType] == null) {
            woodType = 0;
        }

        TextureDimensions top = this.top[woodType];
        TextureDimensions side = this.side[woodType];

        // ToDo rotation

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        if (x > 0 && !chunk.getBlockType(x - 1, y, z).isSolid() || x == 0) {
            Renderer.renderNonstandardVertical(side, 0, 0, 0, 0, 1, 1);
        }
        if (x < chunk.getMaxWidth() && !chunk.getBlockType(x + 1, y, z).isSolid() || x == chunk.getMaxWidth()) {
            Renderer.renderNonstandardVertical(side, 1, 0, 0, 1, 1, 1);
        }
        if (y > 0 && !chunk.getBlockType(x, y - 1, z).isSolid() || y == 0) {
            Renderer.renderHorizontal(top, 0, 0, 1, 1, 0, false);
        }
        if (y < chunk.getMaxHeight() && !chunk.getBlockType(x, y + 1, z).isSolid() || y == chunk.getMaxHeight()) {
            Renderer.renderHorizontal(top, 0, 0, 1, 1, 1, false);
        }
        if (z > 0 && !chunk.getBlockType(x, y, z - 1).isSolid() || z == 0) {
            Renderer.renderNonstandardVertical(side, 0, 0, 0, 1, 1, 0);
        }
        if (z < chunk.getMaxLength() && !chunk.getBlockType(x, y, z + 1).isSolid() || z == chunk.getMaxLength()) {
            Renderer.renderNonstandardVertical(side, 0, 0, 1, 1, 1, 1);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}