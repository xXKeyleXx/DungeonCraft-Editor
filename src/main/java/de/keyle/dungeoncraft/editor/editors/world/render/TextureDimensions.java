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

package de.keyle.dungeoncraft.editor.editors.world.render;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class TextureDimensions {
    int blockId;
    byte data = 0;
    float texLeft;
    float texTop;
    float texWidth;
    float texHeight;

    public TextureDimensions(int blockId, byte data, float texLeft, float texTop, float texWidth, float texHeight) {
        this.blockId = blockId;
        this.data = data;
        this.texLeft = texLeft;
        this.texTop = texTop;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    public TextureDimensions(int blockId, float texLeft, float texTop, float texWidth, float texHeight) {
        this(blockId, (byte) 0, texLeft, texTop, texWidth, texHeight);
    }

    public TextureDimensions(int blockId, byte data, int texLeft, int texTop) {
        this.blockId = blockId;
        this.data = data;
        this.texLeft = texLeft * TEX16;
        this.texTop = texTop * TEX_Y;
        this.texWidth = TEX16;
        this.texHeight = TEX_Y;
    }

    public TextureDimensions(int blockId, int texLeft, int texTop) {
        this(blockId, (byte) 0, texLeft, texTop);
    }

    public TextureDimensions(int blockId, byte data, int texLeft, int texTop, float texWidth, float texHeigh, boolean top) {
        this.blockId = blockId;
        this.data = data;
        this.texLeft = texLeft * TEX16;
        this.texTop = texTop * TEX_Y + (top ? 0 : (TEX_Y - texHeigh));
        this.texWidth = texWidth;
        this.texHeight = texHeigh;
    }

    public TextureDimensions(int blockId, int texLeft, int texTop, float texWidth, float texHeigh, boolean top) {
        this(blockId, (byte) 0, texLeft, texTop, texWidth, texHeigh, top);
    }

    public int getBlockId() {
        return blockId;
    }

    public byte getData() {
        return data;
    }

    public float getTexLeft() {
        return texLeft;
    }

    public float getTexTop() {
        return texTop;
    }

    public float getTexWidth() {
        return texWidth;
    }

    public float getTexHeight() {
        return texHeight;
    }
}