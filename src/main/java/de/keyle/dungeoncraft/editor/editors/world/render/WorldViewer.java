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

import de.keyle.dungeoncraft.editor.editors.world.WorldOverview;
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.ExceptionDialog;
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.JumpDialog;
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.KeyHelpDialog;
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.WarningDialog;
import de.keyle.dungeoncraft.editor.editors.world.schematic.Schematic;
import de.keyle.dungeoncraft.editor.util.vector.OrientationVector;
import de.keyle.dungeoncraft.editor.util.vector.Region;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.*;

public class WorldViewer extends Thread {

    // number of chunks around the camera which are visible (Square)
    private int visible_chunk_range = 5;

    private static final int[] CHUNK_RANGES_KEYS = new int[6];
    private static final int[] CHUNK_RANGES = new int[]{3, 4, 5, 6, 7, 8};
    private int currentChunkRange = 4;

    // set to true when the program is finished
    private boolean done = false;

    // current display mode
    private DisplayMode displayMode;

    // last system time in the main loop (to calculate delta for camera movement)
    private long lastTime;

    // our camera
    private FirstPersonCameraController camera;
    private boolean camera_lock = false;

    // the current mouseX and mouseY on the screen
    private int mouseX;
    private int mouseY;

    // the sprite sheet for all textures
    public Texture minecraftTextures;
    public Texture loadingTextTexture;
    public Texture chunkBorderTexture;
    public Texture outOfRangeTexture;

    public double outOfRangeHeight;
    public double outOfRangeWidth;

    // Texture for screenshots
    public Texture screenshotTexture;

    protected WorldOverview worldOverviewEditor;

    public enum HIGHLIGHT_TYPE {
        DISCO("Disco", Color.GREEN.darker()),
        WHITE("White", Color.GREEN.darker()),
        REGION("Region", Color.GREEN.darker());
        public String reportText;
        public Color reportColor;

        HIGHLIGHT_TYPE(String reportText, Color reportColor) {
            this.reportText = reportText;
            this.reportColor = reportColor;
        }
    }

    private static final HIGHLIGHT_TYPE defaultHighlightRegion = HIGHLIGHT_TYPE.REGION;

    // Toggles that need to be available to the renderers
    public static class RenderToggles {
        public boolean render_water = true;
        public boolean beta19_fences = true;
        public HIGHLIGHT_TYPE highlightRegions = defaultHighlightRegion;
    }

    public static RenderToggles toggle = new RenderToggles();

    // the minecraft level we are exploring
    private MinecraftLevel level;
    private MinecraftLevel emptyLevel;

    // the current block (universal coordinate) where the camera is hovering on
    private int levelBlockX, levelBlockZ;

    // The same, but as a float, to more accurately show what Minecraft itself shows
    private float reportBlockX, reportBlockZ;

    // the current and previous chunk coordinates where the camera is hovering on
    private int currentLevelX, currentLevelZ;

    // wheter we need to reload the world
    private boolean needToReloadWorld = false;

    // the width and height of the current screen resolution
    private int screenWidth, screenHeight;

    // wheter we are done with loading the map data (just for the mini map really)
    private boolean map_load_started = false;

    // the current fps we are 'doing'
    private int fps;

    // the laste time fps was updated
    private long lastFpsTime = -1;

    // the number of frames since the last fps update
    private int framesSinceLastFps;

    // the fps display texture
    private Texture fpsTexture;

    // far too many fps calculation variables (copied this from another project)
    public long previousTime;
    public long timeDelta;
    private boolean updateFPSText;
    private long time;

    // lighting on or of (its actually fog, but hey...)
    private boolean lightMode = true;

    // level info texture
    private boolean levelInfoToggle = true;
    private Texture levelInfoTexture;
    private boolean renderDetailsToggle = true;
    private Texture renderDetailsTexture;
    private int renderDetails_w = 160;
    private int cur_renderDetails_h;
    private int levelInfoTexture_h = 144;
    private boolean regenerateRenderDetailsTexture = false;
    private boolean regenerateOutOfBoundsTexture = false;

    // light level
    private int[] lightLevelEnd = new int[]{30, 50, 70, 100, 130, 160, 190, 220};
    private int[] lightLevelStart = new int[]{0, 20, 30, 40, 60, 80, 100, 120};
    private int currentLightLevel = 2;

    // Grass rendering status
    private boolean accurateGrass = true;

    // Chunk border rendering status
    private boolean renderChunkBorders = false;

    private HashMap<KEY_ACTION, Integer> key_mapping;
    private WorldViewerProperties xray_properties;

    public boolean jump_dialog_trigger = false;
    public int open_dialog_trigger = 0;

    public static Logger logger = Logger.getLogger("XRay.class");

    private volatile boolean loadNewSchematic = false;
    private volatile boolean resize = false;
    private OrientationVector newCameraPosition = null;


    public WorldViewer(WorldOverview worldOverviewEditor) {
        this.worldOverviewEditor = worldOverviewEditor;
        this.emptyLevel = new MinecraftLevel(new Schematic(new byte[0], new byte[0], (short) 0, (short) 0, (short) 0));
    }

