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

public class Cactus extends Default {
    public Cactus(short id) throws BlockTypeLoadException {
        super(id);
    }

    @Override
    public String getBlockType() {
        return "CACTUS";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        if (dimensions[blockData] == null) {
            blockData = 0;
        }
        TextureDimensions[] dataDimensions = dimensions[blockData];

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);

        Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], 0.0625f, 0, 0, 0.0625f, 1, 1);
        Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.SIDES.ordinal()], 0.9f, 0, 0, 0.9375f, 1, 1);
        if ((y > 0 && shouldRender(x, y - 1, z, chunk)) || y == 0) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.BOTTOM.ordinal()], 0.0625f, 0.0625f, 0.9375f, 0.9375f, 0, false);
        }
        if ((y < chunk.getMaxHeight() && shouldRender(x, y + 1, z, chunk)) || y == chunk.getMaxHeight()) {
            Renderer.renderHorizontal(dataDimensions[BLOCK_FACE.TOP.ordinal()], 0.0625f, 0.0625f, 0.9375f, 0.9375f, 1, false);
        }
        Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.FRONT.ordinal()], 0, 0, 0.0625f, 1, 1, 0.0625f);
        Renderer.renderNonstandardVertical(dataDimensions[BLOCK_FACE.BACK.ordinal()], 0, 0, 0.9375f, 1, 1, 0.9375f);

        GL11.glPopMatrix();
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