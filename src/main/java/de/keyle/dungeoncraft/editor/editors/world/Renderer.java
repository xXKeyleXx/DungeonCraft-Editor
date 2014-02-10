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

import de.keyle.dungeoncraft.editor.editors.world.schematic.Schematic;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class Renderer extends Thread {
    private float PX, PY, PZ, PR = 45f;
    Canvas canvas;
    final Map<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setSchematic(Schematic schematic) {
        int schematicHeight = schematic.getHeight();
        int schematicWidth = schematic.getWidth();
        int schematicLength = schematic.getLenght();
        byte[] schematicBlocks = schematic.getBlocks();
        int chunkCountX = (int) Math.ceil(schematicWidth / (double) Chunk.CHUNK_SIZE);
        int chunkCountY = (int) Math.ceil(schematicHeight / (double) Chunk.CHUNK_SIZE);
        int chunkCountZ = (int) Math.ceil(schematicLength / (double) Chunk.CHUNK_SIZE);

        int blockIndex;
        Map<Integer, Chunk> newChunks = new HashMap<Integer, Chunk>();
        for (int x = 0; x < chunkCountX; x++) {
            for (int y = 0; y < chunkCountY; y++) {
                for (int z = 0; z < chunkCountZ; z++) {
                    int[][][] blocks = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];

                    for (int xC = 0; xC < Chunk.CHUNK_SIZE; xC++) {
                        for (int yC = 0; yC < Chunk.CHUNK_SIZE; yC++) {
                            for (int zC = 0; zC < Chunk.CHUNK_SIZE; zC++) {
                                blockIndex = getSchematicIndex(x, z, xC, yC, zC, schematicLength, schematicWidth);
                                if (blockIndex != -1 && blockIndex < schematicBlocks.length) {
                                    blocks[xC][yC][zC] = schematicBlocks[blockIndex] & 0xFF;
                                }
                            }
                        }
                    }
                    Chunk c = new Chunk(x, y, z, blocks);
                    newChunks.put(getHashCode(x, y, z), c);
                }
            }
        }

        synchronized (chunks) {
            chunks.clear();
            chunks.putAll(newChunks);
        }
    }

    public int getHashCode(int x, int y, int z) {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    public static int getSchematicIndex(int chunkX, int chunkZ, int x, int y, int z, int schematicLength, int schematicWidth) {
        if (x >= schematicWidth - chunkX * Chunk.CHUNK_SIZE) {
            return -1;
        }
        if (z >= schematicLength - chunkZ * Chunk.CHUNK_SIZE) {
            return -1;
        }
        return (y * schematicWidth * schematicLength) + ((z + chunkZ * Chunk.CHUNK_SIZE) * schematicWidth) + (x + chunkX * Chunk.CHUNK_SIZE);
    }

    public void run() {
        try {
            Display.setParent(canvas);
            Display.create();
            initGL();

            createVBO();

            while (!Display.isCloseRequested()) {
                try {
                    processInput();
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef(-30f + PX, -40f + PY, -160f + PZ); // Move Right
                    GL11.glRotatef(PR, 0.4f, 1.0f, 0.1f);
                    GL11.glRotatef(PR, 0f, 1.0f, 0f);
                    GL11.glAlphaFunc(GL11.GL_EQUAL, 0.5f);
                    /*
                    int minX = (int) (PX - (Math.floor(PX) % Chunk.CHUNK_SIZE)) / Chunk.CHUNK_SIZE - 1;
                    int minY = (int) (PY - (Math.floor(PY) % Chunk.CHUNK_SIZE)) / Chunk.CHUNK_SIZE - 1;
                    int minZ = (int) (PZ - (Math.floor(PZ) % Chunk.CHUNK_SIZE)) / Chunk.CHUNK_SIZE - 1;
                    //System.out.println("x: " + PX + "(" + minX + "), y: " + PY + "(" + minY + "), z: " + PZ + "(" + minZ + ")");
                    */
                    synchronized (chunks) {
                        /*
                        int hash;
                        for (int x = 0; x < 3; x++) {
                            for (int y = 0; y < 3; y++) {
                                for (int z = 0; z < 3; z++) {
                                    hash = getHashCode(minX + x, minY + y, minZ + z);
                                    if (chunks.containsKey(hash)) {
                                        //System.out.println("show: " + (minX + x) + ", " + (minY + y) + ", " + (minZ + z));
                                        chunks.get(hash).render();
                                    }
                                }
                            }
                        }
                        */

                        for (Chunk c : chunks.values()) {
                            //System.out.print();
                            //GL11.glTranslatef(c.getStartX()*Chunk.CHUNK_SIZE/Chunk.CUBE_LENGTH, c.getStartY()*Chunk.CHUNK_SIZE/Chunk.CUBE_LENGTH, 0); // Move Right
                            c.render();
                        }
                    }
                    Display.update();
                    //Display.sync(60);
                } catch (Exception ignored) {
                }
            }
            Display.destroy();

        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private void initGL() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GLU.gluPerspective(45.0f, 1, 0.1f, 300.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    private void processInput() {
        if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            PR -= 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            PR += 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            PY -= 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            PY += 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            PX += 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            PX -= 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            PZ -= 0.5;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            PZ += 0.5;
        }

    }

    private void createVBO() {
        int VBOColorHandle = GL15.glGenBuffers();
        int VBOVertexHandle = GL15.glGenBuffers();
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(24 * 3);
        VertexPositionData.put(new float[]{1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
                -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,

                1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,

                1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f,

                1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,

                -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,

                1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
                -1.0f, -1.0f});
        VertexPositionData.flip();
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(24 * 3);
        VertexColorData.put(new float[]{1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,
                1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1,
                0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1,
                0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,});
        VertexColorData.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}