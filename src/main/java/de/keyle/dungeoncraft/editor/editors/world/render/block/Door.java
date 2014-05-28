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

public class Door extends Block {
    TextureDimensions bottom;
    TextureDimensions top;

    public Door(short id) {
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
                    if (textureObject.containsKey("BOTTOM")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("BOTTOM");
                        bottom = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " BOTTOM texture.");
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("TOP");
                        top = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " TOP texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "DOOR";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte data = chunk.getData(x, y, z);

        // Anvil-formatted levels use a different data structure for doors, so we've got to
        // split up processing.
        //
        // Technically the new door data happened one weekly release before Anvil, but at
        // the time there was no reasonable way of determining if it was going to be
        // using the new format or not, so we're just going to go ahead and check for
        // Anvil maps.  Good enough.  People using 12w06a will just have to cope.  :)
        byte top_data;
        byte bottom_data;
        TextureDimensions door;
        if ((data & 0x8) == 0x0) {
            bottom_data = data;
            top_data = chunk.getData(x, y + 1, z);
            door= bottom;
        } else {
            top_data = data;
            bottom_data = chunk.getData(x, y - 1, z);
            door = top;
        }
        boolean hinge_on_left = ((top_data & 0x1) == 0x1);
        boolean open = ((bottom_data & 0x4) == 0x4);
        int dir = (bottom_data & 0x3);

        if ((dir == 0 && !open) || (open && ((dir == 1 && hinge_on_left) || (dir == 3 && !hinge_on_left)))) {
            // West
            Renderer.renderBlockFace(door, x+.5f, y+.5f, z+.5f, Facing.WEST);
        } else if ((dir == 1 && !open) || (open && ((dir == 0 && !hinge_on_left) || (dir == 2 && hinge_on_left)))) {
            // North
            Renderer.renderBlockFace(door, x+.5f, y+.5f, z+.5f, Facing.NORTH);
        } else if ((dir == 2 && !open) || (open && ((dir == 1 && !hinge_on_left) || (dir == 3 && hinge_on_left)))) {
            // East
            Renderer.renderBlockFace(door, x+.5f, y+.5f, z+.5f, Facing.EAST);
        } else {
            // South
            Renderer.renderBlockFace(door, x+.5f, y+.5f, z+.5f, Facing.SOUTH);
        }
    }
}
