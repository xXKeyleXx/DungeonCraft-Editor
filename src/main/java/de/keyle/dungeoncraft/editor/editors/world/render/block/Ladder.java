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

public class Ladder extends Block{
    TextureDimensions LADDER;
    public Ladder(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("FRONT")) {
            JSONArray sideArray = (JSONArray) textures.get("FRONT");
            LADDER = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " FRONT texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "LADDER";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte data = chunk.getData(x, y, z);
        switch (data) {
            case 2:
                // South
                Renderer.renderBlockFace(LADDER, x, y, z - 0.01f, Facing.SOUTH);
                break;
            case 3:
                // North
                Renderer.renderBlockFace(LADDER, x, y, z + 0.01f, Facing.NORTH);
                break;
            case 4:
                // East
                Renderer.renderBlockFace(LADDER, x - 0.01f, y, z, Facing.EAST);
                break;
            case 5:
            default:
                // West
                Renderer.renderBlockFace(LADDER, x + 0.01f, y, z, Facing.WEST);
                break;
        }
    }
}
