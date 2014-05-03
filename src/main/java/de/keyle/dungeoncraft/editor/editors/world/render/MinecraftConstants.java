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

import de.keyle.dungeoncraft.editor.GuiMain;
import de.keyle.dungeoncraft.editor.editors.world.render.block.Block;
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.ExceptionDialog;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;


/**
 * Precalcs and the like
 *
 * @author Vincent
 */
public class MinecraftConstants {
    public static final float TEX16 = 1.0f / 16.0f;
    public static final float TEX32 = 1.0f / 32.0f;
    public static final float TEX64 = 1.0f / 64.0f;
    public static final float TEX128 = 1.0f / 128.0f;
    public static final float TEX256 = 1.0f / 256.0f;
    public static final float TEX512 = 1.0f / 512.0f;

    public static float TEX_Y = TEX32;

    // sensitivity and speed mouse configuration
    public static final float MOUSE_SENSITIVITY = 0.05f; // mouse delta is multiplied by this
    public static float MOVEMENT_SPEED = 10.0f; // world units per second

    // the font to draw information to the screen
    public static final Font ARIALFONT = new Font("Arial", Font.BOLD, 14);
    public static final Font HEADERFONT = new Font("Arial", Font.BOLD, 26);
    public static final Font DETAILFONT = new Font("Arial", Font.PLAIN, 13);
    public static final Font DETAILVALUEFONT = new Font("Arial", Font.BOLD, 13);
    public static final Font SMALLFONT = new Font("Arial", Font.PLAIN, 10);

    // some convenience statics regarding time calculation
    public static final long NANOSPERSECOND = 1000000000;
    public static final long MILLISPERSECOND = 1000;
    public static final long NANOSPERMILLIS = 1000000L;


    private static BlockTypes blockTypes;

    // Keyboard action categories
    public static enum ACTION_CAT {
        MOVEMENT("Movement"),
        CAMERA("Camera"),
        RENDERING("Rendering"),
        OTHER("Other");
        public String title;

        ACTION_CAT(String title) {
            this.title = title;
        }
    }

    // Keyboard actions
    public static enum KEY_ACTION {
        MOVE_FORWARD(ACTION_CAT.MOVEMENT, Keyboard.KEY_W, "Move Forward"),
        MOVE_BACKWARD(ACTION_CAT.MOVEMENT, Keyboard.KEY_S, "Move Backward"),
        MOVE_LEFT(ACTION_CAT.MOVEMENT, Keyboard.KEY_A, "Strafe Left"),
        MOVE_RIGHT(ACTION_CAT.MOVEMENT, Keyboard.KEY_D, "Strafe Right"),
        MOVE_UP(ACTION_CAT.MOVEMENT, Keyboard.KEY_SPACE, "Fly Up"),
        MOVE_DOWN(ACTION_CAT.MOVEMENT, Keyboard.KEY_LSHIFT, "Fly Down"),
        SPEED_INCREASE(ACTION_CAT.MOVEMENT, Keyboard.KEY_LCONTROL, "Move Faster"),
        SPEED_DECREASE(ACTION_CAT.MOVEMENT, Keyboard.KEY_RSHIFT, "Move Slower"),

        MOVE_TO_SPAWN(ACTION_CAT.CAMERA, Keyboard.KEY_HOME, "Jump to Spawnpoint"),
        MOVE_TO_PLAYER(ACTION_CAT.CAMERA, Keyboard.KEY_END, "Jump to Player Position"),
        MOVE_NEXT_CAMERAPOS(ACTION_CAT.CAMERA, Keyboard.KEY_INSERT, "Jump to Next Camera Preset"),
        MOVE_PREV_CAMERAPOS(ACTION_CAT.CAMERA, Keyboard.KEY_DELETE, "Jump to Previous Camera Preset"),
        JUMP(ACTION_CAT.CAMERA, Keyboard.KEY_J, "Jump to Abritrary Position"),
        JUMP_NEAREST(ACTION_CAT.CAMERA, Keyboard.KEY_MINUS, "Jump to nearest existing chunk"),
        DIMENSION_NEXT(ACTION_CAT.CAMERA, Keyboard.KEY_N, "Jump to Next Dimension"),
        DIMENSION_PREV(ACTION_CAT.CAMERA, Keyboard.KEY_P, "Jump to Previous Dimension"),
        TOGGLE_CAMERA_LOCK(ACTION_CAT.CAMERA, Keyboard.KEY_L, "Lock Camera to Vertical Axis"),

        TOGGLE_REGION_HIGHLIGHTING(ACTION_CAT.RENDERING, Keyboard.KEY_H, "Toggle Region Highlight Glow"),
        TOGGLE_FULLBRIGHT(ACTION_CAT.RENDERING, Keyboard.KEY_F, "Toggle Fullbright"),
        TOGGLE_BEDROCK(ACTION_CAT.RENDERING, Keyboard.KEY_B, "Toggle Bedrock"),
        TOGGLE_WATER(ACTION_CAT.RENDERING, Keyboard.KEY_T, "Toggle Water"),
        LIGHT_INCREASE(ACTION_CAT.RENDERING, Keyboard.KEY_ADD, "Increase Lighting Range"),
        LIGHT_DECREASE(ACTION_CAT.RENDERING, Keyboard.KEY_SUBTRACT, "Decrease Lighting Range"),
        CHUNK_RANGE_1(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD1, "Visibility Range 1"),
        CHUNK_RANGE_2(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD2, "Visibility Range 2"),
        CHUNK_RANGE_3(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD3, "Visibility Range 3"),
        CHUNK_RANGE_4(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD4, "Visibility Range 4"),
        CHUNK_RANGE_5(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD5, "Visibility Range 5"),
        CHUNK_RANGE_6(ACTION_CAT.RENDERING, Keyboard.KEY_NUMPAD6, "Visibility Range 6"),
        TOGGLE_ACCURATE_GRASS(ACTION_CAT.RENDERING, Keyboard.KEY_G, "Toggle Accurate Grass"),
        TOGGLE_BETA19_FENCES(ACTION_CAT.RENDERING, Keyboard.KEY_C, "Toggle Beta 1.9 Fences"),
        TOGGLE_CHUNK_BORDERS(ACTION_CAT.RENDERING, Keyboard.KEY_U, "Toggle Chunk Borders"),

