/**
 * Copyright (c) 2010-2012, Christopher J. Kucera
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Minecraft X-Ray team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL VINCENT VOLLERS OR CJ KUCERA BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.keyle.dungeoncraft.editor.editors.world.render;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.BLOCK_TYPE;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.blockTypeExtraTexturesReq;

/**
 * Data about block types, intended to be read in from a YAML file (this will
 * get kicked off via the BlockTypeCollection class, actually).
 * <p/>
 * This class suffers from extreme Dissociative Identity Disorder, probably
 * due to deficiencies in the way I'm loading the data via snakeyaml.  The
 * basic problem is that I couldn't figure out a way to get the data to import
 * in a way that's most useful to X-Ray.  So, for a bunch of datatypes, we
 * end up using a call to "normalize()" to convert them to a different set of
 * datatypes, which X-Ray will then use.
 * <p/>
 * One issue is that a few of our data structures use Bytes, and snakeyaml doesn't
 * seem to like those very much.  They'd seemingly work okay when I plug them
 * into a HashMap, but when trying to use them with methods that explicitly require
 * a byte, I'd get this:
 * <p/>
 * java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Byte
 * <p/>
 * ... and I never did figure out a good way around that.  Instead I just have the
 * YAML attributes read into an Integer, and do the conversion manually.  It's
 * possible that we shouldn't bother using Bytes, actually, since from what I've
 * been reading, Java might only reference data in units of words, anyway, but for
 * now we're going to continue using bytes.
 * <p/>
 * The other problem is that I couldn't get snakeyaml to use our Enums properly when
 * they were contained inside a HashMap.  You may notice that "type" is directly
 * loaded from Yaml into the Enum, properly, but I couldn't get it to do the same
 * thing when the Enum was one element of a HashMap.  I'd end up getting the same
 * sort of errors as with the integer/byte thing, above, where it was saying it
 * refused to cast a java.lang.String to whatever the Enum was.
 * <p/>
 * I assume there are solutions to both of those problems, which would let me just
 * import the Yaml directly into the structures I actually care about, but for
 * now we'll just go with this slightly crazy and un-OOesque way of doing things.
 * <p/>
 * The getUsedTextures() call will only provide meaningful information after
 * normalization.
 */
