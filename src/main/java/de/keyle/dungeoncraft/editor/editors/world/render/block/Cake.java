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
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Cake extends Block {
    TextureDimensions[] SIDE = new TextureDimensions[7];
    TextureDimensions[] TOP = new TextureDimensions[7];
    TextureDimensions[] BOTTOM = new TextureDimensions[7];
    TextureDimensions INNER;

    static float BORDER = 0.0625f;
    static float FULL_WIDTH = 0.875f;
    static float SLICE_WIDTH = FULL_WIDTH / 7f;
    static float HEIGHT = 0.5f;

    public Cake(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("SIDE")) {
            JSONArray feedEndArray = (JSONArray) textures.get("SIDE");
            int x = Integer.parseInt(feedEndArray.get(0).toString());
            int y = Integer.parseInt(feedEndArray.get(1).toString());

            SIDE[0] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 0), TEX_Y * 0.5f);
            SIDE[1] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 1), TEX_Y * 0.5f);
            SIDE[2] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 2), TEX_Y * 0.5f);
            SIDE[3] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 3), TEX_Y * 0.5f);
            SIDE[4] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 4), TEX_Y * 0.5f);
            SIDE[5] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 5), TEX_Y * 0.5f);
            SIDE[6] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * 0.875f - TEX16 * (SLICE_WIDTH * 6), TEX_Y * 0.5f);
        } else {
            throw new BlockTypeLoadException(id + " is missing SIDE texture.");
        }
        if (textures.containsKey("INNER")) {
            JSONArray feedEndArray = (JSONArray) textures.get("INNER");
            int x = Integer.parseInt(feedEndArray.get(0).toString());
            int y = Integer.parseInt(feedEndArray.get(1).toString());

            INNER = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * 0.5f, TEX16 * FULL_WIDTH, TEX_Y * 0.5f);
        } else {
            throw new BlockTypeLoadException(id + " is missing INNER texture.");
        }
        if (textures.containsKey("TOP")) {
            JSONArray feedEndArray = (JSONArray) textures.get("TOP");
            int x = Integer.parseInt(feedEndArray.get(0).toString());
            int y = Integer.parseInt(feedEndArray.get(1).toString());

            TOP[0] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 0), TEX_Y * FULL_WIDTH);
            TOP[1] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 1), TEX_Y * FULL_WIDTH);
            TOP[2] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 2), TEX_Y * FULL_WIDTH);
            TOP[3] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 3), TEX_Y * FULL_WIDTH);
            TOP[4] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 4), TEX_Y * FULL_WIDTH);
            TOP[5] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 5), TEX_Y * FULL_WIDTH);
            TOP[6] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 6), TEX_Y * FULL_WIDTH);
        } else {
            throw new BlockTypeLoadException(id + " is missing TOP texture.");
        }
        if (textures.containsKey("BOTTOM")) {
            JSONArray feedEndArray = (JSONArray) textures.get("BOTTOM");
            int x = Integer.parseInt(feedEndArray.get(0).toString());
            int y = Integer.parseInt(feedEndArray.get(1).toString());

            BOTTOM[0] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 0), TEX_Y * FULL_WIDTH);
            BOTTOM[1] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 1), TEX_Y * FULL_WIDTH);
            BOTTOM[2] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 2), TEX_Y * FULL_WIDTH);
            BOTTOM[3] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 3), TEX_Y * FULL_WIDTH);
            BOTTOM[4] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 4), TEX_Y * FULL_WIDTH);
            BOTTOM[5] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 5), TEX_Y * FULL_WIDTH);
            BOTTOM[6] = new TextureDimensions(26, (byte) 0, x * TEX16 + TEX16 * BORDER, y * TEX_Y + TEX_Y * BORDER, TEX16 * FULL_WIDTH - TEX16 * (SLICE_WIDTH * 6), TEX_Y * FULL_WIDTH);
        } else {
            throw new BlockTypeLoadException(id + " is missing BOTTOM texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "CAKE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        int remainingSlices = 7 - blockData;

        Renderer.renderVertical(SIDE[0], BORDER, BORDER, BORDER + FULL_WIDTH, BORDER, 0, HEIGHT);
        Renderer.renderVertical(SIDE[blockData], BORDER, BORDER, BORDER, BORDER + remainingSlices * SLICE_WIDTH, 0, HEIGHT);
        Renderer.renderVertical(SIDE[blockData], BORDER + FULL_WIDTH, BORDER, BORDER + FULL_WIDTH, BORDER + remainingSlices * SLICE_WIDTH, 0, HEIGHT);
        if (blockData == 0) {
            Renderer.renderVertical(SIDE[0], BORDER, BORDER + remainingSlices * SLICE_WIDTH, BORDER + FULL_WIDTH, BORDER + remainingSlices * SLICE_WIDTH, 0, HEIGHT);
        } else {
            Renderer.renderVertical(INNER, BORDER, BORDER + remainingSlices * SLICE_WIDTH, BORDER + FULL_WIDTH, BORDER + remainingSlices * SLICE_WIDTH, 0, HEIGHT);
        }

        Renderer.renderNonstandardHorizontal(TOP[blockData], BORDER, BORDER, BORDER + FULL_WIDTH, BORDER + remainingSlices * SLICE_WIDTH, HEIGHT);
        Renderer.renderNonstandardHorizontal(BOTTOM[blockData], BORDER, BORDER, BORDER + FULL_WIDTH, BORDER + remainingSlices * SLICE_WIDTH, 0);

        GL11.glPopMatrix();
    }
}