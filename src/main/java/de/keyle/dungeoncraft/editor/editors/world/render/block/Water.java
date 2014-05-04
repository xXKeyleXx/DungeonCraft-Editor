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
import org.lwjgl.opengl.GL11;

import java.util.Map;

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
        if (!dimensions.containsKey(blockData)) {
            blockData = 0;
        }
        Map<BLOCK_FACE, TextureDimensions> dataDimensions = dimensions.get(blockData);

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        float height;
        if (chunk.getBlockType(x, y + 1, z).getBlockId() == this.id) {
            height = 1;
        } else {
            height = 0.9f;
        }

        if ((x > 0 && shouldRender(x - 1, y, z, chunk)) || x == 0) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.SIDES), 0, 0, 0, 0, height, 1);
        }
        if ((x < chunk.getMaxWidth() && shouldRender(x + 1, y, z, chunk)) || x == chunk.getMaxWidth()) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.SIDES), 1, 0, 0, 1, height, 1);
        }
        if ((y > 0 && shouldRender(x, y - 1, z, chunk)) || y == 0) {
            Renderer.renderHorizontal(dataDimensions.get(BLOCK_FACE.BOTTOM), 0, 0, 1, 1, 0, false);
        }
        if ((y < chunk.getMaxHeight() && chunk.getBlockType(x, y + 1, z).getBlockId() != this.id) || y == chunk.getMaxHeight()) {
            Renderer.renderHorizontal(dataDimensions.get(BLOCK_FACE.TOP), 0, 0, 1, 1, height, false);
        }
        if ((z > 0 && shouldRender(x, y, z - 1, chunk)) || z == 0) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.FRONT), 0, 0, 0, 1, height, 0);
        }
        if ((z < chunk.getMaxLength() && shouldRender(x, y, z + 1, chunk)) || z == chunk.getMaxLength()) {
            Renderer.renderNonstandardVertical(dataDimensions.get(BLOCK_FACE.BACK), 0, 0, 1, 1, height, 1);
        }

        GL11.glPopMatrix();
    }

    private boolean shouldRender(int x, int y, int z, ChunkSchematic chunk) {
        if (chunk.getBlockType(x, y, z).getBlockId() != this.id) {
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