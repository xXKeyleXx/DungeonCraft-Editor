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
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

public class Unknown extends Block {
    TextureDimensions dimesions = new TextureDimensions(-1, (byte) 0, 5, 15);

    public Unknown() {
        super((short) -1);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {

    }

    @Override
    public String getBlockType() {
        return "UNKNOWN";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        Renderer.renderNonstandardVertical(dimesions, 0, 0, 0, 0, 1, 1);
        Renderer.renderNonstandardVertical(dimesions, 1, 0, 0, 1, 1, 1);
        Renderer.renderHorizontal(dimesions, 0, 0, 1, 1, 0, false);
        Renderer.renderHorizontal(dimesions, 0, 0, 1, 1, 1, false);
        Renderer.renderNonstandardVertical(dimesions, 0, 0, 0, 1, 1, 0);
        Renderer.renderNonstandardVertical(dimesions, 0, 0, 1, 1, 1, 1);

        GL11.glPopMatrix();
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}