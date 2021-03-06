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

import de.keyle.dungeoncraft.editor.util.config.ConfigurationJson;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class TriggerPanel extends JPanel {
    private RSyntaxTextArea triggerTextArea;
    private Trigger trigger;

    public enum Themes {
        Default, Dark, Eclipse, Idea, VS
    }

    public TriggerPanel(Trigger trigger) {
        this.trigger = trigger;
        this.setLayout(new BorderLayout());
        this.setName(trigger.getName());

        triggerTextArea = new RSyntaxTextArea(20, 60);
        triggerTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        triggerTextArea.setCodeFoldingEnabled(true);
        triggerTextArea.setAntiAliasingEnabled(true);
        triggerTextArea.setAutoIndentEnabled(true);
        triggerTextArea.setCloseCurlyBraces(true);
        triggerTextArea.setMarkOccurrences(true);
        triggerTextArea.setPaintMarkOccurrencesBorder(true);
        triggerTextArea.setHyperlinksEnabled(true);
        triggerTextArea.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        if (trigger.getContent() != null || !trigger.getContent().equals("")) {
            triggerTextArea.setText(trigger.getContent());
        }
        triggerTextArea.setCaretPosition(0);

        createContextMenu(triggerTextArea);

        RTextScrollPane sp = new RTextScrollPane(triggerTextArea);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.setFoldIndicatorEnabled(true);
        sp.setAutoscrolls(true);

        this.add(sp, BorderLayout.CENTER);
    }

    public void createContextMenu(final RSyntaxTextArea textArea) {
        JPopupMenu contextMenu = textArea.getPopupMenu();

        JMenu dungeonContextMenu = new JMenu("Dungeon Context");
        contextMenu.add(dungeonContextMenu);

        ActionListener menuItemActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof ContextPopupMenuItem) {
                    textArea.insert(((ContextPopupMenuItem)e.getSource()).getContent(), textArea.getCaretPosition());
                }
            }
        };

        InputStream is = getClass().getResourceAsStream("/editor/trigger/context.json");
        JSONArray contextArray = (JSONArray) ConfigurationJson.load(is);
        if(contextArray.size() == 0) {
            dungeonContextMenu.setEnabled(false);
            return;
        }
        for(Object contextEntryObject : contextArray) {
            JSONObject contextEntryJSONObject = (JSONObject) contextEntryObject;
            if(!contextEntryJSONObject.containsKey("name")) {
                return;
            }
            JMenu newContextMenu = new JMenu(contextEntryJSONObject.get("name").toString());
            dungeonContextMenu.add(newContextMenu);
            if(contextEntryJSONObject.containsKey("entries")) {
                JSONArray contextEntriesArray = (JSONArray) contextEntryJSONObject.get("entries");
                for(Object contextMenuItemObject : contextEntriesArray) {
                    JSONObject contextMenuItemJSONObject = (JSONObject) contextMenuItemObject;
                    if(!contextMenuItemJSONObject.containsKey("menu") || !contextMenuItemJSONObject.containsKey("content")) {
                        return;
                    }
                    String menu = contextMenuItemJSONObject.get("menu").toString();
                    String content = contextMenuItemJSONObject.get("content").toString();
                    String tooltip = "";
                    if(contextMenuItemJSONObject.containsKey("tooltip")) {
                        tooltip = contextMenuItemJSONObject.get("tooltip").toString();
                    }
                    ContextPopupMenuItem contextMenuItem = new ContextPopupMenuItem(menu,content,tooltip);
                    contextMenuItem.addActionListener(menuItemActionListener);
                    newContextMenu.add(contextMenuItem);
                }
            }
        }
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void save() {
        trigger.setName(this.getName());
        trigger.setContent(triggerTextArea.getText());
    }

    public void loadTheme(Themes t) {
        Theme theme = null;
        try {
            switch (t) {
                case Default:
                    theme = Theme.load(getClass().getResourceAsStream("/editor/trigger/themes/default.xml"));
                    break;
                case Dark:
                    theme = Theme.load(getClass().getResourceAsStream("/editor/trigger/themes/dark.xml"));
                    break;
                case Eclipse:
                    theme = Theme.load(getClass().getResourceAsStream("/editor/trigger/themes/eclipse.xml"));
                    break;
                case Idea:
                    theme = Theme.load(getClass().getResourceAsStream("/editor/trigger/themes/idea.xml"));
                    break;
                case VS:
                    theme = Theme.load(getClass().getResourceAsStream("/editor/trigger/themes/vs.xml"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (theme != null) {
            theme.apply(triggerTextArea);
        }
    }
}