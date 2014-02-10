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

package de.keyle.dungeoncraft.editor.editors.trigger;

import com.google.common.io.PatternFilenameFilter;
import de.keyle.dungeoncraft.editor.editors.Editor;
import de.keyle.dungeoncraft.editor.util.DisabledPanel;
import de.keyle.dungeoncraft.editor.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TriggerEditor implements Editor {
    private JPanel mainPanel;
    private JTabbedPane triggerFilesTabbedPane;
    private JButton addTriggerButton;
    private JButton deleteTriggerButton;
    private JButton renameTriggerButton;
    private JComboBox themeComboBox;

    private List<TriggerPanel> triggerPanels = new ArrayList<TriggerPanel>();
    private File triggerFolder = null;
    private List<String> deletedTriggerFiles = new ArrayList<String>();

    public TriggerEditor() {
        addTriggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = JOptionPane.showInputDialog(null, "Enter the name of the new trigger.", "Create new Trigger", JOptionPane.QUESTION_MESSAGE);
                if (response != null) {
                    if (response.matches("[a-zA-Z0-9-_]+")) {
                        for (TriggerPanel panel : triggerPanels) {
                            if (panel.getName().equalsIgnoreCase(response)) {
                                JOptionPane.showMessageDialog(null, "There is already a trigger with this name", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        Trigger newTrigger = new Trigger(response, "function init() {\n\n}");
                        TriggerPanel panel = new TriggerPanel(newTrigger);
                        deletedTriggerFiles.remove(response);
                        triggerPanels.add(panel);
                        triggerFilesTabbedPane.add(panel);
                        panel.loadTheme(getTextAreaTheme());
                        triggerFilesTabbedPane.setVisible(true);
                        deleteTriggerButton.setEnabled(true);
                        renameTriggerButton.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "This is not a valid trigger name!\n\na-z\nA-Z\n0-9\n_ -", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        renameTriggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (triggerFilesTabbedPane.getSelectedComponent() instanceof TriggerPanel) {
                    TriggerPanel renamedPanel = (TriggerPanel) triggerFilesTabbedPane.getSelectedComponent();
                    String response = (String) JOptionPane.showInputDialog(null, "Enter the new name of the trigger.", "Rename Trigger", JOptionPane.QUESTION_MESSAGE, null, null, renamedPanel.getName());
                    if (response != null) {
                        if (response.matches("[a-zA-Z0-9-_]+")) {
                            for (TriggerPanel panel : triggerPanels) {
                                if (panel.getName().equalsIgnoreCase(response)) {
                                    JOptionPane.showMessageDialog(null, "There is already a trigger with this name", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            triggerFilesTabbedPane.setTitleAt(triggerFilesTabbedPane.getSelectedIndex(), response);
                            deletedTriggerFiles.add(renamedPanel.getTrigger().getName());
                            renamedPanel.setName(response);
                            renamedPanel.save();
                        } else {
                            JOptionPane.showMessageDialog(null, "This is not a valid trigger name!\n\na-z\nA-Z\n0-9\n_ -", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        deleteTriggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (triggerFilesTabbedPane.getSelectedComponent() instanceof TriggerPanel) {
                    TriggerPanel panel = (TriggerPanel) triggerFilesTabbedPane.getSelectedComponent();
                    triggerPanels.remove(panel);
                    triggerFilesTabbedPane.remove(panel);
                    deletedTriggerFiles.remove(panel.getTrigger().getName());
                    if (triggerPanels.size() == 0) {
                        triggerFilesTabbedPane.setVisible(false);
                        deleteTriggerButton.setEnabled(false);
                        renameTriggerButton.setEnabled(false);
                    }
                }
            }
        });
        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TriggerPanel.Themes selectedTheme = getTextAreaTheme();
                for (TriggerPanel panel : triggerPanels) {
                    panel.loadTheme(selectedTheme);
                }
            }
        });
    }

    private TriggerPanel.Themes getTextAreaTheme() {
        String selectedThemeName = themeComboBox.getSelectedItem().toString();
        TriggerPanel.Themes selectedTheme;
        if (selectedThemeName.equals("Dark")) {
            selectedTheme = TriggerPanel.Themes.Dark;
        } else if (selectedThemeName.equals("Eclipse")) {
            selectedTheme = TriggerPanel.Themes.Eclipse;
        } else if (selectedThemeName.equals("Idea")) {
            selectedTheme = TriggerPanel.Themes.Idea;
        } else if (selectedThemeName.equals("VS")) {
            selectedTheme = TriggerPanel.Themes.VS;
        } else {
            selectedTheme = TriggerPanel.Themes.Default;
        }
        return selectedTheme;
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public void openDungeon(File dungeonFolder) {
        triggerFolder = new File(dungeonFolder, "trigger");
        if (triggerFolder.exists() && triggerFolder.isDirectory()) {
            File[] triggerFiles = triggerFolder.listFiles(new PatternFilenameFilter(Pattern.compile("[.-_a-z0-9]+\\.js", Pattern.CASE_INSENSITIVE)));
            if (triggerFiles != null) {
                for (File triggerFile : triggerFiles) {
                    String triggerName = triggerFile.getName().substring(0, triggerFile.getName().length() - 3);
                    String triggerContent = Util.readFile(triggerFile);
                    Trigger trigger = new Trigger(triggerName, triggerContent);
                    TriggerPanel panel = new TriggerPanel(trigger);
                    triggerPanels.add(panel);
                    triggerFilesTabbedPane.add(panel);
                }
                if (triggerPanels.size() > 0) {
                    triggerFilesTabbedPane.setVisible(true);
                    deleteTriggerButton.setEnabled(true);
                    renameTriggerButton.setEnabled(true);
                } else {
                    deleteTriggerButton.setEnabled(false);
                    renameTriggerButton.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void saveDungeon() {
        System.out.println("saveDungeon");

        triggerFolder.mkdirs();

        for (String deletedFileName : deletedTriggerFiles) {
            new File(triggerFolder, deletedFileName + ".js").delete();
        }
        for (TriggerPanel panel : triggerPanels) {
            panel.save();
            Trigger trigger = panel.getTrigger();

            File triggerFile = new File(triggerFolder, trigger.getName() + ".js");

            Util.writeFile(triggerFile, trigger.getContent());
        }
    }

    @Override
    public void init() {
    }

    @Override
    public String getName() {
        return "Trigger Editor";
    }

    private void createUIComponents() {
        mainPanel = new DisabledPanel();
    }
}