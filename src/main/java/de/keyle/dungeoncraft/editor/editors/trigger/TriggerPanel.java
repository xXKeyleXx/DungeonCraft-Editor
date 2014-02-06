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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class TriggerPanel extends JPanel {
    private RSyntaxTextArea triggerTextArea;
    private Trigger trigger;

    public TriggerPanel(Trigger trigger) {
        this.trigger = trigger;
        this.setLayout(new BorderLayout());
        this.setName(trigger.getName());

        triggerTextArea = new RSyntaxTextArea(20, 60);
        triggerTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        triggerTextArea.setCodeFoldingEnabled(false);
        triggerTextArea.setAntiAliasingEnabled(true);

        if(trigger.getContent() != null || !trigger.getContent().equals("")) {
            triggerTextArea.setText(trigger.getContent());
        }

        RTextScrollPane sp = new RTextScrollPane(triggerTextArea);
        sp.setFoldIndicatorEnabled(true);

        this.add(sp, BorderLayout.CENTER);
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void save() {
        trigger.setName(this.getName());
        trigger.setContent(triggerTextArea.getText());
    }
}