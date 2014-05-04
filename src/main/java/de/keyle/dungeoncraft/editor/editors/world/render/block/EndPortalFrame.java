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

public class EndPortalFrame extends Block {
    protected TextureDimensions top;
    protected TextureDimensions side;
    protected TextureDimensions bottom;
    protected TextureDimensions eyeTop;
    protected TextureDimensions eyeSide;

    public EndPortalFrame(short id) throws BlockTypeLoadException {
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
                side = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(feedEnd.get(0).toString()), Integer.parseInt(feedEnd.get(1).toString()), TEX16, TEX_Y * 0.8125f, false);
            }
            if (textureObject.containsKey("EYE")) {
                JSONArray eyeTopArray = (JSONArray) textureObject.get("EYE");
                eyeTop = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(eyeTopArray.get(0).toString()) * TEX16 + TEX16 * 0.25f, Integer.parseInt(eyeTopArray.get(1).toString()) * TEX_Y + TEX_Y * 0.25f, TEX16 / 2, TEX_Y / 2);
                eyeSide = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(eyeTopArray.get(0).toString()) * TEX16 + TEX16 * 0.25f, Integer.parseInt(eyeTopArray.get(1).toString()) * TEX_Y, TEX16 / 2, TEX_Y / 4);
            }
        }
    }

    @Override
    public String getBlockType() {
        return "END_PORTAL_FRAME";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float side_height = .8125f;
        float eye_side = .25f;
        float eye_height = .1875f;
        boolean eye = ((blockData & 0x4) == 0x4);

        if (x > 0 && shouldRender(x - 1, y, z, chunk)) {
            Renderer.renderNonstandardVertical(side, x, y, z, x, y + side_height, z + 1);
        }
        if (x < chunk.getMaxWidth() && shouldRender(x + 1, y, z, chunk)) {
            Renderer.renderNonstandardVertical(side, x + 1, y, z, x + 1, y + side_height, z + 1);
        }
        if (y > 0 && shouldRender(x, y - 1, z, chunk)) {
            Renderer.renderHorizontal(bottom, x, z, x + 1, z + 1, y, false);
        }
        Renderer.renderHorizontal(top, x, z, x + 1, z + 1, y + side_height, false);
        if (z > 0 && shouldRender(x, y, z - 1, chunk)) {
            Renderer.renderNonstandardVertical(side, x, y, z, x + 1, y + side_height, z);
        }
        if (z < chunk.getMaxLength() && shouldRender(x, y, z + 1, chunk)) {
            Renderer.renderNonstandardVertical(side, x, y, z + 1, x + 1, y + side_height, z + 1);
        }

        // Now the Eye of Ender, if we should
        if (eye) {
            Renderer.renderNonstandardVertical(eyeSide, x + eye_side, y + side_height, z + eye_side, x + 1 - eye_side, y + side_height + eye_height, z + eye_side);
            Renderer.renderNonstandardVertical(eyeSide, x + eye_side, y + side_height, z + 1 - eye_side, x + 1 - eye_side, y + side_height + eye_height, z + 1 - eye_side);
            Renderer.renderNonstandardVertical(eyeSide, x + eye_side, y + side_height, z + eye_side, x + eye_side, y + side_height + eye_height, z + 1 - eye_side);
            Renderer.renderNonstandardVertical(eyeSide, x + 1 - eye_side, y + side_height, z + eye_side, x + 1 - eye_side, y + side_height + eye_height, z + 1 - eye_side);
            Renderer.renderNonstandardHorizontal(eyeTop, x + eye_side, z + eye_side, x + 1 - eye_side, z + 1 - eye_side, y + side_height + eye_height);
        }
    }

    private boolean shouldRender(int x, int y, int z, ChunkSchematic chunk) {
        if (chunk.getBlockId(x, y, z) != this.id) {
            if (chunk.getBlockType(x, y, z).isSolid()) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}