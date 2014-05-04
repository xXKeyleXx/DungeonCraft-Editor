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

package de.keyle.dungeoncraft.editor.editors.world.render;

import de.keyle.dungeoncraft.editor.editors.world.render.block.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BlockTypes {
    private Map<String, Class<? extends Block>> blockTypes = new HashMap<String, Class<? extends Block>>();
    private Map<Short, Block> blocks = new HashMap<Short, Block>();

    public BlockTypes(JSONObject blockTypeObject) {
        blockTypes.put("DEFAULT", Default.class);
        blockTypes.put("UNKNWON", Unknown.class);
        blockTypes.put("DECORATION_CROSS", DecorationCross.class);
        blockTypes.put("LOG", Log.class);
        blockTypes.put("END_PORTAL", EndPortal.class);
        blockTypes.put("SEMISOLID", SemiSolid.class);
        blockTypes.put("WATER", Water.class);
        blockTypes.put("CACTUS", Cactus.class);
        blockTypes.put("DECORATION_GRID", DecorationGrid.class);
        //blockTypes.put("BED", Bed.class);
        //blockTypes.put("BREWING_STAND", BrewingStand.class);
        //blockTypes.put("BUTTON", Button.class);
        //blockTypes.put("CAKE", Cake.class);
        //blockTypes.put("CAULDRON", Cauldron.class);
        //blockTypes.put("CHEST", Chest.class);

        blocks.put((short) 0, new Air());
        blocks.put((short) -1, new Unknown());

        loadBlocks(blockTypeObject);
    }

    protected void loadBlocks(JSONObject blockTypeObject) {
        if (blockTypeObject.containsKey("blocks")) {
            JSONArray blockArray = (JSONArray) blockTypeObject.get("blocks");

            for (Object aBlockArray : blockArray) {
                JSONObject blockObject = (JSONObject) aBlockArray;
                Class<? extends Block> blockClass;
                short id;
                if (blockObject.containsKey("id")) {
                    id = Short.parseShort(blockObject.get("id").toString());
                } else {
                    continue;
                }
                if (blocks.containsKey(id)) {
                    continue;
                }
                if (!blockObject.containsKey("texture")) {
                    continue;
                }
                if (blockObject.containsKey("type")) {
                    blockClass = blockTypes.get(blockObject.get("type").toString());
                    if (blockClass == null) {
                        continue;
                    }
                } else {
                    blockClass = blockTypes.get("DEFAULT");
                }
                Block b;
                try {
                    b = blockClass.getConstructor(short.class).newInstance(id);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    continue;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    continue;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    b.readTextures((JSONObject) blockObject.get("texture"));
                } catch (BlockTypeLoadException e) {
                    e.printStackTrace();
                    continue;
                }
                blocks.put(id, b);
            }
        }
    }

    public Block getBlock(short id) {
        return blocks.get(id);
    }
}