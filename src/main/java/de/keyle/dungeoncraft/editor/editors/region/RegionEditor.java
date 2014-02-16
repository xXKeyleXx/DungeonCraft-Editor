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

package de.keyle.dungeoncraft.editor.editors.region;

import de.keyle.dungeoncraft.editor.GuiMain;
import de.keyle.dungeoncraft.editor.editors.Editor;
import de.keyle.dungeoncraft.editor.editors.world.WorldOverview;
import de.keyle.dungeoncraft.editor.util.DisabledPanel;
import de.keyle.dungeoncraft.editor.util.Util;
import de.keyle.dungeoncraft.editor.util.config.ConfigurationJson;
import de.keyle.dungeoncraft.editor.util.vector.OrientationVector;
import de.keyle.dungeoncraft.editor.util.vector.Region;
import de.keyle.dungeoncraft.editor.util.vector.Vector;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class RegionEditor implements Editor {
    private JPanel mainPanel;
    private JTree regionsTree;
    private JSpinner pos1xSpinner;
    private JSpinner pos1ySpinner;
    private JSpinner pos1zSpinner;
    private JSpinner pos2xSpinner;
    private JSpinner pos2ySpinner;
    private JSpinner pos2zSpinner;
    private JTextField regionNameTextField;
    private JButton addRegionButton;
    private JButton deleteRegionButton;
    private JButton renameRegionButton;
    private JButton goToPos1Button;
    private JButton goToPos2Button;
    private JPanel valuePanel;
    private JButton colorPickerButton;
    private JCheckBox showInWorldViewerCheckBox;

    private File regionFile = null;
    private Map<String, Region> regions = new HashMap<String, Region>();
    private Region selectedRegion = null;
    public static java.util.List<Region> shownRegions = new ArrayList<Region>();

    public RegionEditor() {
        deleteRegionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object component = regionsTree.getSelectionPath().getLastPathComponent();
                if (component instanceof RegionNode) {
                    RegionNode node = (RegionNode) component;
                    int selectionRow = regionsTree.getMaxSelectionRow();
                    regions.remove(node.getRegionName());
                    ((DefaultMutableTreeNode) regionsTree.getModel().getRoot()).remove(node);
                    regionsTree.updateUI();
                    if (selectionRow >= regionsTree.getRowCount()) {
                        regionsTree.setSelectionRow(regionsTree.getRowCount() - 1);
                    } else {
                        regionsTree.setSelectionRow(selectionRow);
                    }
                }
            }
        });
        addRegionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = (String) JOptionPane.showInputDialog(null, "Enter the name of the region.", "Rename Region", JOptionPane.QUESTION_MESSAGE, null, null, "");
                if (response != null) {
                    if (response.matches("[a-zA-Z0-9-_]+")) {
                        if (regions.containsKey(response)) {
                            JOptionPane.showMessageDialog(null, "There is already a region with this name", "Create new Region", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Region newRegion = new Region(new Vector(), new Vector());
                        regions.put(response, newRegion);
                        ((DefaultMutableTreeNode) regionsTree.getModel().getRoot()).add(new RegionNode(response, newRegion));
                        regionsTree.updateUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "This is not a valid region name!\n\na-z\nA-Z\n0-9\n_ -", "Create new Region", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        regionsTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (e.getPath().getPath().length == 2) {
                    Object component = regionsTree.getSelectionPath().getLastPathComponent();
                    if (component instanceof RegionNode) {
                        RegionNode node = (RegionNode) component;
                        selectedRegion = node.getRegion();
                        regionNameTextField.setText(node.getRegionName());
                        pos2xSpinner.setValue(selectedRegion.getMax().getBlockX());
                        pos2ySpinner.setValue(selectedRegion.getMax().getBlockY());
                        pos2zSpinner.setValue(selectedRegion.getMax().getBlockZ());
                        pos1xSpinner.setValue(selectedRegion.getMin().getBlockX());
                        pos1ySpinner.setValue(selectedRegion.getMin().getBlockY());
                        pos1zSpinner.setValue(selectedRegion.getMin().getBlockZ());
                        showInWorldViewerCheckBox.setSelected(shownRegions.contains(selectedRegion));
                        valuePanel.setEnabled(true);
                        deleteRegionButton.setEnabled(true);
                        for (Editor editor : GuiMain.getMainForm().getEditorList()) {
                            if (editor instanceof WorldOverview) {
                                if (!((WorldOverview) editor).isLWJGLFound()) {
                                    goToPos1Button.setEnabled(false);
                                    goToPos2Button.setEnabled(false);
                                    showInWorldViewerCheckBox.setEnabled(false);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    regionNameTextField.setText("");
                    pos1xSpinner.setValue(0);
                    pos1ySpinner.setValue(0);
                    pos1zSpinner.setValue(0);
                    pos2xSpinner.setValue(0);
                    pos2ySpinner.setValue(0);
                    pos2zSpinner.setValue(0);
                    deleteRegionButton.setEnabled(false);
                    goToPos1Button.setEnabled(true);
                    goToPos2Button.setEnabled(true);
                    valuePanel.setEnabled(false);
                }
            }
        });
        goToPos1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRegion != null) {
                    OrientationVector pos = new OrientationVector(selectedRegion.getMin(), 0, 0);
                    for (Editor editor : GuiMain.getMainForm().getEditorList()) {
                        if (editor instanceof WorldOverview) {
                            GuiMain.getMainForm().setEditorVisible(editor);
                            ((WorldOverview) editor).setCameraPosition(pos);
                            break;
                        }
                    }
                }
            }
        });
        goToPos2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRegion != null) {
                    OrientationVector pos = new OrientationVector(selectedRegion.getMax(), 0, 0);
                    for (Editor editor : GuiMain.getMainForm().getEditorList()) {
                        if (editor instanceof WorldOverview) {
                            GuiMain.getMainForm().setEditorVisible(editor);
                            ((WorldOverview) editor).setCameraPosition(pos);
                            break;
                        }
                    }
                }
            }
        });
        renameRegionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object component = regionsTree.getSelectionPath().getLastPathComponent();
                if (component instanceof RegionNode) {
                    RegionNode node = (RegionNode) component;
                    String response = (String) JOptionPane.showInputDialog(null, "Enter the new name of the region.", "Rename Region", JOptionPane.QUESTION_MESSAGE, null, null, node.getRegionName());
                    if (response != null) {
                        if (response.matches("[a-zA-Z0-9-_]+")) {
                            if (regions.containsKey(response)) {
                                JOptionPane.showMessageDialog(null, "There is already a region with this name", "Create new Region", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            regions.remove(node.getRegionName());
                            regions.put(response, selectedRegion);
                            node.setRegionName(response);
                            regionNameTextField.setText(response);
                            regionsTree.updateUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "This is not a valid region name!\n\na-z\nA-Z\n0-9\n_ -", "Create new Region", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        valuePanel.setEnabled(false);
        pos1xSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMin().setX((Integer) pos1xSpinner.getValue());
                }
            }
        });
        pos1ySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMin().setY((Integer) pos1ySpinner.getValue());
                }
            }
        });
        pos1zSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMin().setZ((Integer) pos1zSpinner.getValue());
                }
            }
        });
        pos2xSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMax().setX((Integer) pos2xSpinner.getValue());
                }
            }
        });
        pos2ySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMax().setY((Integer) pos2ySpinner.getValue());
                }
            }
        });
        pos2zSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (selectedRegion != null) {
                    selectedRegion.getMax().setZ((Integer) pos2zSpinner.getValue());
                }
            }
        });
        colorPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(mainPanel, "Select Region Color", selectedRegion.getColor());
                if (color != null) {
                    selectedRegion.setColor(color);
                }
            }
        });
        showInWorldViewerCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showInWorldViewerCheckBox.isSelected()) {
                    shownRegions.add(selectedRegion);
                } else {
                    shownRegions.remove(selectedRegion);
                }
            }
        });
    }

    @Override
    public String getName() {
        return "Regions";
    }

    @Override
    public Component getPanel() {
        return mainPanel;
    }

    public Collection<Region> getRegions() {
        return regions.values();
    }

    public List<Region> getShownRegions() {
        return shownRegions;
    }

    @Override
    public void openDungeon(File dungeonFolder) {
        resetFields();
        regionFile = new File(dungeonFolder, "regions.json");
        if (regionFile.exists()) {
            ConfigurationJson jsonConfig = new ConfigurationJson(regionFile);
            jsonConfig.load();

            JSONObject regionsObject = (JSONObject) jsonConfig.getObject();
            for (Object regionObject : regionsObject.keySet()) {
                String regionName = regionObject.toString();
                JSONObject region = (JSONObject) regionsObject.get(regionObject);
                if (!region.containsKey("min") || !region.containsKey("max")) {
                    continue;
                }
                JSONObject min = (JSONObject) region.get("min");
                if (!min.containsKey("x") || !min.containsKey("y") || !min.containsKey("z")) {
                    continue;
                }
                JSONObject max = (JSONObject) region.get("max");
                if (!max.containsKey("x") || !max.containsKey("y") || !max.containsKey("z")) {
                    continue;
                }
                String xMinString = min.get("x").toString();
                String yMinString = min.get("y").toString();
                String zMinString = min.get("z").toString();

                String xMaxString = max.get("x").toString();
                String yMaxString = max.get("y").toString();
                String zMaxString = max.get("z").toString();

                if (!Util.isInt(xMinString) || !Util.isInt(yMinString) || !Util.isInt(zMinString)) {
                    continue;
                }

                if (!Util.isInt(xMaxString) || !Util.isInt(yMaxString) || !Util.isInt(zMaxString)) {
                    continue;
                }

                int xMin = Integer.parseInt(xMinString);
                int yMin = Integer.parseInt(yMinString);
                int zMin = Integer.parseInt(zMinString);
                int xMax = Integer.parseInt(xMaxString);
                int yMax = Integer.parseInt(yMaxString);
                int zMax = Integer.parseInt(zMaxString);

                Vector minVector = new Vector(Math.min(xMin, xMax), Math.min(yMin, yMax), Math.min(zMin, zMax));
                Vector maxVector = new Vector(Math.max(xMin, xMax), Math.max(yMin, yMax), Math.max(zMin, zMax));

                Region newRegion = new Region(minVector, maxVector);
                if (region.containsKey("editor")) {
                    JSONObject editor = (JSONObject) region.get("editor");
                    if (editor.containsKey("color") && Util.isInt(editor.get("color").toString())) {
                        Color color = new Color(Integer.parseInt(editor.get("color").toString()));
                        newRegion.setColor(color);
                    }
                    if (editor.containsKey("visible") && editor.get("visible").toString().equalsIgnoreCase("true")) {
                        shownRegions.add(newRegion);
                    }
                }


                regions.put(regionName, newRegion);
                ((DefaultMutableTreeNode) regionsTree.getModel().getRoot()).add(new RegionNode(regionName, newRegion));
            }
            regionsTree.expandRow(0);
            regionsTree.updateUI();
        }
    }

    private class RegionNode extends DefaultMutableTreeNode {
        private Region region;
        private String regionName;

        public RegionNode(String regionName, Region region) {
            super(regionName);
            this.regionName = regionName;
            this.region = region;
        }

        public Region getRegion() {
            return region;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
            super.setUserObject(regionName);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveDungeon() {
        ConfigurationJson jsonConfig = new ConfigurationJson(regionFile);
        JSONObject root = (JSONObject) jsonConfig.getObject();
        for (String regionName : regions.keySet()) {
            Region region = regions.get(regionName);
            JSONObject regionObject = new JSONObject();
            JSONObject min = new JSONObject();
            min.put("x", region.getMin().getBlockX());
            min.put("y", region.getMin().getBlockY());
            min.put("z", region.getMin().getBlockZ());
            regionObject.put("min", min);
            JSONObject max = new JSONObject();
            max.put("x", region.getMax().getBlockX());
            max.put("y", region.getMax().getBlockY());
            max.put("z", region.getMax().getBlockZ());
            regionObject.put("max", max);
            JSONObject editor = new JSONObject();
            editor.put("color", region.getColor().getRGB());
            editor.put("visible", shownRegions.contains(region));
            regionObject.put("editor", editor);
            root.put(regionName, regionObject);
        }
        jsonConfig.save();
    }

    @Override
    public void init() {

    }

    @Override
    public void switchToEditor(Editor editor) {
    }

    private void resetFields() {
        regionNameTextField.setText("");
        pos1xSpinner.setValue(0);
        pos1ySpinner.setValue(0);
        pos1zSpinner.setValue(0);
        pos2xSpinner.setValue(0);
        pos2ySpinner.setValue(0);
        pos2zSpinner.setValue(0);
        ((DefaultMutableTreeNode) regionsTree.getModel().getRoot()).removeAllChildren();
        regionsTree.updateUI();
        valuePanel.setEnabled(false);
        showInWorldViewerCheckBox.setSelected(false);
    }

    private void createUIComponents() {
        mainPanel = new DisabledPanel();
        valuePanel = new DisabledPanel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Regions");
        DefaultTreeModel regionsTreeModel = new DefaultTreeModel(root);
        regionsTree = new JTree(regionsTreeModel) {
            protected void setExpandedState(TreePath path, boolean state) {
                super.setExpandedState(path, true);
            }
        };
        regionsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        MinSpinner xMinSpinner = new MinSpinner(0, 0, 1600, 1);
        MinSpinner yMinSpinner = new MinSpinner(0, 0, 1600, 1);
        MinSpinner zMinSpinner = new MinSpinner(0, 0, 1600, 1);
        MaxSpinner xMaxSpinner = new MaxSpinner(0, 0, 1600, 1);
        MaxSpinner yMaxSpinner = new MaxSpinner(0, 0, 1600, 1);
        MaxSpinner zMaxSpinner = new MaxSpinner(0, 0, 1600, 1);

        xMinSpinner.setMaxSpinner(xMaxSpinner);
        yMinSpinner.setMaxSpinner(yMaxSpinner);
        zMinSpinner.setMaxSpinner(zMaxSpinner);
        xMaxSpinner.setMinSpinner(xMinSpinner);
        yMaxSpinner.setMinSpinner(yMinSpinner);
        zMaxSpinner.setMinSpinner(zMinSpinner);

        pos1xSpinner = new JSpinner(xMinSpinner);
        pos1ySpinner = new JSpinner(yMinSpinner);
        pos1zSpinner = new JSpinner(zMinSpinner);
        pos2xSpinner = new JSpinner(xMaxSpinner);
        pos2ySpinner = new JSpinner(yMaxSpinner);
        pos2zSpinner = new JSpinner(zMaxSpinner);
    }
}