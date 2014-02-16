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

import com.google.common.io.Files;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DependencyDownloader extends Thread {

    private static final String LWJGL_VERSION = "2.9.1";

    private static final String LWJGL_LINK = "http://search.maven.org/remotecontent?filepath=org/lwjgl/lwjgl/lwjgl/" + LWJGL_VERSION + "/lwjgl-" + LWJGL_VERSION + ".jar";
    private static final String LWJGL_UTIL_LINK = "http://search.maven.org/remotecontent?filepath=org/lwjgl/lwjgl/lwjgl_util/" + LWJGL_VERSION + "/lwjgl_util-" + LWJGL_VERSION + ".jar";
    private static final String LWJGL_NATIVE_WINDOWS_LINK = "http://search.maven.org/remotecontent?filepath=org/lwjgl/lwjgl/lwjgl-platform/" + LWJGL_VERSION + "/lwjgl-platform-" + LWJGL_VERSION + "-natives-windows.jar";
    private static final String LWJGL_NATIVE_LINUX_LINK = "http://search.maven.org/remotecontent?filepath=org/lwjgl/lwjgl/lwjgl-platform/" + LWJGL_VERSION + "/lwjgl-platform-" + LWJGL_VERSION + "-natives-linux.jar";
    private static final String LWJGL_NATIVE_MAC_LINK = "http://search.maven.org/remotecontent?filepath=org/lwjgl/lwjgl/lwjgl-platform/" + LWJGL_VERSION + "/lwjgl-platform-" + LWJGL_VERSION + "-natives-osx.jar";

    private final JProgressBar progressBar;
    private final JButton restartButton;
    private File libFolder;

    public DependencyDownloader(JProgressBar progressBar, JButton restartButton) {
        this.progressBar = progressBar;
        this.restartButton = restartButton;
        File editorFolder = new File(WorldOverview.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
        libFolder = new File(editorFolder, "libs");
        libFolder.mkdirs();
    }

    public void run() {
        progressBar.setValue(10);
        String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            downloadAndExtract(LWJGL_NATIVE_MAC_LINK);
        } else if (os.contains("Windows")) {
            downloadAndExtract(LWJGL_NATIVE_WINDOWS_LINK);
        } else {
            downloadAndExtract(LWJGL_NATIVE_LINUX_LINK);
        }
        progressBar.setValue(40);
        download(LWJGL_LINK, "lwjgl-" + LWJGL_VERSION + ".jar");
        progressBar.setValue(70);
        download(LWJGL_UTIL_LINK, "lwjgl_util-" + LWJGL_VERSION + ".jar");
        progressBar.setValue(100);
        restartButton.setEnabled(true);
    }

    public void downloadAndExtract(String url) {
        try {
            URL website = new URL(url);
            BufferedInputStream bis = new BufferedInputStream(website.openStream());
            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                //exclude META-INFO folder
                if (ze.getName().contains("META-INF")) {
                    continue;
                }
                if (ze.isDirectory()) {
                    continue;
                }
                int size = (int) ze.getSize();
                // -1 means unknown size.
                if (size == -1) {
                    continue;
                }
                byte[] b = new byte[size];
                int rb = 0;
                int chunk;

                while ((size - rb) > 0) {
                    chunk = zis.read(b, rb, size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }
                Files.write(b, new File(libFolder, ze.getName()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url, String filename) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(new File(libFolder, filename));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}