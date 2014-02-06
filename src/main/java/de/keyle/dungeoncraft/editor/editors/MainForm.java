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

import de.keyle.dungeoncraft.editor.About;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainForm {
    private JFrame editorFrame;
    private JTabbedPane editorsTabbedPane;
    private JButton openFromFolderButton;
    private JButton saveButton;
    private JButton openButton;
    private JButton saveAsButton;
    private JButton aboutButton;
    private JPanel mainPanel;


    public MainForm() {
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                About.showAbout();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

            }
        });
    }

    public void registerNewEditor(Editor editor) {
        editorsTabbedPane.add(editor.getName(), editor.getPanel());
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