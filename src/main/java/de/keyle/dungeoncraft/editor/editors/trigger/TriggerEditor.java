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

import de.keyle.dungeoncraft.editor.editors.Editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TriggerEditor implements Editor {
    private JPanel mainPanel;
    private JTabbedPane triggerFilesTabbedPane;
    private JButton addTriggerButton;
    private JButton deleteTriggerButton;
    private JButton renameTriggerButton;

    private List<TriggerPanel> triggerPanels = new ArrayList<TriggerPanel>();

    public TriggerEditor() {
        addTriggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String response = JOptionPane.showInputDialog(null, "Enter the name of the new trigger.", "Create new Trigger", JOptionPane.QUESTION_MESSAGE);
                if (response != null) {
                    if (response.matches("[a-zA-Z0-9-_]+")) {
                        for(TriggerPanel panel : triggerPanels) {
                            if(panel.getName().equalsIgnoreCase(response)) {
                                JOptionPane.showMessageDialog(null, "There is already a trigger with this name", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        Trigger newTrigger = new Trigger(response, "");
                        TriggerPanel panel = new TriggerPanel(newTrigger);
                        triggerPanels.add(panel);
                        triggerFilesTabbedPane.add(panel);
                    } else {
                        JOptionPane.showMessageDialog(null, "This is not a valid trigger name!\n\na-z\nA-Z\n0-9\n_ -", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        renameTriggerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(triggerFilesTabbedPane.getSelectedComponent() instanceof TriggerPanel) {
                    TriggerPanel renamedPanel = (TriggerPanel) triggerFilesTabbedPane.getSelectedComponent();
                    String response = (String) JOptionPane.showInputDialog(null, "Enter the new name of the trigger.", "Rename Trigger", JOptionPane.QUESTION_MESSAGE, null, null, renamedPanel.getName());
                    if (response != null) {
                        if (response.matches("[a-zA-Z0-9-_]+")) {
                            for(TriggerPanel panel : triggerPanels) {
                                if(panel.getName().equalsIgnoreCase(response)) {
                                    JOptionPane.showMessageDialog(null, "There is already a trigger with this name", "Create new Trigger", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            triggerFilesTabbedPane.setTitleAt(triggerFilesTabbedPane.getSelectedIndex(),response);
                            renamedPanel.setName(response);
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
                if(triggerFilesTabbedPane.getSelectedComponent() instanceof TriggerPanel) {
                    TriggerPanel panel = (TriggerPanel) triggerFilesTabbedPane.getSelectedComponent();
                    triggerPanels.remove(panel);
                    triggerFilesTabbedPane.remove(panel);
                }
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    public void save() {
        for(TriggerPanel panel : triggerPanels) {
            panel.save();
            Trigger trigger = panel.getTrigger();

            //ToDo Save triggers
        }
    }

    public void load() {
        //ToDo Load triggers
    }

    @Override
    public String getName() {
        return "Trigger Editor";
    }
}