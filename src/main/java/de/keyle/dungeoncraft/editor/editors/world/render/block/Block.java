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
import org.json.simple.JSONObject;

public abstract class Block {
    protected short id;

    public enum BLOCK_FACE {
        FRONT,
        BACK,
        WEST,
        EAST,
        TOP,
        BOTTOM,
        SIDES
    }

    public Block(short id) {
        this.id = id;
    }

    public abstract void readTextures(JSONObject textures) throws BlockTypeLoadException;

    public abstract String getBlockType();

    public boolean isSolid() {
        return false;
    }

    public short getBlockId() {
        return id;
    }

    public abstract void render(byte blockData, int x, int y, int z, ChunkSchematic chunk);
}