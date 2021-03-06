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
    private Block[] blocks = new Block[255];
    public static final Block unknownBlock = new Unknown();

    public BlockTypes(JSONObject blockTypeObject) {
        blockTypes.put("DEFAULT", Default.class);
        blockTypes.put("UNKNOWN", Unknown.class);
        blockTypes.put("DECORATION_CROSS", DecorationCross.class);
        blockTypes.put("LOG", Log.class);
        blockTypes.put("END_PORTAL", EndPortal.class);
        blockTypes.put("SEMISOLID", SemiSolid.class);
        blockTypes.put("WATER", Water.class);
        blockTypes.put("CACTUS", Cactus.class);
        blockTypes.put("DECORATION_GRID", DecorationGrid.class);
        blockTypes.put("ENCHANTMENT_TABLE", EnchantmentTable.class);
        blockTypes.put("SIGNPOST", Signpost.class);
        blockTypes.put("END_PORTAL_FRAME", EndPortalFrame.class);
        blockTypes.put("FENCE",Fence.class);
        blockTypes.put("LADDER", Ladder.class);
        blockTypes.put("VINE", Vine.class);
        blockTypes.put("LEVER", Lever.class);
        blockTypes.put("BED", Bed.class);
        blockTypes.put("BREWING_STAND", BrewingStand.class);
        blockTypes.put("BUTTON", Button.class);
        blockTypes.put("CAKE", Cake.class);
        blockTypes.put("CAULDRON", Cauldron.class);
        blockTypes.put("CHEST", Chest.class);
        blockTypes.put("DOOR", Door.class);
        blockTypes.put("DRAGON_EGG", DragonEgg.class);
        blockTypes.put("SNOW", Snow.class);
        blockTypes.put("TORCH", Torch.class);
        blockTypes.put("SOLID_PANE", SolidPane.class);
        blockTypes.put("FLOOR", Floor.class);
        blockTypes.put("PRESSURE_PLATE", PressurePlate.class);
        blockTypes.put("CARPET", Carpet.class);
        blockTypes.put("SLAB", Slab.class);
        blockTypes.put("PORTAL", Portal.class);
        blockTypes.put("WALLSIGN", WallSign.class);
        blockTypes.put("TRAPDOOR", TrapDoor.class);
        blockTypes.put("HUGE_MUSHROOM", HugeMushroom.class);

        blocks[0] = new Air();

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
                if (blocks[id] != null) {
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
                blocks[id] = b;
            }
        }
    }

    public Block getBlock(short id) {
        if (id < 0) {
            return unknownBlock;
        }
        return blocks[id];
    }

    public Block getBlock(int id) {
        if (id < 0) {
            return unknownBlock;
        }
        return blocks[id];
    }
}