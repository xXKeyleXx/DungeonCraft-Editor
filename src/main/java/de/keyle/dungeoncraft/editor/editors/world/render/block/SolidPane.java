package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Facing;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class SolidPane extends Block {

    TextureDimensions front[] = new TextureDimensions[16];
    TextureDimensions side[] = new TextureDimensions[16];
    TextureDimensions top[] = new TextureDimensions[16];

    public SolidPane(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (!textures.containsKey("0")) {
            throw new BlockTypeLoadException(id + " is missing \"0\" data.");
        }
        for (Object key : textures.keySet()) {
            if (textures.get(key) instanceof JSONObject) {
                if (Util.isByte(key.toString())) {
                    byte data = Byte.parseByte(key.toString());
                    JSONObject textureObject = (JSONObject) textures.get(key);
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("SIDE");
                        side[data] = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                    if (textureObject.containsKey("FRONT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("FRONT");
                        front[data] = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT texture.");
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("TOP");
                        top[data] = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " TOP texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "SOLID_PANE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        boolean has_north = false;
        boolean has_south = false;
        boolean has_west = false;
        boolean has_east = false;

        float top_width = .0625f;

        short temp_id;
        temp_id = chunk.getAdjBlockId(x, y, z, Facing.WEST);
        if (temp_id == id || chunk.getBlock(temp_id) != null && chunk.getBlock(temp_id).isSolid()) {
            has_west = true;
        }
        temp_id = chunk.getAdjBlockId(x, y, z, Facing.EAST);
        if (temp_id == id || chunk.getBlock(temp_id) != null && chunk.getBlock(temp_id).isSolid()) {
            has_east = true;
        }
        temp_id = chunk.getAdjBlockId(x, y, z, Facing.SOUTH);
        if (temp_id == id || chunk.getBlock(temp_id) != null && chunk.getBlock(temp_id).isSolid()) {
            has_south = true;
        }
        temp_id = chunk.getAdjBlockId(x, y, z, Facing.NORTH);
        if (temp_id == id || chunk.getBlock(temp_id) != null && chunk.getBlock(temp_id).isSolid()) {
            has_north = true;
        }

        if (!has_north && !has_south && !has_west && !has_east) {
            has_north = true;
            has_south = true;
            has_west = true;
            has_east = true;
        }

        TextureDimensions side = this.side[blockData];
        TextureDimensions front = this.front[blockData];
        TextureDimensions top = this.top[blockData];

        // Now we should be able to actually draw stuff
        if (has_west && has_east) {
            Renderer.renderVertical(front, x - .5f + .5f, z+ .5f, x + .5f+ .5f, z+ .5f, y - .5f+ .5f, .98f);
        } else {
            if (has_west) {
                Renderer.renderVertical(front, x+ .5f, z+ .5f, x - .5f+ .5f, z+ .5f, y - .5f+ .5f, .98f);
            }
            if (has_east) {
                Renderer.renderVertical(front, x+ .5f, z+ .5f, x + .5f+ .5f, z+ .5f, y - .5f+ .5f, .98f);
            }
        }
        if (has_west) {
            Renderer.renderHorizontal(side, x - .5f+ .5f, z + top_width+ .5f, x - top_width+ .5f, z+ .5f, y + .48f+ .5f, false);
            Renderer.renderHorizontal(side, x - .5f+ .5f, z - top_width+ .5f, x - top_width+ .5f, z+ .5f, y + .48f+ .5f, false);
        }
        if (has_east) {
            Renderer.renderHorizontal(side, x + .5f+ .5f, z + top_width+ .5f, x + top_width+ .5f, z+ .5f, y + .48f+ .5f, false);
            Renderer.renderHorizontal(side, x + .5f+ .5f, z - top_width+ .5f, x + top_width+ .5f, z+ .5f, y + .48f+ .5f, false);
        }

        if (has_north && has_south) {
            Renderer.renderVertical(front, x+ .5f, z - .5f+ .5f, x+ .5f, z + .5f+ .5f, y - .5f+ .5f, .98f);
        } else {
            if (has_south) {
                Renderer.renderVertical(front, x+ .5f, z+ .5f, x+ .5f, z + .5f+ .5f, y - .5f+ .5f, .98f);
            }
            if (has_north) {
                Renderer.renderVertical(front, x+ .5f, z+ .5f, x+ .5f, z - .5f+ .5f, y - .5f+ .5f, .98f);
            }
        }
        if (has_south) {
            Renderer.renderHorizontal(side, x + top_width+ .5f, z + .5f+ .5f, x+ .5f, z + top_width+ .5f, y + .48f+ .5f, true);
            Renderer.renderHorizontal(side, x - top_width+ .5f, z + .5f+ .5f, x+ .5f, z + top_width+ .5f, y + .48f+ .5f, true);
        }
        if (has_north) {
            Renderer.renderHorizontal(side, x + top_width+ .5f, z - .5f+ .5f, x+ .5f, z - top_width+ .5f, y + .48f+ .5f, true);
            Renderer.renderHorizontal(side, x - top_width+ .5f, z - .5f+ .5f, x+ .5f, z - top_width+ .5f, y + .48f+ .5f, true);
        }

        // Finally, the center top square.  Technically we shouldn't draw past the edge, but whatever.
        Renderer.renderHorizontal(top, x + top_width+ .5f, z + top_width+ .5f, x - top_width+ .5f, z+ .5f, y + .48f+ .5f, false);
        Renderer.renderHorizontal(top, x + top_width+ .5f, z - top_width+ .5f, x - top_width+ .5f, z+ .5f, y + .48f+.5f, false);

    }
}
