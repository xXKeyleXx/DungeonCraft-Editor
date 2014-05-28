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

public class Snow extends Block {
    
    TextureDimensions snow;
    
    public Snow(short id) {
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
                        snow = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }    
    }

    @Override
    public String getBlockType() {
        return "SNOW";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float edge = 0.5f;
        float top = -.375f;
        float height = .125f;
        short adj;

        // The top face
        Renderer.renderHorizontal(snow, x + edge + .5f, z + edge+ .5f, x - edge+ .5f, z - edge+ .5f, y + top + .5f,false);

        // East
        adj = chunk.getAdjBlockId(x, y, z, Facing.EAST);
        if (adj != id && chunk.getBlock(adj)!= null && !chunk.getBlock(adj).isSolid()) {
            Renderer.renderVertical(snow, x + edge+ .5f, z + edge+ .5f, x + edge+ .5f, z - edge+ .5f, y, height);
        }

        // West
        adj = chunk.getAdjBlockId(x, y, z, Facing.WEST);
        if (adj != id && chunk.getBlock(adj)!= null && !chunk.getBlock(adj).isSolid()) {
            Renderer.renderVertical(snow, x - edge+ .5f, z + edge+ .5f, x - edge+ .5f, z - edge+ .5f, y, height);
        }

        // South
        adj = chunk.getAdjBlockId(x, y, z, Facing.SOUTH);
        if (adj != id && chunk.getBlock(adj)!= null && !chunk.getBlock(adj).isSolid()) {
            Renderer.renderVertical(snow, x + edge+ .5f, z + edge+ .5f, x - edge+ .5f, z + edge+ .5f, y, height);
        }

        // North
        adj = chunk.getAdjBlockId(x, y, z, Facing.NORTH);
        if (adj != id && chunk.getBlock(adj)!= null && !chunk.getBlock(adj).isSolid()) {
            Renderer.renderVertical(snow, x + edge+ .5f, z - edge+ .5f, x - edge+ .5f, z - edge+ .5f, y, height);
        }
    }
}
