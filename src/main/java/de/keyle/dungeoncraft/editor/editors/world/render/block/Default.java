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

public class Default extends Block {
    protected final TextureDimensions[][] dimensions = new TextureDimensions[16][];

    public Default(short id) {
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
                    TextureDimensions[] dataDimensions = new TextureDimensions[BLOCK_FACE.values().length];
                    JSONObject textureObject = (JSONObject) textures.get(key);
                    if (textureObject.containsKey("BOTTOM")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("BOTTOM");
                        dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("TOP");
                        dataDimensions[BLOCK_FACE.TOP.ordinal()] = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
                    }
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("SIDE");
                        dataDimensions[BLOCK_FACE.SIDES.ordinal()] = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
                    }
                    if (textureObject.containsKey("BACK")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("BACK");
                        dataDimensions[BLOCK_FACE.BACK.ordinal()] = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
                    }
                    if (textureObject.containsKey("FRONT")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("FRONT");
                        dataDimensions[BLOCK_FACE.FRONT.ordinal()] = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false);
                    }

                    while (Util.arrayContains(dataDimensions, null)) {
                        if (dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] == null && dataDimensions[BLOCK_FACE.TOP.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] = dataDimensions[BLOCK_FACE.TOP.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.TOP.ordinal()] == null && dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.TOP.ordinal()] = dataDimensions[BLOCK_FACE.BOTTOM.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.BACK.ordinal()] == null && dataDimensions[BLOCK_FACE.FRONT.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.BACK.ordinal()] = dataDimensions[BLOCK_FACE.FRONT.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.FRONT.ordinal()] == null && dataDimensions[BLOCK_FACE.BACK.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.FRONT.ordinal()] = dataDimensions[BLOCK_FACE.BACK.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.FRONT.ordinal()] == null && dataDimensions[BLOCK_FACE.SIDES.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.FRONT.ordinal()] = dataDimensions[BLOCK_FACE.SIDES.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.BACK.ordinal()] == null && dataDimensions[BLOCK_FACE.SIDES.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.BACK.ordinal()] = dataDimensions[BLOCK_FACE.SIDES.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.SIDES.ordinal()] == null && dataDimensions[BLOCK_FACE.FRONT.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.SIDES.ordinal()] = dataDimensions[BLOCK_FACE.FRONT.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.SIDES.ordinal()] == null && dataDimensions[BLOCK_FACE.BACK.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.SIDES.ordinal()] = dataDimensions[BLOCK_FACE.BACK.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] == null && dataDimensions[BLOCK_FACE.SIDES.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.BOTTOM.ordinal()] = dataDimensions[BLOCK_FACE.SIDES.ordinal()];
                        }
                        if (dataDimensions[BLOCK_FACE.TOP.ordinal()] == null && dataDimensions[BLOCK_FACE.SIDES.ordinal()] != null) {
                            dataDimensions[BLOCK_FACE.TOP.ordinal()] = dataDimensions[BLOCK_FACE.SIDES.ordinal()];
                        }
                    }

                    dimensions[data] = dataDimensions;
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "DEFAULT";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        if (dimensions[blockData] == null) {
            blockData = 0;
        }
        TextureDimensions[] dataDimensions = dimensions[blockData];

        if (x > 0 && !chunk.getBlockType(x - 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], x, y, z, x, y + 1, z + 1);
        }
        if (x < chunk.getMaxWidth() && !chunk.getBlockType(x + 1, y, z).isSolid()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], x + 1, y, z, x + 1, y + 1, z + 1);
        }
        if (y > 0 && !chunk.getBlockType(x, y - 1, z).isSolid()) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.BOTTOM.ordinal()], x, z, x + 1, z + 1, y, false);
        }
        if (y < chunk.getMaxHeight() && !chunk.getBlockType(x, y + 1, z).isSolid()) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.TOP.ordinal()], x, z, x + 1, z + 1, y + 1, false);
        }
        if (z > 0 && !chunk.getBlockType(x, y, z - 1).isSolid()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.FRONT.ordinal()], x, y, z, x + 1, y + 1, z);
        }
        if (z < chunk.getMaxLength() && !chunk.getBlockType(x, y, z + 1).isSolid()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.BACK.ordinal()], x, y, z + 1, x + 1, y + 1, z + 1);
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}