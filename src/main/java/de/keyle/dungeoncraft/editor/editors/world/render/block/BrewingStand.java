package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class BrewingStand extends Block {

    TextureDimensions base;
    TextureDimensions top;

    public BrewingStand(short id) {
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
                    if (textureObject.containsKey("BASE")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("BASE");
                        base = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " BASE texture.");
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("TOP");
                        top = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " TOP texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return null;
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float one = .0625f;
        float seven = .4375f;
        float eight = .5f;
        float postheight = .875f;

        GL11.glPushMatrix();
        GL11.glTranslatef(x+.5f, y+.5f, z+.5f);

        // Center post
        Renderer.renderNonstandardVertical(top, -seven, one-eight, -seven, seven, postheight+one-eight, seven);
        Renderer.renderNonstandardVertical(top, -seven,one-eight, seven, seven, postheight+one-eight, -seven);

        Renderer.renderHorizontal(base, -seven, -seven, seven, seven, -seven, true);
        Renderer.renderVertical(base, -seven, seven, -seven, -seven, -eight, one);
        Renderer.renderVertical(base, -seven, seven, seven, seven, -eight, one);
        Renderer.renderVertical(base, seven, -seven, -seven, -seven, -eight, one);
        Renderer.renderVertical(base, seven, seven, seven, -seven, -eight, one);

        GL11.glPopMatrix();
    }
}
