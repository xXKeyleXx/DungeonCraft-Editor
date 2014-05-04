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

public class Water extends SemiSolid {
    public Water(short id) throws BlockTypeLoadException {
        super(id);
    }

    @Override
    public String getBlockType() {
        return "WATER";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        if (dimensions[blockData] == null) {
            blockData = 0;
        }
        TextureDimensions[] dataDimensions = dimensions[blockData];

        float height;
        if (chunk.getBlockType(x, y + 1, z).getBlockId() == this.id) {
            height = 1;
        } else {
            height = 0.9f;
        }

        if ((x > 0 && shouldRender(x - 1, y, z, chunk)) || x == 0) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], x, y, z, x, y + height, z + 1);
        }
        if ((x < chunk.getMaxWidth() && shouldRender(x + 1, y, z, chunk)) || x == chunk.getMaxWidth()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], x + 1, y, z, x + 1, y + height, z + 1);
        }
        if ((y > 0 && shouldRender(x, y - 1, z, chunk)) || y == 0) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.BOTTOM.ordinal()], x, z, x + 1, z + 1, y, false);
        }
        if ((y < chunk.getMaxHeight() && chunk.getBlockType(x, y + 1, z).getBlockId() != this.id) || y == chunk.getMaxHeight()) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.TOP.ordinal()], x, z, x + 1, z + 1, y + height, false);
        }
        if ((z > 0 && shouldRender(x, y, z - 1, chunk)) || z == 0) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.FRONT.ordinal()], x, y, z, x + 1, y + height, z);
        }
        if ((z < chunk.getMaxLength() && shouldRender(x, y, z + 1, chunk)) || z == chunk.getMaxLength()) {
            Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.BACK.ordinal()], x, y, z + 1, x + 1, y + height, z + 1);
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

    @Override
    public boolean isSolid() {
        return false;
    }
}