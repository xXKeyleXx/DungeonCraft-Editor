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

import de.keyle.dungeoncraft.editor.editors.world.render.block.Block;
import de.keyle.dungeoncraft.editor.editors.world.schematic.Schematic;
import de.keyle.dungeoncraft.editor.util.Facing;

public class ChunkSchematic {

    private short[][][] blockId;
    private byte[][][] blockData;

    int maxHeight;
    int maxWidth;
    int maxLength;

    int renderDistance = 100;

    protected int lx, ly, lz;
    private int fromX = 0;
    private int fromZ = 0;
    private int toX = renderDistance * 2;
    private int toZ = renderDistance * 2;

    public ChunkSchematic(MinecraftLevel level, Schematic schematic) {
        this.maxHeight = schematic.getHeight();
        this.maxLength = schematic.getLenght();
        this.maxWidth = schematic.getWidth();

        this.parseSchematic(schematic);
    }

    public void renderWorld(FirstPersonCameraController center, boolean solid) {
        Block block;
        short t;
        byte data;

        this.setLoopBorders(center);
        this.rewindLoop();
        t = 0;

        while (t != -2) {
            // Grab our block type
            t = this.nextBlock();
            if (t < 1) {
                continue;
            }

            // Get the actual BlockType object
            block = MinecraftConstants.getBlockType(t);
            if (block == null) {
                //XRay.logger.debug("Unknown block ID: " + t);
                block = MinecraftConstants.getBlockType((short) -1);
                data = 0;
            } else {
                data = getData(this.lx, this.ly, this.lz);
            }
            if (solid == block.isSolid()) {
                // Continue on to the actual rendering
                try {
                    block.render(data, this.lx, this.ly, this.lz, this);
                } catch (Exception e) {
                    System.out.println("Excaption cought while rendering block with ID " + block.getBlockId() + ":" + data + " (" + block.getBlockType() + ")");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * "Rewinds" our looping over blocks.
     */
    protected void rewindLoop() {
        this.lx = fromX;
        this.ly = 0;
        this.lz = fromZ;
    }

    /**
     * sets looping borders.
     */
    protected void setLoopBorders(FirstPersonCameraController center) {
        this.fromX = (int) Math.max(0, -center.getPosition().getX() - renderDistance);
        this.fromZ = (int) Math.max(0, -center.getPosition().getZ() - renderDistance);
        this.toX = (int) Math.min(maxWidth, -center.getPosition().getX() + renderDistance);
        this.toZ = (int) Math.min(maxLength, -center.getPosition().getZ() + renderDistance);
    }

    /**
     * Renders a border around this chunk.  Note that we should have our chunkBorderTexture
     * bound before we get in here.
     */
    public void renderBorder() {
        float x = -.05f;
        float z = -.05f;
        float top = this.maxHeight + 1f;
        float bottom = -1f;
        float width = this.maxWidth + .05f;
        float length = this.maxLength + .05f;
        Renderer.renderNonstandardVertical(0, 0, 1, 1, x, top, z, x + width, bottom, z);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, x, top, z + length, x + width, bottom, z + length);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, x, top, z, x, bottom, z + width);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, x + width, top, z, x + width, bottom, z + length);
    }

    private void parseSchematic(Schematic schematic) {
        byte[] schematicBlocks = schematic.getBlocks();
        byte[] schematicBlockDatas = schematic.getData();

        int blockIndex;
        blockId = new short[maxWidth][maxHeight][maxLength];
        blockData = new byte[maxWidth][maxHeight][maxLength];
        for (int xC = 0; xC < maxWidth; xC++) {
            for (int yC = 0; yC < maxHeight; yC++) {
                for (int zC = 0; zC < maxLength; zC++) {
                    blockIndex = getSchematicIndex(xC, yC, zC, maxLength, maxWidth, maxHeight);
                    if (blockIndex != -1 && blockIndex < schematicBlocks.length) {
                        blockId[xC][yC][zC] = schematicBlocks[blockIndex];
                        blockData[xC][yC][zC] = schematicBlockDatas[blockIndex];
                    }
                }
            }
        }
    }

    public static int getSchematicIndex(int x, int y, int z, int schematicLength, int schematicWidth, int schematicHeight) {
        if (x >= schematicWidth) {
            return -1;
        }
        if (y >= schematicHeight) {
            return -1;
        }
        if (z >= schematicLength) {
            return -1;
        }
        return (y * schematicWidth * schematicLength) + (z * schematicWidth) + x;
    }

    public short getAdjBlockId(int x, int y, int z, Facing facing) {
        switch (facing) {
            case UP:
                return getAdjUpBlockId(x, y, z);
            case DOWN:
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

    public short getAdjWestBlockId(int x, int y, int z) {
        if (x > 0) {
            return getBlockId(x - 1, y, z);
        } else {
            return -1;
        }
    }

    public short getAdjEastBlockId(int x, int y, int z) {
        if (x < maxWidth) {
            return getBlockId(x + 1, y, z);
        } else {
            return -1;
        }
    }

    public short getAdjNorthBlockId(int x, int y, int z) {
        if (z > 0) {
            return getBlockId(x, y, z - 1);
        } else {
            return -1;
        }
    }

    public short getAdjSouthBlockId(int x, int y, int z) {
        if (z < maxLength) {
            return getBlockId(x, y, z + 1);
        } else {
            return -1;
        }
    }

    public short getAdjUpBlockId(int x, int y, int z) {
        if (y < maxHeight) {
            return getBlockId(x, y + 1, z);
        } else {
            return -1;
        }
    }

    protected short getAdjDownBlockId(int x, int y, int z) {
        if (y > 0) {
            return getBlockId(x, y - 1, z);
        } else {
            return -1;
        }
    }

    public short getBlockId(int x, int y, int z) {
        if (x >= 0 && x < blockId.length) {
            if (y >= 0 && y < blockId[x].length) {
                if (z >= 0 && z < blockId[x][y].length) {
                    return blockId[x][y][z];
                }
            }
        }
        return -1;
    }

    public Block getBlockType(int x, int y, int z) {
        Block type = null;
        if (x >= 0 && x < blockId.length) {
            if (y >= 0 && y < blockId[x].length) {
                if (z >= 0 && z < blockId[x][y].length) {
                    type = MinecraftConstants.getBlockType(blockId[x][y][z]);
                }
            }
        }
        if (type != null) {
            return type;
        }
        return MinecraftConstants.getBlockType((short) -1);
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
        if (++this.lx > toX) {
            this.lx = fromX;
            if (++this.lz > toZ) {
                this.lz = fromZ;
                if (++this.ly >= maxHeight) {
                    return -2;
                }
            }
        }
        return getBlockId(lx, ly, lz);
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxLength() {
        return maxLength;
    }
}