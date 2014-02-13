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

    protected short getAdjUpBlockId(int x, int y, int z) {
        return getBlock(x, y + 1, z);
    }

    protected short getAdjDownBlockId(int x, int y, int z) {
        if (y <= 0) {
            return -1;
        }
        return getBlock(x, y - 1, z);
    }

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

    protected short nextBlock() {
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