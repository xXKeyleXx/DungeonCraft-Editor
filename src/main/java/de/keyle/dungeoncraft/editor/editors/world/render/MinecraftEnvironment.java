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
import de.keyle.dungeoncraft.editor.editors.world.render.dialog.ExceptionDialog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Utility class which has convenience methods to access the
 * files of the current minecraft installation
 *
 * @author Vincent Vollers
 */
public class MinecraftEnvironment {
    public static File worldViewerBaseDir;

    static {
        worldViewerBaseDir = new File(WorldViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }

    /**
     * Returns a file handle to our config file; will create the
     * directory if needed.  This will also create some other
     * subdirectories if needed.
     */
    public static File getWorldViewerConfigFile() {
        if (MinecraftEnvironment.worldViewerBaseDir.exists()) {
            if (!MinecraftEnvironment.worldViewerBaseDir.isDirectory()) {
                return null;
            }
        } else {
            if (!MinecraftEnvironment.worldViewerBaseDir.mkdir()) {
                return null;
            }
        }
        return new File(MinecraftEnvironment.worldViewerBaseDir, "dungeon-craft-editor.properties");
    }

    /**
     * Returns a stream to an arbitrary file either from our override directory,
     * the main jar, or from the user-specified texture pack.
     */
    public static InputStream getMinecraftTexturepackData(String filename) throws TextureLoadException {
        ExceptionDialog.clearExtraStatus();
        ExceptionDialog.setExtraStatus1("Loading tiles from " + filename);

        File blockFile = new File(filename);
        if (!blockFile.exists()) {
            return GuiMain.class.getResourceAsStream("/editor/world/minecraft.png");
        } else {
            try {
                return new FileInputStream(blockFile);
            } catch (FileNotFoundException e) {
                throw new TextureLoadException("Could not load " + filename + ": " + e.toString(), e);
            }
        }
    }

    /**
     * Attempts to create a bufferedImage for a stream
     *
     * @param i The inputstream of the image
     */
    public static BufferedImage buildImageFromInput(InputStream i) {
        if (i == null) {
            return null;
        }
        try {
            return ImageIO.read(i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts to create a bufferedImage containing the texture sprite sheet.  This does
     * some munging to make things a little more useful for us.  Namely:
     * <p/>
     * 1) It will attempt to colorize any biome-ready skin by first checking for green
     * pixels in the texture, and then doing some blending if it looks greyscale.
     * 2) It will colorize the redstone wire texture appropriately (due to the Beta 1.3
     * change where redstone wire color depends on how far from the source it is)
     * 3) We also copy in the water texture from misc/water.png, because many third-party
     * skins don't actually have a water graphic in the same place as the default skin
     * 4) Then we attempt to construct a passable "fire" texture from the particles file.
     * 5) Then we create a "blank" texture, for use with unknown block types
     * 6) Then we create a nether (portal) texture and an air portal texture
     * <p/>
     * TODO: it would be good to use the data values loaded from YAML to figure out where
     * to colorize, rather than hardcoding them here.
     */
    public static BufferedImage getMinecraftTexture() throws BlockTypeLoadException {
        BufferedImage bi = null;
        try {
            bi = buildImageFromInput(getMinecraftTexturepackData("minecraft.png"));
        } catch (TextureLoadException e) {
            e.printStackTrace();
        }
        if (bi == null) {
            return null;
        }
        return bi;
    }
}