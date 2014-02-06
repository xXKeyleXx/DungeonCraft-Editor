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


import de.keyle.dungeoncraft.editor.editors.entity.EntityCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GuiMain {

    public static EntityCreator entityCreator;
    public static String[] mobTypes = new String[]{"Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "Enderman", "Ghast", "Giant", "Horse", "IronGolem", "MagmaCube", "Mooshroom", "Ocelot", "Pig", "PigZombie", "Sheep", "Silverfish", "Skeleton", "Slime", "Snowman", "Spider", "Squid", "Witch", "Wither", "Wolf", "Villager", "Zombie"};
    public static void main(String[] args) {
        entityCreator = new EntityCreator();
        final JFrame entityCreatorFrame = entityCreator.getFrame();
        entityCreatorFrame.setContentPane(entityCreator.getMainPanel());
        entityCreatorFrame.setMinimumSize(new Dimension(600,500));
        //entityCreatorFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        entityCreatorFrame.pack();
        entityCreatorFrame.setVisible(true);
        entityCreatorFrame.setLocationRelativeTo(null);
        entityCreatorFrame.addWindowListener(new WindowListener() {
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
}
