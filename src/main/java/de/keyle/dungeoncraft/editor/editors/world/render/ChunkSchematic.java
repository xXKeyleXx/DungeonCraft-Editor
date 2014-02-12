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

public class ChunkSchematic extends Chunk {

    private short[][][] blockId;
    private byte[][][] blockData;

    public ChunkSchematic(MinecraftLevel level, int x, int z, Schematic schematic) {
        super(level, x, z);
        this.ceilingHeight = this.maxHeight = schematic.getHeight();

        this.parseSchematic(schematic);

        this.finishConstructor();
    }

    private void parseSchematic(Schematic schematic) {
        byte[] schematicBlocks = schematic.getBlocks();
        byte[] schematicBlockDatas = schematic.getData();
        int schematicHeight = schematic.getHeight();
        int schematicWidth = schematic.getWidth();
        int schematicLength = schematic.getLenght();

        int blockIndex;
        blockId = new short[16][schematicHeight][16];
        blockData = new byte[16][schematicHeight][16];
        for (int xC = 0; xC < 16; xC++) {
            for (int yC = 0; yC < schematicHeight; yC++) {
                for (int zC = 0; zC < 16; zC++) {
                    blockIndex = getSchematicIndex(x, z, xC, yC, zC, schematicLength, schematicWidth, schematicHeight);
                    if (blockIndex != -1 && blockIndex < schematicBlocks.length) {
                        blockId[xC][yC][zC] = schematicBlocks[blockIndex];
                        blockData[xC][yC][zC] = schematicBlockDatas[blockIndex];
                    }
                }
            }
        }
    }

    public static int getSchematicIndex(int chunkX, int chunkZ, int x, int y, int z, int schematicLength, int schematicWidth, int schematicHeight) {
        if (x >= schematicWidth - chunkX * 16) {
            return -1;
        }
        if (y >= schematicHeight) {
            return -1;
        }
        if (z >= schematicLength - chunkZ * 16) {
            return -1;
        }
        return (y * schematicWidth * schematicLength) + ((z + chunkZ * 16) * schematicWidth) + (x + chunkX * 16);
    }

    /**
     * Gets the Block ID of the block immediately to the given facing. This might
     * load in the adjacent chunk, if needed.  Will return -1 if that adjacent
     * chunk can't be found.
     */
    protected short getAdjBlockId(int x, int y, int z, FACING facing) {
        switch (facing) {
            case TOP:
                return getAdjUpBlockId(x, y, z);
            case BOTTOM:
                return getAdjDownBlockId(x, y, z);
            case NORTH:
                return getAdjNorthBlockId(x, y, z);
            case SOUTH:
                return getAdjSouthBlockId(x, y, z);
            case WEST:
                return getAdjWestBlockId(x, y, z);
            case EAST:
                return getAdjEastBlockId(x, y, z);
        }

        return -1;
    }

    /**
     * Gets the Block ID of the block immediately to the west.  This might
     * load in the adjacent chunk, if needed.  Will return -1 if that adjacent
     * chunk can't be found.
     */
    protected short getAdjWestBlockId(int x, int y, int z) {
        if (x > 0) {
            return getBlock(x - 1, y, z);
        } else {
            Chunk otherChunk = level.getChunk(this.x - 1, this.z);
            if (otherChunk == null) {
                return -1;
            } else {
                return otherChunk.getBlock(15, y, z);
            }
        }
    }

    /**
     * Gets the Block ID of the block immediately to the east.  This might
     * load in the adjacent chunk, if needed.  Will return -1 if that adjacent
     * chunk can't be found.
     */
    protected short getAdjEastBlockId(int x, int y, int z) {
        if (x < 15) {
            return getBlock(x + 1, y, z);
        } else {
            Chunk otherChunk = level.getChunk(this.x + 1, this.z);
            if (otherChunk == null) {
                return -1;
            } else {
                return otherChunk.getBlock(0, y, z);
            }
        }
    }

    /**
     * Gets the Block ID of the block immediately to the south.  This might
     * load in the adjacent chunk, if needed.  Will return -1 if that adjacent
     * chunk can't be found.
     */
    protected short getAdjNorthBlockId(int x, int y, int z) {
        if (z > 0) {
            return getBlock(x, y, z - 1);
        } else {
            Chunk otherChunk = level.getChunk(this.x, this.z - 1);
            if (otherChunk == null) {
                return -1;
            } else {
                return otherChunk.getBlock(x, y, 15);
            }
        }
    }

    /**
     * Gets the Block ID of the block immediately to the north.  This might
     * load in the adjacent chunk, if needed.  Will return -1 if that adjacent
     * chunk can't be found.
     */
    protected short getAdjSouthBlockId(int x, int y, int z) {
        if (z < 15) {
            return getBlock(x, y, z + 1);
        } else {
            Chunk otherChunk = level.getChunk(this.x, this.z + 1);
            if (otherChunk == null) {
                return -1;
            } else {
                return otherChunk.getBlock(x, y, 0);
            }
        }
    }

    /**
     * Gets the Block ID of the block immediately up.
     * Will return -1 if we're already at the top
     */
    protected short getAdjUpBlockId(int x, int y, int z) {
        return getBlock(x, y + 1, z);
    }

    /**
     * Gets the Block ID of the block immediately down.
     * Will return -1 if we're already at the bottom
     */
    protected short getAdjDownBlockId(int x, int y, int z) {
        if (y <= 0) {
            return -1;
        }
        return getBlock(x, y - 1, z);
    }

    /**
     * Gets the block ID at the specified coordinate in the chunk.  This is
     * only really used in the getAdj*BlockId() methods.
     */
    public short getBlock(int x, int y, int z) {
        if (x >= 0 && x < blockId.length) {
            if (y >= 0 && y < blockId[x].length) {
                if (z >= 0 && z < blockId[x][y].length) {
                    return blockId[x][y][z];
                }
            }
        }
        return -1;
    }

    /**
     * Gets the block data at the specified coordinates.
     */
    public byte getData(int x, int y, int z) {
        if (x >= 0 && x < blockData.length) {
            if (y >= 0 && y < blockData[x].length) {
                if (z >= 0 && z < blockData[x][y].length) {
                    return blockData[x][y][z];
                }
            }
        }
        return 0;
    }

    /**
     * Advances our block loop
     */
    protected short nextBlock() {
        //I don't get why it want a 1 here first
        if (++this.lx > 0xF) {
            this.lx = 0;
            if (++this.lz > 0xF) {
                this.lz = 0;
                if (++this.ly >= maxHeight) {
                    return -2;
                }
            }
        }
        return getBlock(lx, ly, lz);
    }
}