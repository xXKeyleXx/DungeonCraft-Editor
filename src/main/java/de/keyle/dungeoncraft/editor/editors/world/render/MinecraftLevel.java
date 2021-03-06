/**
 * Copyright (c) 2010-2012, Vincent Vollers and Christopher J. Kucera
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Minecraft X-Ray team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL VINCENT VOLLERS OR CJ KUCERA BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.keyle.dungeoncraft.editor.editors.world.render;

import de.keyle.dungeoncraft.editor.editors.world.schematic.Schematic;

/**
 * A Minecraft level
 *
 * @author Vincent
 */
public class MinecraftLevel {

    //public static int LEVELDATA_SIZE = 256;
    public ChunkSchematic chunk;

    public Schematic schematic;

    // This var holds the index of the player position we've most recently picked
    private int spawnPoint_idx;

    /**
     * Create a minecraftLevel from the given world
     *
     * @param schematic
     */
    public MinecraftLevel(Schematic schematic) {
        this.schematic = schematic;

        int chunkCountX = (int) Math.ceil(schematic.getWidth() / 16.);
        int chunkCountZ = (int) Math.ceil(schematic.getLenght() / 16.);

        loadSchematic();

        this.spawnPoint_idx = -1;
    }

    public void loadSchematic() {
        chunk = new ChunkSchematic(schematic);
    }

    public ChunkSchematic getChunk() {
        return chunk;
    }

    /**
     * correctly calculate the chunk X value given a universal coordinate
     *
     * @param x
     * @return
     */
    public static int getChunkX(int x) {
        if (x < 0) {
            return -(((-x) - 1) / 16) - 1; // otherwise -1 and +1 would return the same chunk
        } else {
            return x / 16;
        }
    }

    /**
     * correctly calculate the block X value given a universal coordinate
     *
     * @param x
     * @return
     */
    public static int getBlockX(int x) {
        if (x < 0) {
            return 15 - (((-x) - 1) % 16); // compensate for different chunk calculation
        } else {
            return x % 16;
        }
    }

    /**
     * correctly calculate the chunk Z value given a universal coordinate
     *
     * @param z
     * @return
     */
    public static int getChunkZ(int z) {
        if (z < 0) {
            return -(((-z) - 1) / 16) - 1; // otherwise -1 and +1 would return the same chunk
        } else {
            return z / 16;
        }
    }

    /**
     * correctly calculate the block Z value given a universal coordinate
     *
     * @param z
     * @return
     */
    public static int getBlockZ(int z) {
        if (z < 0) {
            return 15 - (((-z) - 1) % 16); // compensate for different chunk calculation
        } else {
            return z % 16;
        }
    }

    /**
     * Sets chunk to null
     */
    public void clearChunk() {
        chunk = null;
    }
}