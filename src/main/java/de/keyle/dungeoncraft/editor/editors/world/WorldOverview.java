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

import de.keyle.dungeoncraft.editor.GuiMain;
import de.keyle.dungeoncraft.editor.editors.Editor;
import de.keyle.dungeoncraft.editor.editors.region.RegionEditor;
import de.keyle.dungeoncraft.editor.editors.world.render.WorldViewer;
import de.keyle.dungeoncraft.editor.editors.world.schematic.Schematic;
import de.keyle.dungeoncraft.editor.editors.world.schematic.SchematicLoader;
import de.keyle.dungeoncraft.editor.editors.world.schematic.SchematicReveiver;
import de.keyle.dungeoncraft.editor.util.DisabledPanel;
import de.keyle.dungeoncraft.editor.util.Util;
import de.keyle.dungeoncraft.editor.util.vector.OrientationVector;
import de.keyle.dungeoncraft.editor.util.vector.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.List;

public class WorldOverview implements Editor, SchematicReveiver {
    private JPanel mainPanel;
    private JPanel renderPanel;
    private JCheckBox showRegionsCheckBox;
    private JProgressBar downloadProgressBar;
    private JButton downloadLWJGLButton;
    private JButton restartEditorButton;
    private JPanel downloadPanel;

    private final boolean lwjglFound;

    public Canvas canvas;
    File schematicFile;
    Schematic schematic = null;
    WorldViewer renderThread;
    RegionEditor regionEditor = null;

    public WorldOverview() {
        lwjglFound = Util.existsClass("org.lwjgl.opengl.Display") && Util.existsClass("org.lwjgl.util.glu.GLU");
        downloadLWJGLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DependencyDownloader(downloadProgressBar, restartEditorButton).start();
                downloadLWJGLButton.setEnabled(false);
            }
        });
        restartEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Util.restartApplication();
            }
        });
    }

    private void createUIComponents() {
        mainPanel = new DisabledPanel();
        canvas = new Canvas();
        renderPanel = new JPanel(new BorderLayout());
        renderPanel.add(canvas, BorderLayout.CENTER);
    }

    @Override
    public String getName() {
        return "World Overview";
    }

    @Override
    public Component getPanel() {
        return mainPanel;
    }

    @Override
    public void openDungeon(File dungeonFolder) {
        resetFields();
        if (lwjglFound) {
            schematicFile = new File(dungeonFolder, dungeonFolder.getName() + ".schematic");
            schematic = null;
            if (schematicFile.exists()) {
                new SchematicLoader(this).start();
            }
        } else {
            mainPanel.setEnabled(false);
            downloadLWJGLButton.setEnabled(true);
        }
    }

    @Override
    public void saveDungeon() {
    }

    private void resetFields() {
        showRegionsCheckBox.setSelected(false);
    }

    public void setCameraPosition(OrientationVector pos) {
        renderThread.setCameraPosition(pos);
    }

    public synchronized boolean showRegions() {
        return showRegionsCheckBox.isSelected();
    }

    @Override
    public void init() {
        if (lwjglFound) {
            downloadPanel.setVisible(false);
            renderThread = new WorldViewer(this);
            renderThread.start();

            GuiMain.getMainForm().getFrame().addComponentListener(new ComponentAdapter() {
                boolean isResized = false;

                @Override
                public void componentResized(ComponentEvent e) {
                    if (!isResized) {
                        canvas.setVisible(false);
                        isResized = true;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                canvas.setSize(new Dimension(renderPanel.getWidth(), renderPanel.getHeight()));
                                if (mainPanel.isVisible()) {
                                    canvas.setVisible(true);
                                }
                                isResized = false;
                                if (renderThread != null) {
                                    renderThread.resize();
                                }
                            }
                        });
                    }
                }

                public void componentMoved(ComponentEvent e) {
                }

                @Override
                public void componentShown(ComponentEvent e) {
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                }
            });
        } else {
            downloadPanel.setVisible(true);
            downloadLWJGLButton.setEnabled(true);
        }
    }

    @Override
    public void switchToEditor(Editor editor) {
        if (lwjglFound && editor == this) {
            canvas.setVisible(true);
        } else {
            canvas.setVisible(false);
        }
    }

    public boolean isLWJGLFound() {
        return lwjglFound;
    }

    @Override
    public File getSchematicFile() {
        return schematicFile;
    }

    @Override
    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
        renderThread.openNewSchematic();

    }

    public synchronized Schematic getSchematic() {
        return schematic;
    }

    public synchronized List<Region> getSelectedRegions() {
        if (regionEditor == null) {
            regionEditor = GuiMain.getMainForm().getEditor(RegionEditor.class);
        }
        if (regionEditor != null) {
            return regionEditor.getShownRegions();
        }
        return null;
    }
}