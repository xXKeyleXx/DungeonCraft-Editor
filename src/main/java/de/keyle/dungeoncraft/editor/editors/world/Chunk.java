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
    private final int startX;
    private final int startY;
    private final int startZ;
    private int[][][] blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private boolean init = false;

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }

    public void render() {
        render1();
    }

    public void render1() {
        if (!init) {
            init = true;
            VBOColorHandle = GL15.glGenBuffers();
            VBOVertexHandle = GL15.glGenBuffers();
            rebuildMesh(startX * 64, startY * 64, startZ * 64);
        }
        GL11.glPushMatrix();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        GL11.glPopMatrix();
    }


    public boolean render2() {

        //GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix
        //GL11.glTranslatef(1.5f,0.0f,-3.0f);             // Move Right 1.5 Units And Into The Screen 6.0
        //GL11.glRotatef(rquad,1.0f,1.0f,1.0f);               // Rotate The Quad On The X axis ( NEW )
        //GL11.glColor3f(0.5f,0.5f,1.0f);                 // Set The Color To Blue One Time Only
        GL11.glBegin(GL11.GL_QUADS);                        // Draw A Quad
        //Top
        GL11.glColor3f(0.0f, 1.0f, 0.0f);             // Set The Color To Green
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Top)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Top)
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Bottom Left Of The Quad (Top)
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Bottom Right Of The Quad (Top)
        //Bottom
        GL11.glColor3f(1.0f, 0.5f, 0.0f);             // Set The Color To Orange
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Top Right Of The Quad (Bottom)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Top Left Of The Quad (Bottom)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Bottom)
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Bottom)
        //Right
        GL11.glColor3f(1.0f, 0.0f, 0.0f);             // Set The Color To Red
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Top Right Of The Quad (Front)
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Top Left Of The Quad (Front)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Bottom Left Of The Quad (Front)
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Bottom Right Of The Quad (Front)
        //Left
        GL11.glColor3f(1.0f, 1.0f, 0.0f);             // Set The Color To Yellow
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Back)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Back)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Back)
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Back)
        //Front
        GL11.glColor3f(0.0f, 0.0f, 1.0f);             // Set The Color To Blue
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Top Right Of The Quad (Left)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Left)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Left)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Bottom Right Of The Quad (Left)
        //Back
        GL11.glColor3f(1.0f, 0.0f, 1.0f);             // Set The Color To Violet
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Right)
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Top Left Of The Quad (Right)
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Bottom Left Of The Quad (Right)
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Right)

        GL11.glEnd();                                       // Done Drawing The Quad

        GL11.glTranslatef(5f, 0.0f, 0f);
        GL11.glBegin(GL11.GL_QUADS);                        // Draw A Quad
        //Top
        GL11.glColor3f(0.0f, 1.0f, 0.0f);             // Set The Color To Green
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Top)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Top)
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Bottom Left Of The Quad (Top)
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Bottom Right Of The Quad (Top)
        //Bottom
        GL11.glColor3f(1.0f, 0.5f, 0.0f);             // Set The Color To Orange
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Top Right Of The Quad (Bottom)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Top Left Of The Quad (Bottom)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Bottom)
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Bottom)
        //Right
        GL11.glColor3f(1.0f, 0.0f, 0.0f);             // Set The Color To Red
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Top Right Of The Quad (Front)
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Top Left Of The Quad (Front)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Bottom Left Of The Quad (Front)
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Bottom Right Of The Quad (Front)
        //Left
        GL11.glColor3f(1.0f, 1.0f, 0.0f);             // Set The Color To Yellow
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Back)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Back)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Back)
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Back)
        //Front
        GL11.glColor3f(0.0f, 0.0f, 1.0f);             // Set The Color To Blue
        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);         // Top Right Of The Quad (Left)
        GL11.glVertex3f(-1.0f, 1.0f, -1.0f);         // Top Left Of The Quad (Left)
        GL11.glVertex3f(-1.0f, -1.0f, -1.0f);         // Bottom Left Of The Quad (Left)
        GL11.glVertex3f(-1.0f, -1.0f, 1.0f);         // Bottom Right Of The Quad (Left)
        //Back
        GL11.glColor3f(1.0f, 0.0f, 1.0f);             // Set The Color To Violet
        GL11.glVertex3f(1.0f, 1.0f, -1.0f);         // Top Right Of The Quad (Right)
        GL11.glVertex3f(1.0f, 1.0f, 1.0f);         // Top Left Of The Quad (Right)
        GL11.glVertex3f(1.0f, -1.0f, 1.0f);         // Bottom Left Of The Quad (Right)
        GL11.glVertex3f(1.0f, -1.0f, -1.0f);         // Bottom Right Of The Quad (Right)

        GL11.glEnd();                                       // Done Drawing The Quad
        return true;
    }

    public Chunk(int startX, int startY, int startZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        Random random = new Random();
        blocks = new int[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (random.nextFloat() > 0.7f) {
                        blocks[x][y][z] = 1;
                    } else if (random.nextFloat() > 0.4f) {
                        blocks[x][y][z] = 2;
                    } else if (random.nextFloat() > 0.2f) {
                        blocks[x][y][z] = 3;
                    } else {
                        blocks[x][y][z] = 4;
                    }
                }
            }
        }
    }

    public Chunk(int startX, int startY, int startZ, int[][][] blocks) {
        System.out.println("Chunk: " + startX + ", " + startY + ", " + startZ);
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.blocks = blocks;
    }
    int blockfacecounter;

    public void rebuildMesh(float startX, float startY, float startZ) {
        VBOColorHandle = GL15.glGenBuffers();
        VBOVertexHandle = GL15.glGenBuffers();

        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) * 6 * 12);
        //System.out.print((CHUNK_SIZE ^ 3) * 6 * 12);
        int blockID;
        blockfacecounter = 0;
        for (float x = 0; x < CHUNK_SIZE; x++) {
            for (float y = 0; y < CHUNK_SIZE; y++) {
                for (float z = 0; z < CHUNK_SIZE; z++) {
                    blockID = blocks[((int) x)][(int) (y)][(int) (z)];
                    if (blockID != 0) {
                        VertexPositionData.put(createCube(x*CUBE_LENGTH, y*CUBE_LENGTH, z*CUBE_LENGTH));
                        VertexColorData.put(createCubeVertexCol(getCubeColor(blockID)));
                    }
                }
            }

        }
        System.out.println("blockfacecounter: " + blockfacecounter);
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

    public int getBlockId(int x, int y, int z) {
        if (x >= 0 && x < blocks.length) {
            if (y >= 0 && y < blocks[x].length) {
                if (z >= 0 && z < blocks[x][y].length) {
                    return blocks[x][y][z];
                }
            }
        }
        return -1;
    }

    public float[] createCube(float x, float y, float z) {
        return createCube1(x, y, z);
    }

    public float[] createCube2(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        int offsetX = startX * CHUNK_SIZE * CUBE_LENGTH;
        int offsetY = startY * CHUNK_SIZE * CUBE_LENGTH;
        int offsetZ = startZ * CHUNK_SIZE * CUBE_LENGTH;
        return new float[]{
                // BOTTOM QUAD(DOWN=+Y)
                x + offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x + offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                // TOP!
                x + offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z + offsetZ,
                x + offset + offsetX,
                y - offset + offsetY,
                z + offsetZ,
                // FRONT QUAD
                x + offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x - offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x + offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                // BACK QUAD
                x + offset + offsetX,
                y - offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                x + offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                // LEFT QUAD
                x - offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH,
                x - offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z + offsetZ,
                x - offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                // RIGHT QUAD
                x + offset + offsetX,
                y + offset + offsetY,
                z + offsetZ,
                x + offset + offsetX,
                y + offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x + offset + offsetX,
                y - offset + offsetY,
                z - CUBE_LENGTH + offsetZ,
                x + offset + offsetX,
                y - offset + offsetY,
                z + offsetZ};
    }

    public float[] createCube1(float x, float y, float z) {
        int offset = CUBE_LENGTH / 2;
        int arrayLength = 0;
        int visibleSides = 0;

        //System.out.println("bottom: " + getBlockId((int) x, (int) y-1, (int) z));
        if (getBlockId((int) x, (int) y - 1, (int) z) <= 0) {
            arrayLength += 12;
            visibleSides += 0x1;
        }
        //System.out.println("top " + getBlockId((int) x, (int) y+1, (int) z));
        if (getBlockId((int) x, (int) y + 1, (int) z) <= 0) {
            arrayLength += 12;
            visibleSides += 0x10;
        }
        //System.out.println("left: " + getBlockId((int) x-1, (int) y, (int) z));
        if (getBlockId((int) x - 1, (int) y, (int) z) <= 0) {
            arrayLength += 12;
            visibleSides += 0x100;
        }
        //System.out.println("right: " + getBlockId((int) x+1, (int) y, (int) z));
        if (getBlockId((int) x + 1, (int) y, (int) z) <= 0) {
            arrayLength += 12;
            visibleSides += 0x1000;
        }
        //System.out.println("front: " + getBlockId((int) x, (int) y, (int) z - 1));
        if (getBlockId((int) x, (int) y, (int) z - 1) <= 0) {
            arrayLength += 12;
            visibleSides += 0x10000;
        }
        //System.out.println("back: " + getBlockId((int) x, (int) y, (int) z + 1));
        if (getBlockId((int) x, (int) y, (int) z + 1) <= 0) {
            arrayLength += 12;
            visibleSides += 0x100000;
        }

        arrayLength = 72;
        visibleSides = 0x111111;

        float[] returnValue = new float[arrayLength];
        //System.out.println("visible: " + visibleSides);
        if (visibleSides == 0) {
            return returnValue;
        }

        int offsetX = startX * CHUNK_SIZE * CUBE_LENGTH;
        int offsetY = startY * CHUNK_SIZE * CUBE_LENGTH;
        int offsetZ = startZ * CHUNK_SIZE * CUBE_LENGTH;
        int counter = 0;



        // BACK QUAD
        if (visibleSides >= 0x100000) {
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ;
            visibleSides -= 0x100000;
            blockfacecounter++;
        }

        // FRONT QUAD
        if (visibleSides >= 0x10000) {
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            visibleSides -= 0x10000;
            blockfacecounter++;
        }

        // LEFT QUAD
        if (visibleSides >= 0x1000) {
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            visibleSides -= 0x1000;
            blockfacecounter++;
        }

        // RIGHT QUAD
        if (visibleSides >= 0x100) {
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY + offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            visibleSides -= 0x100;
            blockfacecounter++;
        }

        // TOP QUAD
        if (visibleSides >= 0x10) {
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX - offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX + offset;
            returnValue[counter++] = y + offsetY - offset;
            returnValue[counter++] = z + offsetZ;
            visibleSides -= 0x10;
            blockfacecounter++;
        }

        // BOTTOM QUAD
        if (visibleSides >= 0x1) {
            returnValue[counter++] = x + offsetX + offset ;
            returnValue[counter++] = y + offsetY + offset ;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX  - offset;
            returnValue[counter++] = y + offsetY  + offset;
            returnValue[counter++] = z + offsetZ;
            returnValue[counter++] = x + offsetX  - offset;
            returnValue[counter++] = y + offsetY  + offset;
            returnValue[counter++] = z + offsetZ - CUBE_LENGTH;
            returnValue[counter++] = x + offsetX  + offset;
            returnValue[counter++] = y + offsetY  + offset;
            returnValue[counter] = z + offsetZ - CUBE_LENGTH;
            blockfacecounter++;
        }

        return returnValue;
    }

    private float[] getCubeColor(int blockID) {
        switch (blockID) {
            case 1:
                return new float[]{0.4f, 0.4f, 0.4f};
            case 2:
                return new float[]{0, 1, 0};
            case 3:
                return new float[]{0.4f, 0.12f, 0};
            case 5:
                return new float[]{0.4f, 0.27f, 0};
            case 8:
            case 9:
                return new float[]{0, 0f, 1f};
        }

        Random r = new Random();
        return new float[]{r.nextFloat(), r.nextFloat(), r.nextFloat()};
        //return new float[]{1, 1, 1};
    }
}