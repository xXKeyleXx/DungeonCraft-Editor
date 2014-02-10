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

package de.keyle.dungeoncraft.editor.editors.world.schematic;

public class Schematic {
    private final byte[] blocks;
    private final byte[] data;
    private final byte[] biomes;
    private final short width;
    private final short lenght;
    private final short height;

    public Schematic(byte[] blocks, byte[] data, byte[] biomes, short width, short lenght, short height) {
        this.blocks = blocks;
        this.data = data;
        this.biomes = biomes;
        this.width = width;
        this.lenght = lenght;
        this.height = height;
    }

    public Schematic(byte[] blocks, byte[] data, short width, short lenght, short height) {
        this.blocks = blocks;
        this.data = data;
        this.biomes = new byte[width * lenght];
        this.width = width;
        this.lenght = lenght;
        this.height = height;
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getBiomes() {
        return biomes;
    }

    public short getWidth() {
        return width;
    }

    public short getLenght() {
        return lenght;
    }

    public short getHeight() {
        return height;
    }
}