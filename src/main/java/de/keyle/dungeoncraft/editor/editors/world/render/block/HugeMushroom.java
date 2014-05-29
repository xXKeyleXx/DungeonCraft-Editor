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

public class HugeMushroom extends Block {
    TextureDimensions SKIN;
    TextureDimensions STEM;
    TextureDimensions INSIDE;

    int[][] sides =
            {{0, 0, 0, 0, 0, 0}  // 0
                    , {1, 0, 1, 0, 1, 0}  // 1
                    , {1, 0, 1, 0, 0, 0}  // 2
                    , {1, 0, 1, 0, 0, 1}  // 3
                    , {1, 0, 0, 0, 1, 0}  // 4
                    , {1, 0, 0, 0, 0, 0}  // 5
                    , {1, 0, 0, 0, 0, 1}  // 6
                    , {1, 0, 0, 1, 1, 0}  // 7
                    , {1, 0, 0, 1, 0, 0}  // 8
                    , {1, 0, 0, 1, 0, 1}  // 9
                    , {0, 0, 2, 2, 2, 2}  // 10
                    , null           // 11
                    , null           // 12
                    , null           // 13
                    , {1, 1, 1, 1, 1, 1}  // 14
                    , {2, 2, 2, 2, 2, 2}};// 15

    public HugeMushroom(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("SKIN")) {
            JSONArray sideArray = (JSONArray) textures.get("SKIN");
            SKIN = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " SKIN texture.");
        }
        if (textures.containsKey("STEM")) {
            JSONArray sideArray = (JSONArray) textures.get("STEM");
            STEM = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " STEM texture.");
        }
        if (textures.containsKey("INSIDE")) {
            JSONArray sideArray = (JSONArray) textures.get("INSIDE");
            INSIDE = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " TOP texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "HUGE_MUSHROOM";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        if (sides[blockData] == null) {
            blockData = 0;
        }

        if (x > 0 && !chunk.getBlockType(x - 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(getTexture(sides[blockData][4]), x, y, z, x, y + 1, z + 1);
        }
        if (x < chunk.getMaxWidth() && !chunk.getBlockType(x + 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(getTexture(sides[blockData][5]), x + 1, y, z, x + 1, y + 1, z + 1);
        }
        if (y > 0 && !chunk.getBlockType(x, y - 1, z).isSolid()) {
            Renderer.renderHorizontal(getTexture(sides[blockData][1]), x, z, x + 1, z + 1, y, false);
        }
        if (y < chunk.getMaxHeight() && !chunk.getBlockType(x, y + 1, z).isSolid()) {
            Renderer.renderHorizontal(getTexture(sides[blockData][0]), x, z, x + 1, z + 1, y + 1, false);
        }
        if (z > 0 && !chunk.getBlockType(x, y, z - 1).isSolid()) {
            Renderer.renderNonstandardVertical(getTexture(sides[blockData][2]), x, y, z, x + 1, y + 1, z);
        }
        if (z < chunk.getMaxLength() && !chunk.getBlockType(x, y, z + 1).isSolid()) {
            Renderer.renderNonstandardVertical(getTexture(sides[blockData][3]), x, y, z + 1, x + 1, y + 1, z + 1);
        }
    }

    private TextureDimensions getTexture(int tex) {
        switch (tex) {
            case 1:
                return SKIN;
            case 2:
                return STEM;
            default:
                return INSIDE;
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}