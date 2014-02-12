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

import de.keyle.dungeoncraft.editor.util.DungeonCraftEditorVersion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class About extends JDialog {
    private JPanel mainPanel;
    private JLabel versionLabel;
    private JLabel urlBukkitDevLabel;
    private JButton closeButton;
    private JLabel oxygenLicenseLabel;
    private JLabel oxygenWebsiteLabel;
    private JLabel rsyntaxtextareaWebsiteLabel;
    private JLabel rsyntaxtextareaLicenseLabel;
    private JLabel buildLabel;
    private JLabel xrayWebsiteLabel;
    private JLabel xrayLicenseLabel;

    public About() {
        setContentPane(mainPanel);
        setModal(true);
        getRootPane().setDefaultButton(closeButton);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buildLabel.setText(DungeonCraftEditorVersion.getBuild());
        versionLabel.setText(DungeonCraftEditorVersion.getVersion());
    }

    public static void main(String[] args) {
        showAbout();
    }

    private void createUIComponents() {
        urlBukkitDevLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>http://dev.bukkit.org/bukkit-plugins/dungeoncraft/</U></FONT></HTML>");
        urlBukkitDevLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlBukkitDevLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://dev.bukkit.org/bukkit-plugins/dungeoncraft/");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        oxygenLicenseLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>License</U></FONT></HTML>");
        oxygenLicenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        oxygenLicenseLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        oxygenWebsiteLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>http://www.oxygen-icons.org/</U></FONT></HTML>");
        oxygenWebsiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        oxygenWebsiteLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://www.oxygen-icons.org/");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        rsyntaxtextareaWebsiteLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>http://fifesoft.com/rsyntaxtextarea/</U></FONT></HTML>");
        rsyntaxtextareaWebsiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rsyntaxtextareaWebsiteLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://fifesoft.com/rsyntaxtextarea/");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        rsyntaxtextareaLicenseLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>License</U></FONT></HTML>");
        rsyntaxtextareaLicenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rsyntaxtextareaLicenseLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://fifesoft.com/rsyntaxtextarea/RSyntaxTextArea.License.txt");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        xrayWebsiteLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>http://apocalyptech.com/minecraft/xray/</U></FONT></HTML>");
        xrayWebsiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        xrayWebsiteLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://apocalyptech.com/minecraft/xray/");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        xrayLicenseLabel = new JLabel("<HTML><FONT color=\"#000099\"><U>License</U></FONT></HTML>");
        xrayLicenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        xrayLicenseLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        URI uri = new URI("http://apocalyptech.com/minecraft/xray/COPYING.txt");
                        desktop.browse(uri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public static void showAbout() {
        About dialog = new About();
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}