package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Facing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Door extends Block {
    TextureDimensions bottom;
    TextureDimensions top;

    public Door(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("BOTTOM")) {
            JSONArray bottom = (JSONArray) textures.get("BOTTOM");
            this.bottom = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(bottom.get(0).toString()), Integer.parseInt(bottom.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " BOTTOM texture.");
            }
        if (textures.containsKey("TOP")) {
            JSONArray top = (JSONArray) textures.get("TOP");
            this.top = new TextureDimensions(this.id, (byte) 0, Integer.parseInt(top.get(0).toString()), Integer.parseInt(top.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " TOP texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "DOOR";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte top_data;
        byte bottom_data;
        TextureDimensions door;
        if ((blockData & 0x8) == 0x0) {
            bottom_data = blockData;
            top_data = chunk.getData(x, y + 1, z);
            door= bottom;
        } else {
            top_data = blockData;
            bottom_data = chunk.getData(x, y - 1, z);
            door = top;
        }
        boolean hinge_on_left = (top_data & 0x1) == 1;
        boolean open = (bottom_data & 0x4) == 4;
        int dir = bottom_data & 0x3;

        if (dir == 0) {
            if (open) {
                if (hinge_on_left) {
                    Renderer.renderBlockFace(door, x, y, z - 0.01f, Facing.SOUTH);
                } else {
                    Renderer.renderBlockFace(door, x, y, z + 0.01f, Facing.NORTH);
                }
            } else {
                Renderer.renderBlockFace(door, x + 0.01f, y, z, Facing.WEST);
            }
        } else if (dir == 1) {
            if (open) {
                if (hinge_on_left) {
                    Renderer.renderBlockFace(door, x + 0.01f, y, z, Facing.WEST);
                } else {
                    Renderer.renderBlockFace(door, x - 0.01f, y, z, Facing.EAST);
                }
            } else {
                Renderer.renderBlockFace(door, x, y, z + 0.01f, Facing.NORTH);
            }
        } else if (dir == 2) {
            if (open) {
                if (hinge_on_left) {
                    Renderer.renderBlockFace(door, x, y, z + 0.01f, Facing.NORTH);
                } else {
                    Renderer.renderBlockFace(door, x, y, z - 0.01f, Facing.SOUTH);
                }
            } else {
                Renderer.renderBlockFace(door, x - 0.01f, y, z, Facing.EAST);
            }
        } else {
            if (open) {
                if (hinge_on_left) {
                    Renderer.renderBlockFace(door, x - 0.01f, y, z, Facing.EAST);
                } else {
                    Renderer.renderBlockFace(door, x + 0.01f, y, z, Facing.WEST);
                }
            } else {
                Renderer.renderBlockFace(door, x, y, z - 0.01f, Facing.SOUTH);
            }
        }
    }
}
