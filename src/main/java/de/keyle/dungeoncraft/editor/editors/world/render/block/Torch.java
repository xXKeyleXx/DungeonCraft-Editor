package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Torch extends Block {

    TextureDimensions torch;

    public Torch(short id) {
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
                        torch = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "TORCH";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        byte data = chunk.getData(x, y, z);
        data &= 0xF;
        switch (data) {
            case 1:
                Renderer.renderRectDecoration(torch, x, y, z, -30, 0f, 1f, -.6f, 0f);
                return;
            case 2:
                Renderer.renderRectDecoration(torch, x, y, z, 30, 0f, 1f, .6f, 0f);
                return;
            case 3:
                Renderer.renderRectDecoration(torch, x, y, z, 30, 1f, 0f, 0f, -.6f);
                return;
            case 4:
                Renderer.renderRectDecoration(torch, x, y, z, -30, 1f, 0f, 0f, .6f);
                return;
            default:
                Renderer.renderRectDecoration(torch, x, y, z, 0, 0, 0, 0, 0);
                return;
        }
    }
}
