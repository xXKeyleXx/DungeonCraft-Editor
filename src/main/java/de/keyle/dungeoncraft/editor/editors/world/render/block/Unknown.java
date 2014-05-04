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
        Renderer.renderNonstandardVertical(dimesions, x, y, z, x, y + 1, z + 1);
        Renderer.renderNonstandardVertical(dimesions, x + 1, y, z, x + 1, y + 1, z + 1);
        Renderer.renderHorizontal(dimesions, x, z, x + 1, z + 1, y, false);
        Renderer.renderHorizontal(dimesions, x, z, x + 1, z + 1, y + 1, false);
        Renderer.renderNonstandardVertical(dimesions, x, y, z, x + 1, y + 1, z);
        Renderer.renderNonstandardVertical(dimesions, x, y, z + 1, x + 1, y + 1, z + 1);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}