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

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Signpost extends Block {
    TextureDimensions sign;

    public Signpost(short id) throws BlockTypeLoadException {
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
                    if (textureObject.containsKey("FRONT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("FRONT");
                        sign = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y * 0.75f, false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "SIGNPOST";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float signBottom = 0.5f;
        float signHeight = .5f;
        float postRadius = .05f;
        float face_spacing = 3; // in degrees

        // First a signpost
        Renderer.renderVertical(sign, x - postRadius + 0.5f, z - postRadius + 0.5f, x + postRadius + 0.5f, z - postRadius + 0.5f, y, signBottom);
        Renderer.renderVertical(sign, x - postRadius + 0.5f, z + postRadius + 0.5f, x + postRadius + 0.5f, z + postRadius + 0.5f, y, signBottom);
        Renderer.renderVertical(sign, x + postRadius + 0.5f, z - postRadius + 0.5f, x + postRadius + 0.5f, z + postRadius + 0.5f, y, signBottom);
        Renderer.renderVertical(sign, x - postRadius + 0.5f, z + postRadius + 0.5f, x - postRadius + 0.5f, z - postRadius + 0.5f, y, signBottom);

        // Signpost top
        Renderer.renderHorizontal(sign, x - postRadius + 0.5f, z - postRadius + 0.5f, x + postRadius + 0.5f, z + postRadius + 0.5f, y, false);

        // Now we continue to draw the sign itself.
        blockData &= 0xF;
        // data: 0 is South, increasing numbers add 22.5 degrees (so 4 is West, 8 East, etc)
        // Because we're not actually drawing the message (yet), as far as we're concerned
        // West is the same as East, etc.
        float angle = (blockData % 8) * 22.5f;
        float radius = 0.5f;

        angle -= face_spacing;
        // First x/z
        float x1a = x + radius * (float) Math.cos(Math.toRadians(angle));
        float z1a = z + radius * (float) Math.sin(Math.toRadians(angle));
        angle += face_spacing * 2;
        float x1b = x + radius * (float) Math.cos(Math.toRadians(angle));
        float z1b = z + radius * (float) Math.sin(Math.toRadians(angle));

        // Now the other side
        angle += 180;
        float x2a = x + radius * (float) Math.cos(Math.toRadians(angle));
        float z2a = z + radius * (float) Math.sin(Math.toRadians(angle));
        angle -= face_spacing * 2;
        float x2b = x + radius * (float) Math.cos(Math.toRadians(angle));
        float z2b = z + radius * (float) Math.sin(Math.toRadians(angle));

        // Faces
        Renderer.renderVertical(sign, x1a + 0.5f, z1a + 0.5f, x2a + 0.5f, z2a + 0.5f, y + signBottom, signHeight);
        Renderer.renderVertical(sign, x1b + 0.5f, z1b + 0.5f, x2b + 0.5f, z2b + 0.5f, y + signBottom, signHeight);

        // Sides
        Renderer.renderVertical(sign, x1a + 0.5f, z1a + 0.5f, x1b + 0.5f, z1b + 0.5f, y + signBottom, signHeight);
        Renderer.renderVertical(sign, x2a + 0.5f, z2a + 0.5f, x2b + 0.5f, z2b + 0.5f, y + signBottom, signHeight);

        // Top/Bottom
        Renderer.renderHorizontalAskew(sign, x1a + 0.5f, z1a + 0.5f, x1b + 0.5f, z1b + 0.5f, x2a + 0.5f, z2a + 0.5f, x2b + 0.5f, z2b + 0.5f, y + signBottom);
        Renderer.renderHorizontalAskew(sign, x1a + 0.5f, z1a + 0.5f, x1b + 0.5f, z1b + 0.5f, x2a + 0.5f, z2a + 0.5f, x2b + 0.5f, z2b + 0.5f, y + signBottom + signHeight);
    }
}
