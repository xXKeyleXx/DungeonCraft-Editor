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

public class Ladder extends Block{
    TextureDimensions ladder;
    public Ladder(short id) {
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
                    if (textureObject.containsKey("FRONT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("FRONT");
                        ladder = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y * 0.75f, false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT texture.");
                    }
                }
            }
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
                Renderer.renderBlockFace(ladder, x + .5f, y + .5f, z + .45f, Facing.SOUTH);
                break;
            case 3:
                // North
                Renderer.renderBlockFace(ladder, x + .5f, y + .5f, z + .55f, Facing.NORTH);
                break;
            case 4:
                // East
                Renderer.renderBlockFace(ladder, x + .45f, y + .5f, z + .5f, Facing.EAST);
                break;
            case 5:
            default:
                // West
                Renderer.renderBlockFace(ladder, x + .55f, y + .5f, z + .5f, Facing.WEST);
                break;
        }
    }
}
