/*
 * This file is part of DungeonCraft
 *
 * Copyright (C) 2013-2014 Keyle & xXLupoXx
 * DungeonCraft is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.dungeoncraft.editor;

import de.keyle.dungeoncraft.editor.editors.MainForm;
import de.keyle.dungeoncraft.editor.editors.config.ConfigEditor;
import de.keyle.dungeoncraft.editor.editors.entity.EntityCreator;
import de.keyle.dungeoncraft.editor.editors.region.RegionEditor;
import de.keyle.dungeoncraft.editor.editors.trigger.TriggerEditor;
import de.keyle.dungeoncraft.editor.editors.world.WorldOverview;

import javax.swing.*;

public class GuiMain {
    private static MainForm mainForm;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        mainForm = new MainForm();

        mainForm.registerNewEditor(new ConfigEditor());
        mainForm.registerNewEditor(new WorldOverview());
        mainForm.registerNewEditor(new EntityCreator());
        mainForm.registerNewEditor(new TriggerEditor());
        mainForm.registerNewEditor(new RegionEditor());

        mainForm.showFrame();
    }

    public static MainForm getMainForm() {
        return mainForm;
    }
}