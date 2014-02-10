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

package de.keyle.dungeoncraft.editor.editors.world.schematic;

import de.keyle.knbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicLoader extends Thread {
    private SchematicReveiver schematicReceiver;

    public SchematicLoader(SchematicReveiver schematicReceiver) {
        this.schematicReceiver = schematicReceiver;
    }

    public void run() {
        Schematic schematic;
        try {
            schematic = loadSchematic(schematicReceiver.getSchematicFile());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        schematicReceiver.setSchematic(schematic);
    }

    public static Schematic loadSchematic(File file) throws IOException {

        if (!file.exists()) {
            throw new IllegalArgumentException("Schematic file not found");
        }

        FileInputStream stream = new FileInputStream(file);

        TagCompound schematicTag = TagStream.readTag(stream, true);
        if (schematicTag == null) {
            schematicTag = TagStream.readTag(stream, false);
            if (schematicTag == null) {
                throw new IllegalArgumentException("Can not read tags");
            }
        }

        if (!schematicTag.containsKeyAs("Blocks", TagByteArray.class)) {
            throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
        }

        short width = getChildTag(schematicTag, "Width", TagShort.class).getShortData();
        short length = getChildTag(schematicTag, "Length", TagShort.class).getShortData();
        short height = getChildTag(schematicTag, "Height", TagShort.class).getShortData();

        String materials = getChildTag(schematicTag, "Materials", TagString.class).getStringData();
        if (!materials.equals("Alpha")) {
            throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
        }

        byte[] blocks = getChildTag(schematicTag, "Blocks", TagByteArray.class).getByteArrayData();
        byte[] blockData = getChildTag(schematicTag, "Data", TagByteArray.class).getByteArrayData();
        byte[] biomeData;
        if (schematicTag.containsKeyAs("Biomes", TagByteArray.class)) {
            biomeData = getChildTag(schematicTag, "Biomes", TagByteArray.class).getByteArrayData();
        } else {
            biomeData = new byte[length * width];
        }

        return new Schematic(blocks, blockData, biomeData, width, length, height);
    }

    private static <T extends TagBase> T getChildTag(TagCompound items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.getCompoundData().containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        TagBase tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}