    // go
    public void run() {
        // First up: initialize our static datastructures in MinecraftConstants.
        // This used to happen in a static {} block, but that makes some things
        // difficult.
        try {
            MinecraftConstants.initialize();
        } catch (BlockTypeLoadException e) {
            ExceptionDialog.presentDialog("Error reading Minecraft block data", e);
            return;
        }

        // This was moved from initialize() because we want to have this variable
        // available for loadOptionStates(), which happens first.

        try {
            // Load our preferences (this includes key mappings)
            setPreferenceDefaults();
            ArrayList<String> errors = loadPreferences();
            if (errors.size() > 0) {
                StringBuilder errorText = new StringBuilder();
                errorText.append("The following errors were encountered while loading xray.properties:\n\n");
                for (String error : errors) {
                    errorText.append(" * ").append(error).append("\n");
                }
                WarningDialog.presentDialog("Errors in xray.properties", errorText.toString(), false, 600, 250);
            }

            // prompt for the resolution and initialize the window
            createWindow();

            // Save any prefs which may have changed
            savePreferences();

            // basic opengl initialization
            initGL();

            // init our program
            try {
                initialize();
            } catch (BlockTypeLoadException e) {
                ExceptionDialog.presentDialog("Error reading Minecraft block data", e);
                return;
            }

            // And now load our world
            this.setMinecraftWorld(null);

            // Render details
            updateRenderDetails();

            // main loop
            while (!done) {
                long time = Sys.getTime();
                float timeDelta = (time - lastTime) / 1000.0f;
                lastTime = time;

                // handle input given the timedelta (for mouse control)
                handleInput(timeDelta);

                // Load chunks if needed
                /*
                if (mapChunksToLoad != null) {
                    loadPendingChunks();
                }
                */

                if (loadNewSchematic) {
                    this.savePreferences();

                    // A full reinitialization is kind of overkill, but whatever.
                    // TODO: code duplicated from switchDimension
                    this.prepareNewWorld();
                    this.setMinecraftWorld(worldOverviewEditor.getSchematic());
                    Mouse.setGrabbed(true);
                    loadNewSchematic = false;
                }

                // Regenerate our rendering details window if we've been told to
                if (this.regenerateRenderDetailsTexture) {
                    updateRenderDetails();
                }
                if (this.regenerateOutOfBoundsTexture) {
                    updateOutOfBoundsTexture();
                }

                // render whatever we need to render
                render();

                // Sleep a bit if we're not visible, to save on CPU
                // This is especially important when isVisible() is false, because
                // Display.update() does NOT vSync in that case.
                if (!Display.isVisible()) {
                    Thread.sleep(100);
                } else if (!Display.isActive()) {
                    Thread.sleep(33);
                }

                // Push to screen
                Display.update();

                if (resize) {
                    displayMode = new DisplayMode(worldOverviewEditor.canvas.getWidth(), worldOverviewEditor.canvas.getHeight());
                    GL11.glViewport(0, 0, displayMode.getWidth(), displayMode.getHeight());
                    GLU.gluPerspective(90.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 400.0f);
                    resize = false;
                }

            }
            // cleanup
            saveOptionStates();
            cleanup();
        } catch (Exception e) {
            // bah, some error happened
            Mouse.setGrabbed(false);
            ExceptionDialog.clearExtraStatus();
            ExceptionDialog.presentDialog("Exception Encountered!", e);
            System.exit(0);
        }
    }

    /**
     * Loads our preferences. This also sets our default keybindings if they're
     * not overridden somewhere.
     */
    public ArrayList<String> loadPreferences() {
        xray_properties = new WorldViewerProperties();
        ArrayList<String> errors = new ArrayList<String>();
        String error;

        // First load our defaults into the prefs object
        for (KEY_ACTION action : KEY_ACTION.values()) {
            xray_properties.setProperty("KEY_" + action.toString(), Keyboard.getKeyName(this.key_mapping.get(action)));
        }

        // Here's where we would load from our prefs file
        /*
        File prefs = MinecraftEnvironment.getWorldViewerConfigFile();
        if (prefs.exists() && prefs.canRead()) {
            try {
                xray_properties.load(new FileInputStream(prefs));
            } catch (IOException e) {
                // Just report and continue
                logger.warning("Could not load configuration file: " + e.toString());
            }
        }
        */

        // Loop through the key mappings that we just loaded
        int newkey;
        String prefskey;
        for (KEY_ACTION action : KEY_ACTION.values()) {
            prefskey = xray_properties.getProperty("KEY_" + action.toString());
            if (prefskey.equalsIgnoreCase("none")) {
                // If the user actually specified "NONE" in the config file,
                // unbind the key
                newkey = Keyboard.KEY_NONE;
            } else {
                newkey = Keyboard.getKeyIndex(prefskey);
                if (newkey == Keyboard.KEY_NONE) {
                    // TODO: Should output something more visible to the user
                    error = "Key '" + prefskey + "' for action " + action + " is unknown.  Default key '" + Keyboard.getKeyName(key_mapping.get(action)) + "' assigned.";
                    logger.warning(error);
                    errors.add(error);
                    continue;
                }
            }
            this.key_mapping.put(action, newkey);
        }

        // Populate our key ranges
        int i;
        for (i = 0; i < CHUNK_RANGES.length; i++) {
            CHUNK_RANGES_KEYS[i] = this.key_mapping.get(KEY_ACTION.valueOf("CHUNK_RANGE_" + (i + 1)));
        }

        // Read in our saved option states, if we have 'em
        this.loadOptionStates();

        // Save the file immediately, in case we picked up new defaults which weren't present previously
        this.savePreferences();

        // Force our chunk-rendering distance to our selected value
        this.setChunkRange(this.currentChunkRange);

        // Return
        return errors;
    }

    /**
     * Updates our key-mapping preferences and saves out the config file.
     */
    public void updateKeyMapping() {
        for (Map.Entry<KEY_ACTION, Integer> entry : key_mapping.entrySet()) {
            xray_properties.setProperty("KEY_" + entry.getKey().toString(), Keyboard.getKeyName(entry.getValue()));
        }
        this.savePreferences();

        // It's entirely possible that we changed the key to bring up
        // the key dialog itself, so let's force a redraw of that.
        // Because this is called directly from the AWT dialog itself,
        // though, we can't call it directly.  Instead just set a boolean
        // and it'll get picked up in the mainloop.
        this.regenerateRenderDetailsTexture = true;
        this.regenerateOutOfBoundsTexture = true;
    }

    /**
     * Saves our preferences out
     */
    public void savePreferences() {
        File prefs = MinecraftEnvironment.getWorldViewerConfigFile();
        try {
            xray_properties.store(new FileOutputStream(prefs), "Feel free to edit.  Use \"NONE\" to disable an action.  Keys taken from http://www.lwjgl.org/javadoc/constant-values.html#org.lwjgl.input.Keyboard.KEY_1");
        } catch (IOException e) {
            // Just report on the console and move on
            logger.warning("Could not save preferences to file: " + e.toString());
        }
    }

    /**
     * Sets our default preferences
     */
    public void setPreferenceDefaults() {
        // First do the default key mappings
        key_mapping = new HashMap<KEY_ACTION, Integer>();
        for (KEY_ACTION action : KEY_ACTION.values()) {
            key_mapping.put(action, action.def_key);
        }
    }

    public void incLightLevel() {
        this.currentLightLevel++;
        if (this.currentLightLevel >= this.lightLevelStart.length) {
            this.currentLightLevel = this.lightLevelStart.length - 1;
        }
    }

    public void decLightLevel() {
        this.currentLightLevel--;
        if (this.currentLightLevel <= 0) {
            this.currentLightLevel = 0;
        }
    }

    public void setLightLevel() {
        this.setLightLevel(0);
    }

