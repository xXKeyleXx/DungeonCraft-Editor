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

import java.util.HashMap;
import java.util.Map;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Default extends Block {
    protected final Map<Byte, Map<BLOCK_FACE, TextureDimensions>> dimensions = new HashMap<Byte, Map<BLOCK_FACE, TextureDimensions>>();

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
                    Map<BLOCK_FACE, TextureDimensions> dataDimensions = new HashMap<BLOCK_FACE, TextureDimensions>();
                    JSONObject textureObject = (JSONObject) textures.get(key);
                    if (textureObject.containsKey("BOTTOM")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("BOTTOM");
                        dataDimensions.put(BLOCK_FACE.BOTTOM, new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false));
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("TOP");
                        dataDimensions.put(BLOCK_FACE.TOP, new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false));
                    }
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("SIDE");
                        dataDimensions.put(BLOCK_FACE.SIDES, new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false));
                    }
                    if (textureObject.containsKey("BACK")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("BACK");
                        dataDimensions.put(BLOCK_FACE.BACK, new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false));
                    }
                    if (textureObject.containsKey("FRONT")) {
                        JSONArray feedEnd = (JSONArray) textureObject.get("FRONT");
                        dataDimensions.put(BLOCK_FACE.FRONT, new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y, false));
                    }
                    while (dataDimensions.size() < 5) {
                        if (!dataDimensions.containsKey(BLOCK_FACE.BOTTOM) && dataDimensions.containsKey(BLOCK_FACE.TOP)) {
                            dataDimensions.put(BLOCK_FACE.BOTTOM, dataDimensions.get(BLOCK_FACE.TOP));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.TOP) && dataDimensions.containsKey(BLOCK_FACE.BOTTOM)) {
                            dataDimensions.put(BLOCK_FACE.TOP, dataDimensions.get(BLOCK_FACE.BOTTOM));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.BACK) && dataDimensions.containsKey(BLOCK_FACE.FRONT)) {
                            dataDimensions.put(BLOCK_FACE.BACK, dataDimensions.get(BLOCK_FACE.FRONT));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.FRONT) && dataDimensions.containsKey(BLOCK_FACE.BACK)) {
                            dataDimensions.put(BLOCK_FACE.FRONT, dataDimensions.get(BLOCK_FACE.BACK));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.FRONT) && dataDimensions.containsKey(BLOCK_FACE.SIDES)) {
                            dataDimensions.put(BLOCK_FACE.FRONT, dataDimensions.get(BLOCK_FACE.SIDES));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.BACK) && dataDimensions.containsKey(BLOCK_FACE.SIDES)) {
                            dataDimensions.put(BLOCK_FACE.BACK, dataDimensions.get(BLOCK_FACE.SIDES));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.SIDES) && dataDimensions.containsKey(BLOCK_FACE.FRONT)) {
                            dataDimensions.put(BLOCK_FACE.SIDES, dataDimensions.get(BLOCK_FACE.FRONT));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.SIDES) && dataDimensions.containsKey(BLOCK_FACE.BACK)) {
                            dataDimensions.put(BLOCK_FACE.SIDES, dataDimensions.get(BLOCK_FACE.BACK));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.BOTTOM) && dataDimensions.containsKey(BLOCK_FACE.SIDES)) {
                            dataDimensions.put(BLOCK_FACE.BOTTOM, dataDimensions.get(BLOCK_FACE.SIDES));
                        }
                        if (!dataDimensions.containsKey(BLOCK_FACE.TOP) && dataDimensions.containsKey(BLOCK_FACE.SIDES)) {
                            dataDimensions.put(BLOCK_FACE.TOP, dataDimensions.get(BLOCK_FACE.SIDES));
                        }
                    }

                    dimensions.put(data, dataDimensions);
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
        if (!dimensions.containsKey(blockData)) {
            blockData = 0;
        }
        Map<BLOCK_FACE, TextureDimensions> dataDimensions = dimensions.get(blockData);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        if (x > 0 && !chunk.getBlockType(x - 1, y, z).isSolid() || x == 0) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.SIDES), 0, 0, 0, 0, 1, 1);
        }
        if (x < chunk.getMaxWidth() && !chunk.getBlockType(x + 1, y, z).isSolid() || x == chunk.getMaxWidth()) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.SIDES), 1, 0, 0, 1, 1, 1);
        }
        if (y > 0 && !chunk.getBlockType(x, y - 1, z).isSolid() || y == 0) {
            Renderer.renderHorizontal(dataDimensions.get(BLOCK_FACE.BOTTOM), 0, 0, 1, 1, 0, false);
        }
        if (y < chunk.getMaxHeight() && !chunk.getBlockType(x, y + 1, z).isSolid() || y == chunk.getMaxHeight()) {
            Renderer.renderHorizontal(dataDimensions.get(BLOCK_FACE.TOP), 0, 0, 1, 1, 1, false);
        }
        if (z > 0 && !chunk.getBlockType(x, y, z - 1).isSolid() || z == 0) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.FRONT), 0, 0, 0, 1, 1, 0);
        }
        if (z < chunk.getMaxLength() && !chunk.getBlockType(x, y, z + 1).isSolid() || z == chunk.getMaxLength()) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.BACK), 0, 0, 1, 1, 1, 1);
        }

        GL11.glPopMatrix();
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}