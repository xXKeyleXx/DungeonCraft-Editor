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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    private Map<Class<? extends Editor>, Editor> editors = new HashMap<Class<? extends Editor>, Editor>();

    public MainForm() {
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                About.showAbout();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dungeonFolder != null) {
                    if (dungeonFolder.exists() && dungeonFolder.isDirectory()) {
                        for (Editor panel : editors.values()) {
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
                if (result == JFileChooser.APPROVE_OPTION) {
                    openDungeon(chooser.getSelectedFile());
                    saveButton.setEnabled(true);
                }
            }
        });
        editorsTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                Editor selectedEditor = null;
                Component selectedComponent = editorsTabbedPane.getSelectedComponent();
                for (Editor editor : editors.values()) {
                    if (editor.getPanel() == selectedComponent) {
                        selectedEditor = editor;
                    }
                }
                if (selectedEditor != null) {
                    for (Editor editor : editors.values()) {
                        editor.switchToEditor(selectedEditor);
                    }
                }
            }
        });
    }

    public Collection<Editor> getEditorList() {
        return editors.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends Editor> T getEditor(Class<? extends Editor> clazz) {
        if (editors.containsKey(clazz)) {
            return (T) editors.get(clazz);
        }
        return null;
    }

    public void setEditorVisible(Editor editor) {
        editorsTabbedPane.setSelectedComponent(editor.getPanel());
    }

    public JFrame getFrame() {
        return editorFrame;
    }

    public void openDungeon(File dungeonFolder) {
        if (dungeonFolder != null) {
            if (dungeonFolder.exists() && dungeonFolder.isDirectory()) {
                this.dungeonFolder = dungeonFolder;
                for (Editor panel : editors.values()) {
                    if (panel.getPanel() instanceof DisabledPanel) {
                        panel.getPanel().setEnabled(true);
                    }
                    panel.openDungeon(this.dungeonFolder);
                }
            }
        }
    }

    public void registerNewEditor(Editor editor) {
        editors.put(editor.getClass(), editor);
        editorsTabbedPane.add(editor.getName(), editor.getPanel());
        if (dungeonFolder == null) {
            if (editor.getPanel() instanceof DisabledPanel) {
                editor.getPanel().setEnabled(false);
            } else if (editor.getPanel() instanceof Container) {
                DisabledPanel.disableContainer((Container) editor.getPanel());
            }
        }
    }

    public JFrame showFrame() {
        if (editorFrame == null) {
            editorFrame = new JFrame("DungeonCraft - Editor");

            editorFrame.setContentPane(mainPanel);
            editorFrame.setMinimumSize(new Dimension(890, 730));
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

            for (Editor panel : editors.values()) {
                panel.init();
            }
        }
        return editorFrame;
    }
}