    public void setLightLevel(int diff) {
        if (this.currentLightLevel > (this.lightLevelStart.length - 1)) {
            this.currentLightLevel = this.lightLevelStart.length - 1;
        }
        int min = this.lightLevelStart[this.currentLightLevel];
        int max = this.lightLevelEnd[this.currentLightLevel];

        min = min + diff;
        max = max + diff;

        if (min <= 0) {
            min = 0;
        }
        if (max <= 0) {
            max = 0;
        }

        GL11.glFogf(GL11.GL_FOG_START, min);
        GL11.glFogf(GL11.GL_FOG_END, max);
    }

    /**
     * Initialize the basic openGL environment
     */
    private void initGL() {
        displayMode = new DisplayMode(worldOverviewEditor.canvas.getWidth(), worldOverviewEditor.canvas.getHeight()); //ResolutionDialog.selectedDisplayMode;

        GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        GL11.glShadeModel(GL11.GL_FLAT); // Disable Smooth Shading
        GL11.glClearColor(0.0f, 0.3f, 1.0f, 0.3f); // Blue Background
        GL11.glClearDepth(1.0); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
        // GL11.glDepthFunc(GL11.GL_ALWAYS);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(90.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 400.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

        GL11.glDisable(GL11.GL_FOG);
        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
        float[] color = new float[]{0.0f, 0.3f, 1.0f, 0.3f};
        ByteBuffer colorBytes = ByteBuffer.allocateDirect(64);
        FloatBuffer colorBuffer = colorBytes.asFloatBuffer();
        colorBuffer.rewind();
        colorBuffer.put(color);
        colorBuffer.rewind();
        GL11.glFog(GL11.GL_FOG_COLOR, colorBytes.asFloatBuffer());
        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.3f);
        GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
        setLightLevel();

    }

    /**
     * Clears out variables and textures that we'd need while loading a new world
     * (just when switching dimensions, at the moment)
     */
    private void prepareNewWorld() {
        try {
            loadingTextTexture = TextureTool.allocateTexture(1024, 64);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // level data
        levelBlockX = Integer.MIN_VALUE;
        levelBlockZ = Integer.MIN_VALUE;
    }

    /**
     * Load textures init precalc tables determine available worlds init misc
     * variables
     */
    private void initialize() throws BlockTypeLoadException {
        // camera
        //camera = new FirstPersonCameraController(-145, -120, -176);
        camera = new FirstPersonCameraController(0, 0, 0);

        // textures
        try {
            // Note that in order to avoid weird texture-resize fuzziness, these textures
            // should have dimensions which are powers of 2
            fpsTexture = TextureTool.allocateTexture(128, 32);
            levelInfoTexture = TextureTool.allocateTexture(128, 256);
            renderDetailsTexture = TextureTool.allocateTexture(256, 256);

            // minecraft textures
            BufferedImage textureImage = MinecraftEnvironment.getMinecraftTexture();
            if (textureImage != null) {
                Texture newtex = TextureTool.allocateTexture(textureImage, GL11.GL_NEAREST);
                newtex.update();
                minecraftTextures = newtex;
                MinecraftConstants.TEX_Y = 1.0f / textureImage.getHeight() / 16f;
            }

            // Chunk border texture
            int chunkBorderWidth = 256;
            int chunkBorderHeight = 2048;
            int stripeheight = 64;
            BufferedImage chunkBorderImage = new BufferedImage(chunkBorderWidth, chunkBorderHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = chunkBorderImage.createGraphics();
            g2d.setColor(new Color(0f, 0f, 0f, .4f));
            g2d.fillRect(0, 0, chunkBorderWidth, chunkBorderHeight);
            g2d.setColor(new Color(1f, 1f, 1f, .05f));
            for (int y = stripeheight; y < chunkBorderHeight; y += (stripeheight * 2)) {
                g2d.fillRect(0, y, chunkBorderWidth, stripeheight);
            }
            g2d.setColor(new Color(1f, 1f, 1f, .8f));
            g2d.drawRect(0, 0, chunkBorderWidth - 1, chunkBorderHeight - 1);
            chunkBorderTexture = TextureTool.allocateTexture(chunkBorderImage, GL11.GL_NEAREST);
            chunkBorderTexture.update();

            this.updateOutOfBoundsTexture();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Extra things we have to do
        this.prepareNewWorld();

        // set mouse grabbed so we can get x/y coordinates
        Mouse.setGrabbed(true);

        // Disable repeat key events
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Creates the window and initializes the lwjgl display object
     *
     * @throws Exception
     */
    private void createWindow() throws Exception {
        displayMode = new DisplayMode(worldOverviewEditor.canvas.getWidth(), worldOverviewEditor.canvas.getHeight());
        Display.setVSyncEnabled(true);
        Display.setResizable(true);
        Display.setParent(worldOverviewEditor.canvas);
        Display.create();
        screenWidth = worldOverviewEditor.canvas.getWidth();
        screenHeight = worldOverviewEditor.canvas.getHeight();
    }

    private void setChunkRange(int n) {
        if (n >= CHUNK_RANGES.length) {
            n = CHUNK_RANGES.length - 1;
        }
        if (n <= 0) {
            n = 0;
        }
        if (n != currentChunkRange) {
            this.needToReloadWorld = true;
        }
        this.currentChunkRange = n;
        this.visible_chunk_range = CHUNK_RANGES[n];
    }

    /**
     * Sets the world number we want to view
     *
     * @param schematic
     */
    private void setMinecraftWorld(Schematic schematic) {
        if (schematic == null) {
            this.level = emptyLevel;
            return;
        }
        this.level = new MinecraftLevel(schematic);

        // determine which chunks are available in this world

        moveCameraToSpawnPoint();
    }

    /**
     * Take a screenshot of our current map, so that we can draw it "behind"
     * our loading map dialog.  This is a bit silly, really, but I do think it
     * looks nicer.
     * TODO: Duplicated code from loadPendingChunks()
     * TODO: For some reason "boxBy" here has to be 40 pixels larger than its
     * equivalent inside loadPendingChunks()
     */
    private void takeLoadingBoxScreenshot() {
        float bx = 100;
        float ex = screenWidth - 100;
        float by = (screenHeight / 2.0f) - 50;
        float boxBx = bx - 20;
        float boxBy = by - 80;
        int boxWidth = (int) ex - (int) bx + 40;
        int boxHeight = 300;

        GL11.glReadBuffer(GL11.GL_FRONT);
        ByteBuffer buffer = BufferUtils.createByteBuffer(boxWidth * boxHeight * 4);
        GL11.glReadPixels((int) boxBx, (int) boxBy, boxWidth, boxHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        BufferedImage screenshotImage = new BufferedImage(boxWidth, boxHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < boxWidth; x++) {
            for (int y = 0; y < boxHeight; y++) {
                int i = (x + (boxWidth * y)) * 4;
                int rVal = buffer.get(i) & 0xFF;
                int gVal = buffer.get(i + 1) & 0xFF;
                int bVal = buffer.get(i + 2) & 0xFF;
                screenshotImage.setRGB(x, boxHeight - (y + 1), (0xFF << 24) | (rVal << 16) | (gVal << 8) | bVal);
            }
        }
        try {
            this.screenshotTexture = TextureTool.allocateTexture(screenshotImage, GL11.GL_NEAREST);
            this.screenshotTexture.update();
        } catch (IOException e) {
            this.screenshotTexture = null;
        }
    }

    private void moveCameraToPosition(OrientationVector playerPos) {
        this.takeLoadingBoxScreenshot();
        this.camera.getPosition().set((float) -playerPos.getX(), (float) -playerPos.getY(), (float) -playerPos.getZ());
        this.camera.setYawAndPitch(180 + (float) playerPos.getYaw(), (float) playerPos.getPitch());
    }

    public synchronized void setCameraPosition(OrientationVector pos) {
        newCameraPosition = pos;
    }

    public void openNewSchematic() {
        loadNewSchematic = true;
    }

    public void resize() {
        resize = true;
    }

    private void launchJumpDialog() {
        Mouse.setGrabbed(false);
        JumpDialog.presentDialog("Choose a New Position", this);
    }

    private void launchKeyHelpDialog() {
        Mouse.setGrabbed(false);
        KeyHelpDialog.presentDialog(key_mapping, this);
    }

    /**
     * Moves the camera to the position specified by the JumpDialog.
     */
    private void moveCameraToArbitraryPosition() {
        int x = JumpDialog.selectedX;
        int z = JumpDialog.selectedZ;
        if (JumpDialog.selectedChunk) {
            x = x * 16;
            z = z * 16;
        }
        this.jump_dialog_trigger = false;
        this.moveCameraToPosition(new OrientationVector(-x, (int) camera.getPosition().y, -z, camera.getYaw() - 180, camera.getPitch()));
        Mouse.setGrabbed(true);
    }

    private void moveCameraToSpawnPoint() {
        this.moveCameraToPosition(new OrientationVector(0, 0, 0, 0, 0));
    }

    /**
     * handles all input on all screens
     *
     * @param timeDelta
     */
    private void handleInput(float timeDelta) {

        int key;

        // distance in mouse movement from the last getDX() call.
        mouseX = Mouse.getDX();
        // distance in mouse movement from the last getDY() call.
        mouseY = Mouse.getDY();

        // we are on the main world screen or the level loading screen update the camera (but only if the mouse is grabbed)
        if (Mouse.isGrabbed()) {
            camera.incYaw(mouseX * MOUSE_SENSITIVITY);
            camera.incPitch(-mouseY * MOUSE_SENSITIVITY);
        }

        //
        // Keyboard commands (well, and mouse presses)
        // First up: "continual" commands which we're just using isKeyDown for
        //

        // Speed shifting
        if (Mouse.isButtonDown(0) || Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.SPEED_INCREASE))) {
            MOVEMENT_SPEED = 30.0f;
        } else if (Mouse.isButtonDown(1) || Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.SPEED_DECREASE))) {
            MOVEMENT_SPEED = 3.0f;
        } else {
            MOVEMENT_SPEED = 10.0f;
        }

        // Move forward
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_FORWARD))) {
            camera.walkForward(MOVEMENT_SPEED * timeDelta, camera_lock);
        }

        // Move backwards
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_BACKWARD))) {
            camera.walkBackwards(MOVEMENT_SPEED * timeDelta, camera_lock);
        }

        // Strafe Left
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_LEFT))) {
            camera.strafeLeft(MOVEMENT_SPEED * timeDelta);
        }

        // Strafe right
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_RIGHT))) {
            camera.strafeRight(MOVEMENT_SPEED * timeDelta);
        }

        // Fly Up
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_UP))) {
            camera.moveUp(MOVEMENT_SPEED * timeDelta);
        }

        // Fly Down
        if (Keyboard.isKeyDown(key_mapping.get(KEY_ACTION.MOVE_DOWN))) {
            camera.moveUp(-MOVEMENT_SPEED * timeDelta);
        }

        //
        // And now, keys that were meant to just be hit once and do their thing
        //
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                key = Keyboard.getEventKey();

                if (key == key_mapping.get(KEY_ACTION.TOGGLE_FULLBRIGHT)) {
                    // Toggle fullbright
                    setLightMode(!lightMode);
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_REGION_HIGHLIGHTING)) {
                    // Toggle ore highlighting
                    boolean found = false;
                    boolean set = false;
                    for (HIGHLIGHT_TYPE type : HIGHLIGHT_TYPE.values()) {
                        if (type == toggle.highlightRegions) {
                            found = true;
                        } else if (found) {
                            toggle.highlightRegions = type;
                            set = true;
                            break;
                        }
                    }
                    if (!set) {
                        toggle.highlightRegions = HIGHLIGHT_TYPE.DISCO;
                    }
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.MOVE_TO_SPAWN)) {
                    // Move camera to spawn point
                    moveCameraToSpawnPoint();
                } else if (key == key_mapping.get(KEY_ACTION.LIGHT_INCREASE)) {
                    // Increase light level
                    incLightLevel();
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.LIGHT_DECREASE)) {
                    // Decrease light level
                    decLightLevel();
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_POSITION_INFO)) {
                    // Toggle position info popup
                    levelInfoToggle = !levelInfoToggle;
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_RENDER_DETAILS)) {
                    // Toggle rendering info popup
                    renderDetailsToggle = !renderDetailsToggle;
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_WATER)) {
                    // Toggle water rendering
                    toggle.render_water = !toggle.render_water;
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_CAMERA_LOCK)) {
                    // Toggle camera lock
                    camera_lock = !camera_lock;
                    updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.TOGGLE_CHUNK_BORDERS)) {
                    renderChunkBorders = !renderChunkBorders;
                    // I think this one should be obvious enough not to bother with wording in the info box
                    //updateRenderDetails();
                } else if (key == key_mapping.get(KEY_ACTION.RELEASE_MOUSE)) {
                    // Release the mouse
                    Mouse.setGrabbed(false);
                } else if (key == key_mapping.get(KEY_ACTION.QUIT)) {
                    // Quit
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        done = true;
                    }
                }
                /*
                else if (key == Keyboard.KEY_P)
				{
					// Temp routine to write the minimap out to a PNG (for debugging purposes)
					BufferedImage bi = minimapTexture.getImage();
					try {
						ImageIO.write(bi, "PNG", new File("/home/pez/xray.png"));
						logger.info("Wrote minimap to disk.");
					}
					catch (Exception e)
					{
						// whatever
					}
				}
				 */
                else {
                    // Handle changing chunk ranges (how far out we draw from the camera
                    for (int i = 0; i < CHUNK_RANGES.length; i++) {
                        if (key == CHUNK_RANGES_KEYS[i]) {
                            setChunkRange(i);
                            updateRenderDetails();
                        }
                    }
                }
            } else {
                // Here are keys which we process once they're RELEASED.  The reason for
                // this is because if we handle it and launch the dialog on the key
                // PRESS event, it's the new dialog which receives the key-release
                // event, not the main window, so the LWJGL context doesn't know
                // about the release and believes that the key is being perpetually
                // pressed (at least, until the key is pressed again).

                key = Keyboard.getEventKey();

                if (key == key_mapping.get(KEY_ACTION.JUMP)) {
                    // Launch the Jump dialog
                    launchJumpDialog();
                } else if (key == key_mapping.get(KEY_ACTION.KEY_HELP)) {
                    // Launch the dialog
                    launchKeyHelpDialog();
                } else if (key == key_mapping.get(KEY_ACTION.JUMP_NEAREST)) {
                    // Jump to the nearest actually-loaded chunk
                    // This actually only launches a dialog if there's no map
                    // data anywhere in our dir.
                    jumpToNearestLoaded();
                }
            }
        }

        // Grab the mouse on a click
        if (Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        }

        // Handle a requested window close
        if (Display.isCloseRequested()) {
            done = true;
        }

        // check to see if we should be jumping to a new position
        if (this.jump_dialog_trigger) {
            moveCameraToArbitraryPosition();
        }
    }

    /**
     * Jump to the nearest chunk that actually has data.  We're playing some stupid games with
     * JumpDialog to do this.
     * TODO: Stop playing stupid games with JumpDialog.
     */
    private void jumpToNearestLoaded() {
        if (level == emptyLevel) {
            JumpDialog.selectedX = 0;
            JumpDialog.selectedZ = 0;
            this.moveCameraToArbitraryPosition();
        }
    }

    private void setLightMode(boolean lightMode) {
        this.lightMode = lightMode;
        if (lightMode) {
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
            GL11.glEnable(GL11.GL_FOG);
        } else {
            GL11.glClearColor(0.0f, 0.3f, 1.0f, 0.3f); // Blue Background
            GL11.glDisable(GL11.GL_FOG);
        }
    }

    /**
     * Main render loop
     */
    private void render() {
        // GL11.glLoadIdentity();
        GL11.glLoadIdentity();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

        // are we still loading the map?
        if (!map_load_started) {
            map_load_started = true;
            // drawMapMarkersToMinimap();
            // minimapTexture.update();
            setLightMode(lightMode); // basically enable fog etc
        }

        // we are viewing a world
        GL11.glPushMatrix();

        // change the camera to point a the right direction
        if (newCameraPosition == null) {
            camera.applyCameraTransformation();
        } else {
            moveCameraToPosition(newCameraPosition);
            newCameraPosition = null;
        }

        float currentCameraPosX = -camera.getPosition().x;
        float currentCameraPosZ = -camera.getPosition().z;
        reportBlockX = currentCameraPosX + .5f;
        reportBlockZ = currentCameraPosZ + .5f;

        // Now do various passes
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        int last_tex = -1;
        int i;

        // Now our regular blocks

        //ToDo get texture id
        ChunkSchematic c = level.getChunk();
        minecraftTextures.bind();
        c.renderWorld(camera, true);
        c.renderWorld(camera, false);

        // Now chunk borders
        if (renderChunkBorders) {
            chunkBorderTexture.bind();
            c.renderBorder();
        }

        // And now, if we're highlighting regions, highlight them.
        if (worldOverviewEditor.showRegions()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            switch (toggle.highlightRegions) {
                // Old-style; at least one person prefers it
                case WHITE:
                    long time = System.currentTimeMillis();
                    float alpha = (time % 1000) / 1000.0f;
                    if (time % 2000 > 1000) {
                        alpha = 1.0f - alpha;
                    }
                    alpha = 0.1f + (alpha * 0.8f);
                    GL11.glColor4f(alpha, alpha, alpha, alpha);
                    break;

                // New style disco-y highlighting
                case DISCO:
                    float timeidx = (System.currentTimeMillis() % 1000) * 6.28318f / 1000.0f;
                    float red = (float) Math.sin(timeidx) * .5f + .5f;
                    float green = (float) Math.sin(timeidx + 2.09439f) * .5f + .5f;
                    float blue = (float) Math.sin(timeidx + 4.18878f) * .5f + .5f;
                    GL11.glColor4f(red, green, blue, 1f);
                    break;
            }
            setLightLevel(20);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            chunkBorderTexture.bind();
            java.util.List<Region> selectedRegions = new ArrayList<Region>(worldOverviewEditor.getSelectedRegions());
            for (Region region : selectedRegions) {
                if (toggle.highlightRegions == HIGHLIGHT_TYPE.REGION) {
                    GL11.glColor4f(region.getColor().getRed(), region.getColor().getGreen(), region.getColor().getBlue(), 1f);
                }
                renderRegion(region);
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            last_tex = -1;
        }

        setLightLevel();

        GL11.glPopMatrix();

        /*
        // Stuff
        if (currChunk == null) {
            int x = (int) (Display.getWidth() - outOfRangeWidth) / 2;
            // TODO: "104" comes from barHeight*2-20 from drawMineralToggle(), should be controlled
            // with constants
            int y = Display.getHeight() - (int) outOfRangeHeight - 104;
            setOrthoOn(); // 2d mode
            this.drawBgBox((float) x, (float) y, (float) outOfRangeWidth, (float) outOfRangeHeight);
            SpriteTool.drawSpriteAbsoluteXY(outOfRangeTexture, x, y);
            setOrthoOff(); // back to 3d mode
        }
        */

        // draw the user interface (fps and map)
        drawUI();
    }

    public void renderRegion(Region region) {
        float xMin = region.getMin().getBlockX();
        float yMin = region.getMin().getBlockY();
        float zMin = region.getMin().getBlockZ();
        float xMax = region.getMax().getBlockX() + 1;
        float yMax = region.getMax().getBlockY() + 1;
        float zMax = region.getMax().getBlockZ() + 1;
        Renderer.renderNonstandardVertical(0, 0, 1, 1, xMin, yMax, zMin, xMax, yMin, zMin);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, xMin, yMax, zMax, xMax, yMin, zMax);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, xMin, yMax, zMin, xMin, yMin, zMax);
        Renderer.renderNonstandardVertical(0, 0, 1, 1, xMax, yMax, zMin, xMax, yMin, zMax);
        Renderer.renderNonstandardHorizontal(0, 0, 1, 1, xMin, zMin, xMax, zMax, yMax);
        Renderer.renderNonstandardHorizontal(0, 0, 1, 1, xMin, zMin, xMax, zMax, yMin);
    }

    /**
     * Draw the ui
     */
    private void drawUI() {
        framesSinceLastFps++;

        setOrthoOn(); // 2d mode

        drawFPSCounter();
        if (levelInfoToggle) {
            drawLevelInfo();
        }
        if (renderDetailsToggle) {
            drawRenderDetails();
        }

        setOrthoOff(); // back to 3d mode
    }

    private void updateLevelInfo() {
        int labelX = 5;
        int valueX = 70;
        Graphics2D g = levelInfoTexture.getImage().createGraphics();
        g.setBackground(Color.BLUE);
        g.clearRect(0, 0, 128, levelInfoTexture_h);
        g.setColor(Color.WHITE);
        g.fillRect(2, 2, 124, levelInfoTexture_h - 4);
        g.setFont(ARIALFONT);
        int chunkX = MinecraftLevel.getChunkX(levelBlockX);
        int chunkZ = MinecraftLevel.getChunkZ(levelBlockZ);
        g.setColor(Color.BLACK);
        g.drawString("Chunk X:", labelX, 22);
        g.setColor(Color.RED.darker());
        g.drawString(Integer.toString(chunkX), valueX, 22);

        g.setColor(Color.BLACK);
        g.drawString("Chunk Z:", labelX, 22 + 16);
        g.setColor(Color.RED.darker());
        g.drawString(Integer.toString(chunkZ), valueX, 22 + 16);

        g.setColor(Color.BLACK);
        g.drawString("World X:", labelX, 22 + 32);
        g.setColor(Color.RED.darker());
        g.drawString(String.format("%.1f", reportBlockX), valueX, 22 + 32);

        g.setColor(Color.BLACK);
        g.drawString("World Z:", labelX, 22 + 16 + 32);
        g.setColor(Color.RED.darker());
        g.drawString(String.format("%.1f", reportBlockZ), valueX, 22 + 16 + 32);

        g.setColor(Color.BLACK);
        g.drawString("World Y:", labelX, 22 + 16 + 32 + 16);
        g.setColor(Color.RED.darker());
        g.drawString(String.format("%.1f", (-camera.getPosition().y + .5f)), valueX, 22 + 16 + 32 + 16);

        long heapSize = Runtime.getRuntime().totalMemory();
        g.setColor(Color.BLACK);
        g.drawString("Memory Used", labelX, 22 + 16 + 32 + 16 + 25);
        g.setColor(Color.RED.darker());
        g.drawString(Integer.toString((int) (heapSize / 1024 / 1024)) + " MB", 20, 22 + 16 + 32 + 16 + 25 + 20);

        levelInfoTexture.update();
    }

    /**
     * Renders a text label in an info box, with differing fonts/colors for the
     * label and its value
     *
     * @param g          Graphics context to render to
     * @param x          Baseline x offset for the label
     * @param y          Baseline y offset for the label
     * @param label      The label to draw
     * @param labelColor Label color
     * @param labelFont  Label font
     * @param value      The value
     * @param valueColor Value color
     * @param valueFont  Value font
     */
    private void infoboxTextLabel(Graphics2D g, int x, int y, String label, Color labelColor, Font labelFont, String value, Color valueColor, Font valueFont) {
        Rectangle2D bounds = labelFont.getStringBounds(label, g.getFontRenderContext());
        g.setColor(labelColor);
        g.setFont(labelFont);
        g.drawString(label, x, y);
        g.setColor(valueColor);
        g.setFont(valueFont);
        g.drawString(value, (int) (x + bounds.getWidth()), y);
    }


    /**
     * Renders a note in the info box, centered.  Note that for the centering, this routine
     * will assume a border of "x" on each side, so it'll only work properly if the infobox
     * is right at the edge of the screen.
     *
     * @param g          Graphics context to render to
     * @param x          Baseline x offset for the label
     * @param y          Baseline y offset for the label
     * @param label      The label to draw
     * @param labelColor Label color
     * @param labelFont  Label font
     */
    private void infoboxTextNote(Graphics2D g, int x, int y, String label, Color labelColor, Font labelFont) {
        Rectangle2D bounds = labelFont.getStringBounds(label, g.getFontRenderContext());
        g.setColor(labelColor);
        g.setFont(labelFont);
        g.drawString(label, x + (int) ((renderDetails_w - (x * 2) - bounds.getWidth()) / 2), y);
    }

    /**
     * Renders a slider-type graphic in an info box, including its label
     *
     * @param g              Graphics context to render to
     * @param x              Baseline X offset for the label
     * @param y              Baseline Y offset for the label
     * @param label          The label
     * @param labelColor     Label color
     * @param labelFont      Label font
     * @param line_h         How tall our individual lines are
     * @param slider_start_x X offset to start the slider at
     * @param curval         Current value of slider
     * @param val_length     Length of slider data (array length, for us)
     */
    private void infoboxSlider(Graphics2D g, int x, int y, String label, Color labelColor, Font labelFont, int line_h, int slider_start_x, int curval, int val_length) {
        int slider_top_y = y - line_h + 10;
        int slider_h = 8;
        int slider_end_x = renderDetails_w - 8;
        // We have a cast to float in there because otherwise rounding errors can pile up
        int marker_x = slider_start_x + (int) (curval * ((slider_end_x - slider_start_x) / (float) (val_length - 1)));

        // Label
        g.setColor(labelColor);
        g.setFont(labelFont);
        g.drawString(label, x, y);

        // Slider Base
        g.setColor(Color.BLACK);
        g.drawRect(slider_start_x, slider_top_y, slider_end_x - slider_start_x, slider_h);

        // Slider Location
        g.setColor(Color.RED);
        g.fillRect(marker_x, y - line_h + 8, 3, 13);
    }

    /**
     * Update our render-details infobox
     */
    private void updateRenderDetails() {
        int line_h = 20;
        int x_off = 5;
        int line_count = 0;
        Graphics2D g = renderDetailsTexture.getImage().createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, renderDetails_w, renderDetailsTexture.getTextureWidth());
        g.setFont(DETAILFONT);
        g.setColor(Color.BLACK);
        if (!lightMode) {
            line_count++;
            infoboxTextLabel(g, x_off, line_count * line_h, "Fullbright: ", Color.BLACK, DETAILFONT, "On", Color.GREEN.darker(), DETAILVALUEFONT);
        } else {
            line_count++;
            infoboxSlider(g, x_off, line_count * line_h, "Light Level:", Color.BLACK, DETAILFONT, line_h, 90, currentLightLevel, lightLevelEnd.length);
        }
        line_count++;
        infoboxSlider(g, x_off, line_count * line_h, "Render Dist:", Color.BLACK, DETAILFONT, line_h, 90, currentChunkRange, CHUNK_RANGES.length);
        line_count++;
        infoboxTextLabel(g, x_off, line_count * line_h, "Region Highlight: ", Color.BLACK, DETAILFONT, toggle.highlightRegions.reportText, toggle.highlightRegions.reportColor, DETAILVALUEFONT);
        if (!toggle.render_water) {
            line_count++;
            infoboxTextLabel(g, x_off, line_count * line_h, "Water: ", Color.BLACK, DETAILFONT, "Off", Color.RED.darker(), DETAILVALUEFONT);
        }
        if (!accurateGrass) {
            line_count++;
            infoboxTextLabel(g, x_off, line_count * line_h, "Grass: ", Color.BLACK, DETAILFONT, "Inaccurate", Color.RED.darker(), DETAILVALUEFONT);
        }
        if (camera_lock) {
            line_count++;
            infoboxTextLabel(g, x_off, line_count * line_h, "Vertical Lock: ", Color.BLACK, DETAILFONT, "On", Color.green.darker(), DETAILVALUEFONT);
        }
        if (!toggle.beta19_fences) {
            line_count++;
            infoboxTextLabel(g, x_off, line_count * line_h, "\"New\" Fences: ", Color.BLACK, DETAILFONT, "Off", Color.green.darker(), DETAILVALUEFONT);
        }

        // Add a note about our keyboard reference, since we have that now.
        if (key_mapping.get(KEY_ACTION.KEY_HELP) != Keyboard.KEY_NONE) {
            line_count++;
            infoboxTextNote(g, x_off, line_count * line_h, "Keyboard Reference: " + Keyboard.getKeyName(key_mapping.get(KEY_ACTION.KEY_HELP)), Color.BLACK, SMALLFONT);
        }

        cur_renderDetails_h = (line_count + 1) * line_h - 8;
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2));
        g.drawRect(1, 1, renderDetails_w - 2, cur_renderDetails_h - 2);
        renderDetailsTexture.update();

        this.regenerateRenderDetailsTexture = false;
    }

    /**
     * Regenerates the texture we use when the camera goes outside of our actual chunks
     */
    private void updateOutOfBoundsTexture() {
        try {
            // Out of Range texture
            Font outFont = DETAILVALUEFONT;
            BufferedImage outOfRangeImage = new BufferedImage(1024, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = outOfRangeImage.createGraphics();
            int key = this.key_mapping.get(KEY_ACTION.JUMP_NEAREST);
            String message = "You are out of the existing map area.";
            String message2;
            if (key != Keyboard.KEY_NONE) {
                message2 = "Press '" + MinecraftConstants.getKeyFullText(KEY_ACTION.JUMP_NEAREST, key) + "' to jump to the nearest valid chunk.";
            } else {
                key = this.key_mapping.get(KEY_ACTION.JUMP);
                if (key != Keyboard.KEY_NONE) {
                    message2 = "Press '" + MinecraftConstants.getKeyFullText(KEY_ACTION.JUMP, key) + "' to jump to any arbitrary location.";
                } else {
                    message2 = "Assign a key to the 'Jump Nearest' action for an easy way to jump to the nearest valid chunk.";
                }
            }
            Rectangle2D bounds = outFont.getStringBounds(message, g2d.getFontRenderContext());
            Rectangle2D bounds2 = outFont.getStringBounds(message2, g2d.getFontRenderContext());
            // We're assuming that the first string is shorter than the second.
            outOfRangeHeight = bounds.getHeight() + bounds2.getHeight() + 15;
            outOfRangeWidth = bounds2.getWidth() + 10;

            g2d.setFont(outFont);
            g2d.setColor(new Color(255, 100, 100));
            g2d.drawString(message, 5 + (int) (bounds2.getWidth() - bounds.getWidth()) / 2, (int) (bounds.getHeight() + 5));
            g2d.drawString(message2, 5, (int) (outOfRangeHeight - 10));

            outOfRangeTexture = TextureTool.allocateTexture(outOfRangeImage, GL11.GL_NEAREST);
            outOfRangeTexture.update();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make sure we only do this once, when we're told to.
        this.regenerateOutOfBoundsTexture = false;
    }

    /**
     * Draws our level info dialog to the screen
     */
    private void drawLevelInfo() {
        int y = 48;
        if (renderDetailsToggle) {
            y += cur_renderDetails_h + 16;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        levelInfoTexture.bind();
        SpriteTool.drawCurrentSprite(0, y, 128, levelInfoTexture_h, 0, 0, 1f, levelInfoTexture_h / 256f);
    }

    /**
     * Draws our rendering details infobox to the screen
     */
    private void drawRenderDetails() {
        renderDetailsTexture.bind();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, .7f);
        SpriteTool.drawCurrentSprite(0, 48, renderDetails_w, cur_renderDetails_h, 0, 0, renderDetails_w / 256f, cur_renderDetails_h / 256f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
    }

    /**
     * Draws a 2d GL box over which we can show some info which might be
     * difficult to make out otherwise
     *
     * @param bgX      X coordinate to draw to
     * @param bgY      Y coordinate to draw to
     * @param bgWidth  Width of the box
     * @param bgHeight Height of the box
     */
    private void drawBgBox(float bgX, float bgY, float bgWidth, float bgHeight) {
        this.drawBgBox(bgX, bgY, bgWidth, bgHeight, true);
    }

    /**
     * Draws a 2d GL box over which we can show some info which might be
     * difficult to make out otherwise
     *
     * @param bgX      X coordinate to draw to
     * @param bgY      Y coordinate to draw to
     * @param bgWidth  Width of the box
     * @param bgHeight Height of the box
     * @param flipTex  Whether to toggle 2D Textures or not
     */
    private void drawBgBox(float bgX, float bgY, float bgWidth, float bgHeight, boolean flipTex) {
        GL11.glColor4f(0f, 0f, 0f, .6f);
        if (flipTex) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(bgX, bgY, 0.0f);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(bgWidth, 0);
        GL11.glVertex2f(0, bgHeight);
        GL11.glVertex2f(bgWidth, bgHeight);
        GL11.glEnd();
        GL11.glColor4f(.4f, .4f, .4f, .9f);
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(bgWidth, 0);
        GL11.glVertex2f(bgWidth, bgHeight);
        GL11.glVertex2f(0, bgHeight);
        GL11.glEnd();
        GL11.glPopMatrix();
        if (flipTex) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    /**
     * Draws a simple fps counter on the top-left of the screen
     * <p/>
     * TODO: rather than do our thing with lastFpsTime, etc, compare the values that
     * we'll be showing, and update whenever we need to (so we get immediate feeback,
     * instead of delayed feedback)
     */
    private void drawFPSCounter() {
        previousTime = time;
        time = System.nanoTime();
        timeDelta = time - previousTime;

        if (time - lastFpsTime > NANOSPERSECOND) {
            fps = framesSinceLastFps;
            framesSinceLastFps = 0;
            lastFpsTime = time;
            updateFPSText = true;
        }
        if (updateFPSText) {
            if (levelInfoToggle) {
                updateLevelInfo();
            }
            Graphics2D g = fpsTexture.getImage().createGraphics();
            g.setBackground(Color.BLUE);
            g.clearRect(0, 0, 128, 32);
            g.setColor(Color.WHITE);
            g.fillRect(2, 2, 124, 28);
            g.setColor(Color.BLACK);
            g.setFont(ARIALFONT);
            g.drawString("FPS:", 10, 22);
            g.setColor(Color.RED.darker());
            g.drawString(Integer.toString(fps), 60, 22);

            fpsTexture.update();

            updateFPSText = false;
        }

        fpsTexture.bind();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
        SpriteTool.drawSpriteAbsoluteXY(fpsTexture, 0, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
    }

    /**
     * Sets ortho (2d) mode
     */
    public void setOrthoOn() {
        // prepare projection matrix to render in 2D
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix(); // preserve perspective view
        GL11.glLoadIdentity(); // clear the perspective matrix
        GL11.glOrtho( // turn on 2D mode
                // //viewportX,viewportX+viewportW, // left, right
                // //viewportY,viewportY+viewportH, // bottom, top !!!
                0, screenWidth, // left, right
                screenHeight, 0, // bottom, top
                -500, 500); // Zfar, Znear
        // clear the modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix(); // Preserve the Modelview Matrix
        GL11.glLoadIdentity(); // clear the Modelview Matrix
        // disable depth test so further drawing will go over the current scene
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Restore the previous mode
     */
    public static void setOrthoOff() {
        // restore the original positions and views
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        // turn Depth Testing back on
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Returns our camera object
     */
    public FirstPersonCameraController getCamera() {
        return camera;
    }

    /**
     * Saves our current option states to our properties file.  Note that our
     * sphere variables are actually set in the sphere-toggling functions, rather
     * than here, because they only actually apply on a per-world basis.
     */
    private void saveOptionStates() {
        xray_properties.setBooleanProperty("STATE_WATER", toggle.render_water);
        xray_properties.setBooleanProperty("STATE_BETA19_FENCES", toggle.beta19_fences);
        xray_properties.setBooleanProperty("STATE_CAMERA_LOCK", camera_lock);
        xray_properties.setBooleanProperty("STATE_LIGHTING", lightMode);
        xray_properties.setBooleanProperty("STATE_LEVEL_INFO", levelInfoToggle);
        xray_properties.setBooleanProperty("STATE_RENDER_DETAILS", renderDetailsToggle);
        xray_properties.setBooleanProperty("STATE_ACCURATE_GRASS", accurateGrass);
        xray_properties.setBooleanProperty("STATE_CHUNK_BORDERS", renderChunkBorders);
        xray_properties.setIntProperty("STATE_CHUNK_RANGE", currentChunkRange);
        xray_properties.setIntProperty("STATE_LIGHT_LEVEL", currentLightLevel);
        savePreferences();
    }

    /**
     * Loads our option states from the properties object.  Note that we do NOT load in
     * our sphere state in here, because we need to process those every time a new world
     * is loaded, not when the application starts up.
     */
    private void loadOptionStates() {
        toggle.render_water = xray_properties.getBooleanProperty("STATE_WATER", toggle.render_water);
        toggle.beta19_fences = xray_properties.getBooleanProperty("STATE_BETA19_FENCES", toggle.beta19_fences);
        camera_lock = xray_properties.getBooleanProperty("STATE_CAMERA_LOCK", camera_lock);
        lightMode = xray_properties.getBooleanProperty("STATE_LIGHTING", lightMode);
        levelInfoToggle = xray_properties.getBooleanProperty("STATE_LEVEL_INFO", levelInfoToggle);
        renderDetailsToggle = xray_properties.getBooleanProperty("STATE_RENDER_DETAILS", renderDetailsToggle);
        accurateGrass = xray_properties.getBooleanProperty("STATE_ACCURATE_GRASS", accurateGrass);
        renderChunkBorders = xray_properties.getBooleanProperty("STATE_CHUNK_BORDERS", renderChunkBorders);
        currentChunkRange = xray_properties.getIntProperty("STATE_CHUNK_RANGE", currentChunkRange);
        currentLightLevel = xray_properties.getIntProperty("STATE_LIGHT_LEVEL", currentLightLevel);
    }

    /**
     * cleanup
     */
    private void cleanup() {
        JumpDialog.closeDialog();
        KeyHelpDialog.closeDialog();
        Display.destroy();
    }
}