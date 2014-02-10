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

package de.keyle.dungeoncraft.editor.editors.config;

import de.keyle.dungeoncraft.editor.editors.Editor;
import de.keyle.dungeoncraft.editor.util.DisabledPanel;
import de.keyle.dungeoncraft.editor.util.Util;
import de.keyle.dungeoncraft.editor.util.config.ConfigurationSnakeYAML;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigEditor implements Editor {
    private JComboBox environmentComboBox;
    private JCheckBox rainCheckBox;
    private JSpinner xSpinner;
    private JSpinner yawSpinner;
    private JCheckBox timeLockedCheckBox;
    private JSpinner playerMinSpinner;
    private JSpinner timeSpinner;
    private JSpinner playerMaxSpinner;
    private JSpinner timeLimitSpinner;
    private JSpinner lockoutSpinner;
    private JSpinner pitchSpinner;
    private JSpinner zSpinner;
    private JSpinner ySpinner;
    private JTextArea commandsTextArea;
    private JTable customOptionsTable;
    private JPanel mainPanel;
    private JButton addCutomOptionButton;
    private JButton deleteCutomOptionButton;
    private JScrollPane configScrollPane;

    DefaultTableModel customOptionsTabelModel;
    File configFile = null;

    public ConfigEditor() {
        configScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        addCutomOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] rowContent = {"", ""};
                customOptionsTabelModel.addRow(rowContent);
                deleteCutomOptionButton.setEnabled(true);
            }
        });
        deleteCutomOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customOptionsTable.getSelectedRow();
                if (selectedRow != -1) {
                    customOptionsTabelModel.removeRow(selectedRow);
                    if (customOptionsTable.getRowCount() > 0) {
                        customOptionsTable.setRowSelectionInterval(Math.min(selectedRow, customOptionsTable.getRowCount() - 1), Math.min(selectedRow, customOptionsTable.getRowCount() - 1));
                    } else {
                        deleteCutomOptionButton.setEnabled(false);
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        mainPanel = new DisabledPanel();

        SpinnerNumberModel timeSpinnerModel = new SpinnerNumberModel(0, 0, 24000, 1000);
        timeSpinner = new JSpinner(timeSpinnerModel);

        SpinnerNumberModel playerMinSpinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        playerMinSpinner = new JSpinner(playerMinSpinnerModel);

        SpinnerNumberModel playerMaxSpinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        playerMaxSpinner = new JSpinner(playerMaxSpinnerModel);

        customOptionsTabelModel = new DefaultTableModel(new String[0][0], new String[]{"Key", "Value"}) {
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 1) {
                    super.setValueAt(aValue, row, column);
                    return;
                }
                for (int i = 0; i < customOptionsTabelModel.getRowCount(); i++) {
                    if (i == row) {
                        continue;
                    }
                    if (aValue.toString().equalsIgnoreCase(customOptionsTabelModel.getValueAt(i, 0).toString())) {
                        return;
                    }
                }
                super.setValueAt(aValue, row, column);
            }
        };
        customOptionsTable = new JTable(customOptionsTabelModel) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return true;
            }
        };
    }

    @Override
    public String getName() {
        return "Config";
    }

    @Override
    public Component getPanel() {
        return mainPanel;
    }

    private void resetFields() {
        environmentComboBox.setSelectedIndex(0);
        rainCheckBox.setSelected(false);
        timeSpinner.setValue(0);
        timeLockedCheckBox.setSelected(false);
        xSpinner.setValue(0);
        ySpinner.setValue(0);
        zSpinner.setValue(0);
        yawSpinner.setValue(0);
        pitchSpinner.setValue(0);
        timeLimitSpinner.setValue(0);
        lockoutSpinner.setValue(0);
        playerMinSpinner.setValue(0);
        playerMaxSpinner.setValue(0);
        commandsTextArea.setText("");
        customOptionsTabelModel.setRowCount(0);
        deleteCutomOptionButton.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void openDungeon(File dungeonFolder) {
        resetFields();
        configFile = new File(dungeonFolder, "config.yml");
        if (configFile.exists()) {
            ConfigurationSnakeYAML config = new ConfigurationSnakeYAML(configFile);
            config.load();
            Map<String, Object> configMap = config.getConfig();

            if (configMap.containsKey("spawn")) {
                Map<String, Object> spawnMap = (Map<String, Object>) configMap.get("spawn");
                if (spawnMap.containsKey("location")) {
                    Map<String, Object> spawnLocationMap = (Map<String, Object>) spawnMap.get("location");
                    if (spawnLocationMap.containsKey("x")) {
                        String xValue = spawnLocationMap.get("x").toString();
                        if (Util.isDouble(xValue)) {
                            xSpinner.setValue(Double.parseDouble(xValue));
                        }
                    }
                    if (spawnLocationMap.containsKey("y")) {
                        String yValue = spawnLocationMap.get("y").toString();
                        if (Util.isDouble(yValue)) {
                            ySpinner.setValue(Double.parseDouble(yValue));
                        }
                    }
                    if (spawnLocationMap.containsKey("z")) {
                        String zValue = spawnLocationMap.get("z").toString();
                        if (Util.isDouble(zValue)) {
                            zSpinner.setValue(Double.parseDouble(zValue));
                        }
                    }
                    if (spawnLocationMap.containsKey("yaw")) {
                        String yawValue = spawnLocationMap.get("yaw").toString();
                        if (Util.isDouble(yawValue)) {
                            yawSpinner.setValue(Double.parseDouble(yawValue));
                        }
                    }
                    if (spawnLocationMap.containsKey("pitch")) {
                        String pitchValue = spawnLocationMap.get("pitch").toString();
                        if (Util.isDouble(pitchValue)) {
                            pitchSpinner.setValue(Double.parseDouble(pitchValue));
                        }
                    }
                }
            }

            if (configMap.containsKey("time")) {
                Map<String, Object> timeMap = (Map<String, Object>) configMap.get("time");
                if (timeMap.containsKey("limit")) {
                    String limitValue = timeMap.get("limit").toString();
                    if (Util.isInt(limitValue)) {
                        timeLimitSpinner.setValue(Integer.parseInt(limitValue));
                    }
                }
                if (timeMap.containsKey("start")) {
                    String timeStartValue = timeMap.get("start").toString();
                    if (Util.isInt(timeStartValue)) {
                        timeSpinner.setValue(Integer.parseInt(timeStartValue));
                    }
                }
                if (timeMap.containsKey("lock")) {
                    timeLockedCheckBox.setSelected(timeMap.get("lock").toString().equalsIgnoreCase("true"));
                }
                if (timeMap.containsKey("lockout")) {
                    String lockoutValue = timeMap.get("lockout").toString();
                    if (Util.isInt(lockoutValue)) {
                        lockoutSpinner.setValue(Integer.parseInt(lockoutValue));
                    }
                }
            }

            if (configMap.containsKey("player")) {
                Map<String, Object> playerMap = (Map<String, Object>) configMap.get("player");
                if (playerMap.containsKey("count")) {
                    Map<String, Object> playerCountMap = (Map<String, Object>) playerMap.get("count");
                    if (playerCountMap.containsKey("min")) {
                        String minValue = playerCountMap.get("min").toString();
                        if (Util.isInt(minValue)) {
                            playerMinSpinner.setValue(Integer.parseInt(minValue));
                        }
                    }
                    if (playerCountMap.containsKey("max")) {
                        String maxValue = playerCountMap.get("max").toString();
                        if (Util.isInt(maxValue)) {
                            playerMaxSpinner.setValue(Integer.parseInt(maxValue));
                        }
                    }
                }
            }

            if (configMap.containsKey("world")) {
                Map<String, Object> worldMap = (Map<String, Object>) configMap.get("world");
                if (worldMap.containsKey("environment")) {
                    String environmentValue = worldMap.get("environment").toString();
                    for (int i = 0; i < environmentComboBox.getItemCount(); i++) {
                        if (environmentComboBox.getItemAt(i).toString().equalsIgnoreCase(environmentValue)) {
                            environmentComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                if (worldMap.containsKey("weather")) {
                    rainCheckBox.setSelected(worldMap.get("weather").toString().equalsIgnoreCase("true"));
                }
            }

            if (configMap.containsKey("commands")) {
                Map<String, Object> commandsMap = (Map<String, Object>) configMap.get("commands");
                if (commandsMap.containsKey("allowed")) {
                    List<Object> allowedList = (List<Object>) commandsMap.get("allowed");
                    String allowedCommands = "";
                    for (Object command : allowedList) {
                        allowedCommands += command.toString() + "\n";
                    }
                    commandsTextArea.setText(allowedCommands);
                }
            }

            if (configMap.containsKey("options")) {
                Map<String, Object> optionsMap = (Map<String, Object>) configMap.get("options");
                for (String key : optionsMap.keySet()) {
                    customOptionsTabelModel.addRow(new Object[]{key, optionsMap.get(key)});
                }
            }
        }
    }

    @Override
    public void saveDungeon() {
        ConfigurationSnakeYAML config = new ConfigurationSnakeYAML(configFile);
        Map<String, Object> configMap = config.getConfig();

        Map<String, Object> spawnMap = new HashMap<String, Object>();
        Map<String, Object> spawnLocationMap = new HashMap<String, Object>();
        spawnLocationMap.put("x", xSpinner.getValue());
        spawnLocationMap.put("y", ySpinner.getValue());
        spawnLocationMap.put("z", zSpinner.getValue());
        spawnLocationMap.put("yaw", yawSpinner.getValue());
        spawnLocationMap.put("pitch", pitchSpinner.getValue());
        spawnMap.put("location", spawnLocationMap);
        configMap.put("spawn", spawnMap);

        Map<String, Object> timeMap = new HashMap<String, Object>();
        timeMap.put("limit", timeLimitSpinner.getValue());
        timeMap.put("start", timeSpinner.getValue());
        timeMap.put("lock", timeLockedCheckBox.isSelected());
        timeMap.put("lockout", lockoutSpinner.getValue());
        configMap.put("time", timeMap);

        Map<String, Object> playerMap = new HashMap<String, Object>();
        Map<String, Object> playerCountMap = new HashMap<String, Object>();
        playerCountMap.put("min", playerMinSpinner.getValue());
        playerCountMap.put("max", playerMaxSpinner.getValue());
        playerMap.put("count", playerCountMap);
        configMap.put("player", playerMap);

        Map<String, Object> worldMap = new HashMap<String, Object>();
        worldMap.put("environment", environmentComboBox.getSelectedItem());
        worldMap.put("weather", rainCheckBox.isSelected());
        configMap.put("world", worldMap);

        Map<String, Object> commandsMap = new HashMap<String, Object>();
        List<String> commandsAllowedList = new ArrayList<String>();
        for (String line : commandsTextArea.getText().split("\\n")) {
            line = line.trim();
            if (line.equals("")) {
                continue;
            }
            commandsAllowedList.add(line);
        }
        commandsMap.put("allowed", commandsAllowedList);
        configMap.put("commands", commandsMap);

        Map<String, Object> optionsMap = new HashMap<String, Object>();
        for (int i = 0; i < customOptionsTabelModel.getRowCount(); i++) {
            String key = customOptionsTabelModel.getValueAt(i, 0).toString().trim();
            Object value = customOptionsTabelModel.getValueAt(i, 1);

            optionsMap.put(key, value);
        }
        configMap.put("options", optionsMap);

        config.save();
    }

    @Override
    public void init() {
    }
}