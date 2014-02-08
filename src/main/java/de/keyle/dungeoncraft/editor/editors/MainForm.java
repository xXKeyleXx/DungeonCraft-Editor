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

package de.keyle.dungeoncraft.editor.editors;

import de.keyle.dungeoncraft.editor.util.DisabledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    private JFrame editorFrame;
    private JTabbedPane editorsTabbedPane;
    private JButton openFromFolderButton;
    private JButton saveButton;
    private JButton openButton;
    private JButton saveAsButton;
    private JButton aboutButton;
    private JPanel mainPanel;

    private File dungeonFolder = null;
    private List<Editor> editorList = new ArrayList<Editor>();


    public MainForm() {
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                About.showAbout();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dungeonFolder != null) {
                    if(dungeonFolder.exists() && dungeonFolder.isDirectory()) {
                        for(Editor panel : editorList) {
                            panel.saveDungeon();
                        }
                    }
                }
            }
        });
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        openFromFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = chooser.showOpenDialog(editorFrame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    openDungeon(chooser.getSelectedFile());
                    saveButton.setEnabled(true);
                }
            }
        });
    }

    public void openDungeon(File dungeonFolder) {
        if(dungeonFolder != null) {
            if(dungeonFolder.exists() && dungeonFolder.isDirectory()) {
                this.dungeonFolder = dungeonFolder;
                for(Editor panel : editorList) {
                    if(panel.getPanel() instanceof DisabledPanel) {
                        panel.getPanel().setEnabled(true);
                    }
                    panel.openDungeon(this.dungeonFolder);
                }
            }
        }
    }

    public void registerNewEditor(Editor editor) {
        editorList.add(editor);
        editorsTabbedPane.add(editor.getName(), editor.getPanel());
        if(dungeonFolder == null && editor instanceof DisabledPanel) {
            editor.getPanel().setEnabled(false);
        }
    }

    public JFrame showFrame() {
        if (editorFrame == null) {
            editorFrame = new JFrame("DungeonCraft - Editor");

            editorFrame.setContentPane(mainPanel);
            editorFrame.setMinimumSize(new Dimension(600, 500));
            editorFrame.pack();
            editorFrame.setVisible(true);
            editorFrame.setLocationRelativeTo(null);
            editorFrame.addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowActivated(WindowEvent e) {
                }

                public void windowDeactivated(WindowEvent e) {
                }
            });
        }
        return editorFrame;
    }
}