public class BlockType
        implements Comparable<BlockType> {

    public enum DIRECTION_REL {
        FORWARD,
        BACKWARD,
        SIDES,
        TOP,
        BOTTOM
    }

    public enum DIRECTION_ABS {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    // YAML Attributes
    public short id;
    public String idStr;
    public String name;
    public String aka;
    public BLOCK_TYPE type;
    private ArrayList<Integer> mapcolor;
    private HashMap<Integer, String> tex_direction_data;
    private int tex_data_bits;
    private boolean override; // This is not at all tested yet
    private boolean explored;

    // Computed Attributes
    public Color color;
    public int tex_idx;
    public HashMap<Byte, Integer> texture_data_map;
    public HashMap<DIRECTION_REL, Integer> texture_dir_map;
    public HashMap<Byte, DIRECTION_ABS> texture_dir_data_map;
    public HashMap<String, Integer> texture_extra_map;

    // Other attributes
    public String texfile;
    public int texSheet;
    private String searchMatch;
    public int tex_data_mask;

    public BlockType() {
        this.override = false;
        this.explored = false;
        this.id = -1;
        this.tex_idx = -1;
        this.texSheet = 0;
        this.setTex_data_bits(4);
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getId() {
        return this.id;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getIdStr() {
        return this.idStr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAka(String aka) {
        this.aka = aka;
    }

    public String getAka() {
        return this.aka;
    }

    public void setMapcolor(ArrayList<Integer> mapcolor) {
        this.mapcolor = mapcolor;
    }

    public ArrayList<Integer> getMapcolor() {
        return this.mapcolor;
    }

    public void setTexfile(String texfile) {
        this.texfile = texfile;
    }

    public String getTexfile() {
        return this.texfile;
    }

    public void setTex_data_bits(int tex_data_bits) {
        this.tex_data_bits = tex_data_bits;
        this.tex_data_mask = (int) (Math.pow(2, tex_data_bits) - 1);
    }

    public int getTex_data_bits() {
        return this.tex_data_bits;
    }

    public void setTexSheet(int texSheet) {
        this.texSheet = texSheet;
    }

    public int getTexSheet() {
        return this.texSheet;
    }

    public void setTexIdx(int tex) {
        this.tex_idx = tex;
    }

    public void setTexIdxCoords(int x, int y) {
        ArrayList<Integer> coords = new ArrayList<Integer>();
        coords.add(x);
        coords.add(y);
        this.tex_idx = getTexReal(coords);
    }

    public static int getTexReal(ArrayList<Integer> coords) {
        return coords.get(0) + (16 * coords.get(1));
    }

    public static ArrayList<Integer> getTexCoords(int tex) {
        int[] tex_coords_arr = getTexCoordsArr(tex);
        ArrayList<Integer> tex_coords = new ArrayList<Integer>();
        tex_coords.add(tex_coords_arr[0]);
        tex_coords.add(tex_coords_arr[1]);
        return tex_coords;
    }

    public static int[] getTexCoordsArr(int tex) {
        int[] tex_coords = new int[2];
        tex_coords[0] = tex % 16;
        tex_coords[1] = tex / 16;
        return tex_coords;
    }

    public int[] getTexCoordsArr() {
        return getTexCoordsArr(this.tex_idx);
    }

    public void setTex_direction_data(HashMap<Integer, String> tex_direction_data) {
        this.tex_direction_data = tex_direction_data;
    }

    public HashMap<Integer, String> getTex_direction_data() {
        return this.tex_direction_data;
    }

    public void setType(BLOCK_TYPE type) {
        this.type = type;
    }

    public BLOCK_TYPE getType() {
        return this.type;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public boolean getOverride() {
        return this.override;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public boolean getExplored() {
        return this.explored;
    }

    public boolean isSolid() {
        // TODO: define some kind of "solid_render_type" or something in YAML, so that we
        // can continue to just test for NORMAL here.
        return (this.type == BLOCK_TYPE.NORMAL || this.type == BLOCK_TYPE.HUGE_MUSHROOM);
    }

    /**
     * A function to pull any necessary data from the Collection object
     * itself.  BlockTypeFilename, for instance, needs to grab the
     * Collection's texpath.
     */
    public void pullDataFromCollection(BlockTypeCollection collection) {
        this.texfile = collection.getTexfile();
    }

    /**
     * Normalizes our data from what we get from YAML, to a format that's
     * easier to deal with in X-Ray.
     */
    public void normalizeData()
            throws BlockTypeLoadException {
        // First check for some required attributes
        if (this.id < -1) {
            throw new BlockTypeLoadException("id is a required attribute");
        }
        if (this.idStr == null) {
            throw new BlockTypeLoadException("idStr is a required attribute");
        }
        if (this.idStr.equals("UNKNOWN")) {
            // This isn't actually 100% accurate; our "special" UNKNOWN block
            // never gets officially added to blockCollection, so there would
            // never be any actual conflicts.  It could get confusing, though,
            // so I'll just pretend it's fully reserved.
            throw new BlockTypeLoadException("UNKNOWN is a reserved idStr");
        }
        if (this.name == null) {
            throw new BlockTypeLoadException("name is a required attribute");
        }
        if (this.mapcolor == null) {
            throw new BlockTypeLoadException("mapcolor is a required attribute");
        }
        if (this.mapcolor.size() != 3) {
            throw new BlockTypeLoadException("mapcolor requires three elements (RGB)");
        }

        // Now do the actual normalizing
        this.color = new Color(this.mapcolor.get(0), this.mapcolor.get(1), this.mapcolor.get(2));
        if (this.getType() == null) {
            this.setType(BLOCK_TYPE.NORMAL);
        }
        if (this.tex_direction_data != null) {
            this.texture_dir_data_map = new HashMap<Byte, DIRECTION_ABS>();
            for (Map.Entry<Integer, String> entry : this.tex_direction_data.entrySet()) {
                DIRECTION_ABS dir;
                try {
                    dir = DIRECTION_ABS.valueOf(entry.getValue());
                } catch (IllegalArgumentException e) {
                    throw new BlockTypeLoadException("Invalid absolute direction: " + entry.getValue());
                }
                this.texture_dir_data_map.put(entry.getKey().byteValue(), dir);
            }
        }
    }

    /**
     * Runs some additional sanity checks on our blocks which must happen post-normalization
     */
    public void postNormalizeData()
            throws BlockTypeLoadException {
        if (blockTypeExtraTexturesReq.containsKey(this.type)) {
            if (this.texture_extra_map == null) {
                StringBuilder sb = new StringBuilder(this.type.toString() + " blocks must contain the following in tex_extra: ");
                for (String key : blockTypeExtraTexturesReq.get(this.type)) {
                    sb.append("'").append(key).append("', ");
                }
                sb.delete(sb.length() - 2, sb.length());
                throw new BlockTypeLoadException(sb.toString());
            }
            for (String key : blockTypeExtraTexturesReq.get(this.type)) {
                if (!this.texture_extra_map.containsKey(key)) {
                    throw new BlockTypeLoadException(this.type.toString() + " blocks must contain '" + key + "' in the tex_extra section");
                }
            }
        }

        // Matching String
        this.searchMatch = this.idStr.toLowerCase().replace("_", " ") + ", " + this.name.toLowerCase();
        if (this.aka != null) {
            this.searchMatch = this.searchMatch.concat(", " + this.aka.toLowerCase());
        }
    }

    /**
     * Returns a list of the filenames of all the textures in-use by this BlockType.
     * This only really makes sense for BlockTypeFilename, but this way the rest of
     * the app doesn't have to know about the different types of BlockTypes.  Note
     * that we don't actually care if there are duplicates or not.
     */
    public ArrayList<String> getTextureFilenames() {
        return new ArrayList<String>();
    }

    /**
     * Sets our "real" texture information based on the hashmap passed in (after
     * doing the actual texture reservations that we need).  Once again, for
     * "regular" blocktypes, this is meaningless.
     */
    public void setTextureFilenameMapping(HashMap<String, Integer> texmap)
            throws BlockTypeLoadException {
    }

    /**
     * Returns a list of all textures that this block type uses
     */
    public ArrayList<Integer> getUsedTextures() {
        HashMap<Integer, Boolean> tempMap = new HashMap<Integer, Boolean>();
        if (this.tex_idx >= 0) {
            tempMap.put(this.tex_idx, true);
        }
        if (this.texture_data_map != null) {
            for (Integer tex : this.texture_data_map.values()) {
                if (!tempMap.containsKey(tex)) {
                    tempMap.put(tex, true);
                }
            }
        }
        if (this.texture_dir_map != null) {
            for (Integer tex : this.texture_dir_map.values()) {
                if (!tempMap.containsKey(tex)) {
                    tempMap.put(tex, true);
                }
            }
        }

        // Be sure to include "extra" textures
        if (blockTypeExtraTexturesReq.containsKey(this.type)) {
            for (String key : blockTypeExtraTexturesReq.get(this.type)) {
                if (!tempMap.containsKey(this.texture_extra_map.get(key))) {
                    tempMap.put(this.texture_extra_map.get(key), true);
                }
            }
        }

        // Now convert to a list
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer tex : tempMap.keySet()) {
            list.add(tex);
        }
        return list;
    }

    /**
     * Given a mapping of old-to-new texture indexes, convert all of our
     * available textures.
     * <p/>
     * TODO: proper exception reporting
     */
    public void convertTexIdx(HashMap<Integer, Integer> mapping)
            throws BlockTypeLoadException {
        // First the texture itself
        if (mapping.containsKey(this.tex_idx)) {
            this.tex_idx = mapping.get(this.tex_idx);
        } else {
            throw new BlockTypeLoadException("Could not convert main texture");
        }

        // Now the data map
        if (this.texture_data_map != null) {
            for (Map.Entry<Byte, Integer> entry : this.texture_data_map.entrySet()) {
                if (mapping.containsKey(entry.getValue())) {
                    entry.setValue(mapping.get(entry.getValue()));
                } else {
                    throw new BlockTypeLoadException("Could not convert data map");
                }
            }
        }

        // Now the direction map
        if (this.texture_dir_map != null) {
            for (Map.Entry<DIRECTION_REL, Integer> entry : this.texture_dir_map.entrySet()) {
                if (mapping.containsKey(entry.getValue())) {
                    entry.setValue(mapping.get(entry.getValue()));
                } else {
                    throw new BlockTypeLoadException("Could not convert direction map");
                }
            }
        }

        // Now the extra map
        if (this.texture_extra_map != null) {
            for (Map.Entry<String, Integer> entry : this.texture_extra_map.entrySet()) {
                if (mapping.containsKey(entry.getValue())) {
                    entry.setValue(mapping.get(entry.getValue()));
                } else {
                    throw new BlockTypeLoadException("Could not convert direction map");
                }
            }
        }
    }

    /**
     * Checks to see if this block matches a given search "pattern."
     * And by "pattern" I mean "substring."  Will check versus idStr,
     * name, and aka.
     */
    public boolean matches(String search) {
        if (this.searchMatch == null) {
            return false;
        } else {
            return this.searchMatch.contains(search.toLowerCase());
        }
    }

    /**
     * Method for sorting BlockType objects, as needed by implementing Comparable
     */
    public int compareTo(BlockType anotherInstance) {
        return this.name.compareTo(anotherInstance.name);
    }

    @Override
    public String toString() {
        return "BlockType{" +
                "id=" + id +
                ", idStr='" + idStr + '\'' +
                ", name='" + name + '\'' +
                ", aka='" + aka + '\'' +
                ", type=" + type +
                ", tex_idx=" + tex_idx +
                ", texSheet=" + texSheet +
                '}';
    }
}