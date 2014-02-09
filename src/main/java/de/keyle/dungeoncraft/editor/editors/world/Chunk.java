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

package de.keyle.dungeoncraft.editor.editors.world;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.util.Random;

public class Chunk {
    static final int CHUNK_SIZE = 64;
    static final int CUBE_LENGTH = 2;
    private Block[][][] blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private Random random;

    public void Render() {
        GL11.glPushMatrix();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);

        GL11.glPopMatrix();
    }

    public Chunk(int startX, int startY, int startZ) {
        random = new Random();
        blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (random.nextFloat() > 0.7f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                    } else if (random.nextFloat() > 0.4f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
                    } else if (random.nextFloat() > 0.2f) {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
                    } else {
                        blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
                    }
                }
            }
        }
        VBOColorHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();
        rebuildMesh(startX, startY, startZ);
    }

    public void rebuildMesh(float startX, float startY, float startZ) {
        VBOColorHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();

        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        System.out.print((CHUNK_SIZE ^ 3) * 6 * 12);
        for (float x = 0; x < CHUNK_SIZE; x += 1) {
            for (float y = 0; y < CHUNK_SIZE; y += 1) {
                for (float z = 0; z < CHUNK_SIZE; z += 1) {
                    if (random.nextFloat() > 0.9f) {
                        VertexPositionData.put(createCube(startX + x * CUBE_LENGTH, startY + y * CUBE_LENGTH, startZ + z * CUBE_LENGTH));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(blocks[(int) (x - startX)][(int) (y - startY)][(int) (z - startZ)])));
                    }
                }
            }

        }
        VertexColorData.flip();
        VertexPositionData.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    private float[] createCubeVertexCol(float[] CubeColorArray) {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for (int i = 0; i < cubeColors.length; i++) {
            cubeColors[i] = CubeColorArray[i % CubeColorArray.length];
        }
        return cubeColors;
    }

    public static float[] createCube(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        return new float[]{
                // BOTTOM QUAD(DOWN=+Y)
                x + offset, y + offset,
                z,
                x - offset,
                y + offset,
                z,
                x - offset,
                y + offset,
                z - CUBE_LENGTH,
                x + offset,
                y + offset,
                z - CUBE_LENGTH,
                // TOP!
                x + offset, y - offset, z - CUBE_LENGTH, x - offset,
                y - offset,
                z - CUBE_LENGTH,
                x - offset,
                y - offset,
                z,
                x + offset,
                y - offset,
                z,
                // FRONT QUAD
                x + offset, y + offset, z - CUBE_LENGTH, x - offset,
                y + offset, z - CUBE_LENGTH, x - offset,
                y - offset,
                z - CUBE_LENGTH,
                x + offset,
                y - offset,
                z - CUBE_LENGTH,
                // BACK QUAD
                x + offset, y - offset, z, x - offset, y - offset, z,
                x - offset, y + offset, z,
                x + offset,
                y + offset,
                z,
                // LEFT QUAD
                x - offset, y + offset, z - CUBE_LENGTH, x - offset,
                y + offset, z, x - offset, y - offset, z, x - offset,
                y - offset,
                z - CUBE_LENGTH,
                // RIGHT QUAD
                x + offset, y + offset, z, x + offset, y + offset,
                z - CUBE_LENGTH, x + offset, y - offset, z - CUBE_LENGTH,
                x + offset, y - offset, z};

    }

    private float[] getCubeColor(Block block) {
        switch (block.getID()) {
            case 1:
                return new float[]{0, 1, 0};
            case 2:
                return new float[]{1, 0.5f, 0};
            case 3:
                return new float[]{0, 0f, 1f};
        }
        return new float[]{1, 1, 1};
    }
}