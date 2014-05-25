package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.util.Facing;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Fence extends Block {
    TextureDimensions fence;
    private final float fence_postsize= .125f;
    private final float fence_postsize_h = fence_postsize / 2f;
    private final float fence_slat_height = .1875f;
    private final float fence_top_slat_offset = .375f;
    private final float fence_slat_start_offset = +.375f;

    public Fence(short id) {
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
                        fence = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y * 0.75f, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "FENCE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float slat_start = y + fence_slat_start_offset;
        boolean beta19_fences = true;

        // First the fencepost
        Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize + 0.5f, z - fence_postsize + 0.5f, y,1f);
        Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize + 0.5f, z - fence_postsize + 0.5f, y, 1f);
        Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize + 0.5f, z + fence_postsize + 0.5f, y, 1f);
        Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize + 0.5f, z + fence_postsize + 0.5f, y, 1f);
        if (y == 255 || !chunk.getBlock(chunk.getAdjBlockId(x, y, z, Facing.UP)).isSolid()) {
            Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize + 0.5f, z - fence_postsize + 0.5f, y +1f ,false);
        }

        short adj_id;
        byte adj_data;

        // Check for adjacent fences / fence gates in the -x direction
        adj_id = chunk.getAdjBlockId(x, y, z, Facing.WEST);
        if (adj_id == id) {
            // Fence to the West

            // Bottom slat
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start, false);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start + fence_slat_height, false);

            // Top slat
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, false);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - 1f + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height, false);
        } else if (adj_id > -1 && chunk.getBlock(adj_id).isSolid()) {
            // Solid block to the West

            // Bottom slat
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z + fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start, false);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_slat_height, false);

            // Top slat
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z + fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, false);
            Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height, false);
        } else if (adj_id > -1 && chunk.getBlock(adj_id) != null && chunk.getBlock(adj_id).getBlockType().equals("FENCE_GATE")) {
            // Fence Gate to the West
            adj_data = chunk.getData(x-1, y, z);
            if (adj_data != -1) {
                adj_data &= 0x3;
                if (adj_data == 0 || adj_data == 2) {
                    // Bottom slat
                    Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z + fence_postsize_h + 0.5f, slat_start, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start, fence_slat_height);
                    Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start, false);
                    Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_slat_height, false);

                    // Top slat
                    Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z + fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, false);
                    Renderer.renderHorizontal(fence, x - fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x - .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height, false);
                }
            }
        }

        // Check for adjacent fence gates in the +x direction
        adj_id = chunk.getAdjBlockId(x, y, z, Facing.EAST);
        if (adj_id > -1 && chunk.getBlock(adj_id) != null && chunk.getBlock(adj_id).getBlockType().equals("FENCE_GATE")) {
            // Fence Gate to the East
            adj_data = chunk.getData(x+1, y, z);
            if (adj_data != -1) {
                adj_data &= 0x3;
                if (adj_data == 0 || adj_data == 2) {
                    // Bottom slat
                    Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z + fence_postsize_h + 0.5f, slat_start, fence_slat_height);
                    Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start, false);
                    Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_slat_height,  false);

                    // Top slat
                    Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z + fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, false);
                    Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height,  false);
                }
            }
        } else if (adj_id > -1 && chunk.getBlock(adj_id).isSolid()) {
            // Solid block to the East

            // Bottom slat
            Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z + fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start, false);
            Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_slat_height, false);

            // Top slat
            Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z + fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x + fence_postsize + 0.5f, z - fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset, false);
            Renderer.renderHorizontal(fence, x + fence_postsize + 0.5f, z + fence_postsize_h + 0.5f, x + .5f, z - fence_postsize_h + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height, false);
        }

        // Check for adjacent fences / fence gates in the -z direction
        adj_id = chunk.getAdjBlockId(x, y, z, Facing.NORTH);
        if (adj_id == id) {
            // Bottom slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start + fence_slat_height, true);

            // Top slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start + fence_top_slat_offset, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - 1f + fence_postsize + 0.5f, slat_start + fence_top_slat_offset + fence_slat_height, true);
        } else if (adj_id > -1 && chunk.getBlock(adj_id).isSolid()) {
            // Solid block to the North

            // Bottom slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - .5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_slat_height, true);

            // Top slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset + fence_slat_height, true);
        } else if (adj_id > -1 && chunk.getBlock(adj_id) != null && chunk.getBlock(adj_id).getBlockType().equals("FENCE_GATE")) {
            // Fence Gate to the North
            adj_data = chunk.getData(x, y, z-1);
            if (adj_data != -1) {
                adj_data &= 0x3;
                if (adj_data == 1 || adj_data == 3) {
                    // Bottom slat
                    Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - .5f, slat_start, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start, true);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_slat_height, true);

                    // Top slat
                    Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset, true);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z - fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z - .5f, slat_start + fence_top_slat_offset + fence_slat_height, true);
                }
            }
        }

        // Check for adjacent fence gates in the +z direction
        adj_id = chunk.getAdjBlockId(x, y, z, Facing.SOUTH);
        if (adj_id > -1 && chunk.getBlock(adj_id) != null && chunk.getBlock(adj_id).getBlockType().equals("FENCE_GATE")) {
            // Fence Gate to the South
            adj_data = chunk.getData(x, y, z+1);
            if (adj_data != -1) {
                adj_data &= 0x3;
                if (adj_data == 1 || adj_data == 3) {
                    // Bottom slat
                    Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z + .5f, slat_start, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start, true);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_slat_height, true);

                    // Top slat
                    Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, fence_slat_height);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, true);
                    Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset + fence_slat_height, true);
                }
            }
        } else if (adj_id > -1 && chunk.getBlock(adj_id).isSolid()) {
            // Solid block to the South

            // Bottom slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z + .5f, slat_start, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_slat_height, true);

            // Top slat
            Renderer.renderVertical(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x + fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderVertical(fence, x - fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, fence_slat_height);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset, true);
            Renderer.renderHorizontal(fence, x + fence_postsize_h + 0.5f, z + fence_postsize + 0.5f, x - fence_postsize_h + 0.5f, z + .5f, slat_start + fence_top_slat_offset + fence_slat_height, true);
        }
    }
}