        TOGGLE_POSITION_INFO(ACTION_CAT.OTHER, Keyboard.KEY_K, "Toggle Level Info"),
        TOGGLE_RENDER_DETAILS(ACTION_CAT.OTHER, Keyboard.KEY_R, "Toggle Rendering Info"),
        RELEASE_MOUSE(ACTION_CAT.OTHER, Keyboard.KEY_ESCAPE, "Release Mouse"),
        KEY_HELP(ACTION_CAT.OTHER, Keyboard.KEY_Y, "Show Keyboard Reference"),
        QUIT(ACTION_CAT.OTHER, Keyboard.KEY_Q, "Quit");
        public final ACTION_CAT category;
        public final int def_key;
        public final String desc;

        KEY_ACTION(ACTION_CAT category, int def_key, String desc) {
            this.category = category;
            this.def_key = def_key;
            this.desc = desc;
        }
    }

    public static final BlockVector[] SURROUNDINGBLOCKS = new BlockVector[]{new BlockVector(+1, 0, 0), new BlockVector(-1, 0, 0), new BlockVector(0, +1, 0), new BlockVector(0, -1, 0), new BlockVector(0, 0, +1), new BlockVector(0, 0, -1),};

    static void initialize() throws BlockTypeLoadException {
        loadBlocks("minecraft.json");
    }

    /**
     * Reads in block information from a YAML file.
     * TODO: should probably go elsewhere
     */
    public static BlockTypes loadBlocks(String filename) throws BlockTypeLoadException {
        ExceptionDialog.clearExtraStatus();
        ExceptionDialog.setExtraStatus1("Loading blocks from " + filename);

        File blockFile = new File(filename);
        InputStream input = null;
        if (!blockFile.exists()) {
            input = GuiMain.class.getResourceAsStream("/editor/world/minecraft.json");
        } else {
            try {
                input = new FileInputStream(blockFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (input == null) {
            throw new BlockTypeLoadException("Could not open minecraft.json!");
        }
        InputStreamReader isr = new InputStreamReader(input);
        JSONParser p = new JSONParser();
        JSONObject data;
        try {
            data = (JSONObject) p.parse(isr);
        } catch (IOException e) {
            throw new BlockTypeLoadException("Could not load " + filename + ": " + e, e);
        } catch (ParseException e) {
            throw new BlockTypeLoadException("Could not load " + filename + ": " + e, e);
        }
        // First load the actual JSON
        //try {
        blockTypes = new BlockTypes(data);
        /*} catch (Exception e) {
            throw new BlockTypeLoadException("Could not load " + filename + ": " + e, e);
        }
        */

        // Return the blocks that we read
        return blockTypes;
    }

    /**
     * Get the text of the key that we want to display to the user
     *
     * @return The text to display for the key
     */
    public static String getKeyEnglish(int bound_key) {
        if (Keyboard.getKeyName(bound_key).equals("GRAVE")) {
            return "`";
        } else if (Keyboard.getKeyName(bound_key).equals("SYSRQ")) {
            return "PRINTSCREEN";
        } else if (Keyboard.getKeyName(bound_key).equals("MINUS")) {
            return "-";
        } else {
            return Keyboard.getKeyName(bound_key);
        }
    }

    /**
     * Get any text which should be displayed after the key
     *
     * @return Text
     */
    public static String getKeyExtraAfter(KEY_ACTION action, int bound_key) {
        switch (action) {
            case SPEED_INCREASE:
                return " / Left Mouse Button (hold)";

            case SPEED_DECREASE:
                return " / Right Mouse Button (hold)";

            default:
                String key_name = Keyboard.getKeyName(bound_key);
                if (key_name.startsWith("NUMPAD") || key_name.equals("DECIMAL")) {
                    return " (numlock must be on)";
                } else if (key_name.equals("DIVIDE") || key_name.equals("MULTIPLY") ||
                        key_name.equals("SUBTRACT") || key_name.equals("ADD")) {
                    return " (on numeric keypad)";
                } else if (key_name.equals("GRAVE")) {
                    return " (grave accent)";
                } else if (key_name.equals("SYSRQ")) {
                    return " (also called SYSRQ)";
                } else if (key_name.equals("MINUS")) {
                    return " (minus, dash)";
                }
                break;
        }
        return "";
    }

    /**
     * Get any text which should be displayed before the key
     *
     * @return Text
     */
    public static String getKeyExtraBefore(KEY_ACTION action) {
        switch (action) {
            case QUIT:
                return "CTRL-";
        }
        return "";
    }

    /**
     * Returns a full text description of the given key
     */
    public static String getKeyFullText(KEY_ACTION action, int bound_key) {
        return getKeyExtraBefore(action) + getKeyEnglish(bound_key) + getKeyExtraAfter(action, bound_key);
    }

    public static Block getBlockType(short id) {
        return blockTypes.getBlock(id);
    }
}