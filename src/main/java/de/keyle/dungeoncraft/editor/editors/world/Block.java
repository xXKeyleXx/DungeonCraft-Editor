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

package de.keyle.dungeoncraft.editor.editors.world;

public class Block {
    private boolean isActive;
    private BlockType blockType;

    public enum BlockType {
        BlockType_Default(0),
        BlockType_Grass(1),
        BlockType_Dirt(2),
        BlockType_Water(3),
        BlockType_Stone(4),
        BlockType_Wood(5),
        BlockType_Sand(6),
        BlockType_NumTypes(7);
        private int BlockID;
        BlockType(int i) {
            BlockID=i;
        }
        public int getID(){
            return BlockID;
        }
    }

    public Block(BlockType blockType){
        this.blockType = blockType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive =active;
    }
    public int getID(){
        return blockType.getID();
    }
}