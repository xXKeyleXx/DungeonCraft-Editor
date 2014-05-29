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

public class Vine extends Block {
    TextureDimensions vine;
    public Vine(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("FRONT")) {
            JSONArray sideArray = (JSONArray) textures.get("FRONT");
            vine = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y * 0.75f, false);
        } else {
            throw new BlockTypeLoadException(id + " FRONT texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "VINE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte data = chunk.getData(x, y, z);
        boolean rendered = false;
        if ((data & 1) == 1) {
            // South
            Renderer.renderBlockFace(vine, x, y, z - 0.01f, Facing.SOUTH);
            rendered = true;
        }
        if ((data & 2) == 2) {
            // West
            Renderer.renderBlockFace(vine, x + 0.01f, y, z, Facing.WEST);
            rendered = true;
        }
        if ((data & 4) == 4) {
            // North
            Renderer.renderBlockFace(vine, x, y, z + 0.01f, Facing.NORTH);
            rendered = true;
        }
        if ((data & 8) == 8) {
            // East
            Renderer.renderBlockFace(vine, x - 0.01f, y, z, Facing.EAST);
            rendered = true;
        }
        if (data == 0 || (rendered && chunk.getBlockType(x, y+1, z).isSolid())) {
            // Top
            Renderer.renderBlockFace(vine, x, y - 0.01f, z, Facing.UP);
        }
    }
}
