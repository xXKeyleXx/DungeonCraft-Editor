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

public class Bed extends Block {
    protected TextureDimensions FEED_END;
    protected TextureDimensions FEED_SIDE;
    protected TextureDimensions FEED_TOP;

    protected TextureDimensions HEAD_END;
    protected TextureDimensions HEAD_SIDE;
    protected TextureDimensions HEAD_TOP;


    public Bed(short id) throws BlockTypeLoadException {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("FEED_END")) {
            JSONArray feedEndArray = (JSONArray) textures.get("FEED_END");
            FEED_END = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedEndArray.get(0).toString()), Integer.parseInt(feedEndArray.get(1).toString()), TEX16, TEX_Y * 0.5625f, false);
        } else {
            throw new BlockTypeLoadException(id + " is missing FEED_END texture.");
        }
        if (textures.containsKey("FEED_SIDE")) {
            JSONArray feedSideArray = (JSONArray) textures.get("FEED_SIDE");
            FEED_SIDE = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedSideArray.get(0).toString()), Integer.parseInt(feedSideArray.get(1).toString()), TEX16, TEX_Y * 0.5625f, false);
        } else {
            throw new BlockTypeLoadException(id + " is missing FEED_SIDE texture.");
        }
        if (textures.containsKey("FEED_TOP")) {
            JSONArray feedTopArray = (JSONArray) textures.get("FEED_TOP");
            FEED_TOP = new TextureDimensions(26, (byte) 0, Integer.parseInt(feedTopArray.get(0).toString()), Integer.parseInt(feedTopArray.get(1).toString()));
        } else {
            throw new BlockTypeLoadException(id + " is missing FEED_TOP texture.");
        }
        if (textures.containsKey("HEAD_END")) {
            JSONArray headEndArray = (JSONArray) textures.get("HEAD_END");
            HEAD_END = new TextureDimensions(26, (byte) 0, Integer.parseInt(headEndArray.get(0).toString()), Integer.parseInt(headEndArray.get(1).toString()), TEX16, TEX_Y * 0.5625f, false);
        } else {
            throw new BlockTypeLoadException(id + " is missing HEAD_END texture.");
        }
        if (textures.containsKey("HEAD_SIDE")) {
            JSONArray headSideArray = (JSONArray) textures.get("HEAD_SIDE");
            HEAD_SIDE = new TextureDimensions(26, (byte) 0, Integer.parseInt(headSideArray.get(0).toString()), Integer.parseInt(headSideArray.get(1).toString()), TEX16, TEX_Y * 0.5625f, false);
        } else {
            throw new BlockTypeLoadException(id + " is missing HEAD_SIDE texture.");
        }
        if (textures.containsKey("HEAD_TOP")) {
            JSONArray headTopArray = (JSONArray) textures.get("HEAD_TOP");
            HEAD_TOP = new TextureDimensions(26, (byte) 0, Integer.parseInt(headTopArray.get(0).toString()), Integer.parseInt(headTopArray.get(1).toString()));
        } else {
            throw new BlockTypeLoadException(id + " is missing HEAD_TOP texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "BED";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        boolean head = true;

        blockData &= 0xF;
        if ((blockData & 0x8) == 0) {
            head = false;
        }
        blockData &= 0x3;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        int xShift = 0;
        int zShift = 0;

        // Render pointing South by default
        if (blockData == 2) {
            // Pointing North
            GL11.glRotatef(180f, 0f, 1f, 0f);
            xShift = -1;
            zShift = -1;
        } else if (blockData == 1) {
            // Pointing West
            GL11.glRotatef(-90f, 0f, 1f, 0f);
            zShift = -1;
        } else if (blockData == 3) {
            // Pointing East
            GL11.glRotatef(90f, 0f, 1f, 0f);
            xShift = -1;
        }

        if (head) {
            // Top face
            Renderer.renderHorizontal(HEAD_TOP, xShift, zShift, xShift + 1, zShift + 1, 0.5625f, false);
            // Side faces
            Renderer.renderNonstandardVertical(HEAD_SIDE, xShift, 0, zShift, xShift, 0.5625f, zShift + 1);
            Renderer.renderNonstandardVertical(HEAD_SIDE, xShift + 1, 0, zShift, xShift + 1, 0.5625f, zShift + 1);
            Renderer.renderNonstandardVertical(HEAD_END, xShift, 0, zShift + 1, xShift + 1, 0.5625f, zShift + 1);
        } else {
            // Top face
            Renderer.renderHorizontal(FEED_TOP, xShift, zShift, xShift + 1, zShift + 1, 0.5625f, false);
            // Side faces
            Renderer.renderNonstandardVertical(FEED_SIDE, xShift, 0, zShift, xShift, 0.5625f, zShift + 1);
            Renderer.renderNonstandardVertical(FEED_SIDE, xShift + 1, 0, zShift, xShift + 1, 0.5625f, zShift + 1);
            Renderer.renderNonstandardVertical(FEED_END, xShift, 0, zShift, xShift + 1, 0.5625f, zShift);
        }

        GL11.glPopMatrix();
    }
}