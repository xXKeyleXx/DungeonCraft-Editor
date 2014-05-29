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

public class Slab extends Block {

    TextureDimensions[] TOP = new TextureDimensions[16];
    TextureDimensions[] SIDE = new TextureDimensions[16];
    TextureDimensions[] BOTTOM = new TextureDimensions[16];

    public Slab(short id) {
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
                        JSONArray side = (JSONArray) textureObject.get("SIDE");
                        SIDE[data] = new TextureDimensions(this.id, data, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y * 0.5f, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray top = (JSONArray) textureObject.get("TOP");
                        TOP[data] = new TextureDimensions(this.id, data, Integer.parseInt(top.get(0).toString()), Integer.parseInt(top.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " TOP texture.");
                    }
                    if (textureObject.containsKey("BOTTOM")) {
                        JSONArray bottom = (JSONArray) textureObject.get("BOTTOM");
                        BOTTOM[data] = new TextureDimensions(this.id, data, Integer.parseInt(bottom.get(0).toString()), Integer.parseInt(bottom.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " BOTTOM texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "SLAB";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        boolean top = ((blockData & 0x8) == 0x8);
        float offset = 0;
        if (top) {
            offset = 0.5f;
        }

        // Sides
        if (chunk.shouldRenderHalfHeightAdj(top, chunk.getAdjBlockId(x, y, z, Facing.NORTH), chunk.getData(x, y, z - 1))) {
            Renderer.renderVertical(SIDE[(blockData & 0x7)], x, z, x + 1, z, y + offset, .5f);
        }
        if (chunk.shouldRenderHalfHeightAdj(top, chunk.getAdjBlockId(x, y, z, Facing.SOUTH), chunk.getData(x, y, z + 1))) {
            Renderer.renderVertical(SIDE[(blockData & 0x7)], x, z + 1, x + 1, z + 1, y + offset, .5f);
        }
        if (chunk.shouldRenderHalfHeightAdj(top, chunk.getAdjBlockId(x, y, z, Facing.WEST), chunk.getData(x - 1, y, z))) {
            Renderer.renderVertical(SIDE[(blockData & 0x7)], x, z, x, z + 1, y + offset, .5f);
        }
        if (chunk.shouldRenderHalfHeightAdj(top, chunk.getAdjBlockId(x, y, z, Facing.EAST), chunk.getData(x + 1, y, z))) {
            Renderer.renderVertical(SIDE[(blockData & 0x7)], x + 1, z, x + 1, z + 1, y + offset, .5f);
        }

        // Top and bottom
        boolean render_bottom = true;
        boolean render_top = true;
        if (top) {
            if (y < 255) {
                short top_block = chunk.getAdjBlockId(x, y, z, Facing.UP);
                render_top = (top_block == 0 || (top_block > 0 && chunk.getBlock(top_block) != null && !chunk.getBlock(top_block).isSolid()));
            }
        } else {
            if (y > 0) {
                short bottom = chunk.getAdjBlockId(x, y, z, Facing.DOWN);
                render_bottom = (bottom == 0 || (bottom > 0 && chunk.getBlock(bottom) != null && !chunk.getBlock(bottom).isSolid()));
            }
        }

        // And now the actual top and bottom rendering
        if (render_bottom) {
            Renderer.renderBlockFace(BOTTOM[(blockData & 0x7)], x, y + offset, z, Facing.DOWN);
        }
        if (render_top) {
            Renderer.renderBlockFace(TOP[(blockData & 0x7)], x, y - 0.5f + offset, z, Facing.UP);
        }
    }
}