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

public class Carpet extends Block {
    TextureDimensions[] SIDE = new TextureDimensions[16];

    public Carpet(short id) {
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
                        SIDE[data] = new TextureDimensions(this.id, data, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "CARPET";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        // The plate itself
        Renderer.renderHorizontal(SIDE[blockData], x + 1, z + 1, x, z, y + .05f , false);

        // Sides
        Renderer.renderVertical(SIDE[blockData], x + 1, z + 1, x + 1, z, y, 0.05f);
        Renderer.renderVertical(SIDE[blockData], x, z + 1, x, z, y, 0.05f);
        Renderer.renderVertical(SIDE[blockData], x + 1, z + 1, x, z + 1, y, 0.05f);
        Renderer.renderVertical(SIDE[blockData], x + 1, z, x, z, y, 0.05f);
    }
}