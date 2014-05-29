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

public class DecorationCross extends Block {
    protected final TextureDimensions[] dimensions = new TextureDimensions[16];

    public DecorationCross(short id) throws BlockTypeLoadException {
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
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("SIDE");
                        dimensions[data] = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "DECORATION_CROSS";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        if (dimensions[blockData] == null) {
            blockData = 0;
        }

        Renderer.renderNonstandardVertical(dimensions[blockData], 0, 0, 0, 1, 1, 1);
        Renderer.renderNonstandardVertical(dimensions[blockData], 1, 0, 0, 0, 1, 1);

        GL11.glPopMatrix();
    }
}