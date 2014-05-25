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

public class DragonEgg extends Block{
    TextureDimensions egg;
    public DragonEgg(short id) {
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
                        egg = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "DRAGON_EGG";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float bottom = -.5f;
        float bottom_base = -.48f;
        float top = .25f;
        float height = .75f;
        float sides = .4375f;

        GL11.glPushMatrix();
        GL11.glTranslatef(x+.5f, y+.5f, z+.5f);

        // Sides
        Renderer.renderVertical(egg, -sides, -sides, sides, -sides, bottom, height);
        Renderer.renderVertical(egg, -sides, sides, sides, sides, bottom, height);
        Renderer.renderVertical(egg, -sides, sides, -sides, -sides, bottom, height);
        Renderer.renderVertical(egg, sides, sides, sides, -sides, bottom, height);

        // Top + Bottom
        Renderer.renderHorizontal(egg, -sides, -sides, sides, sides, top, false);
        Renderer.renderHorizontal(egg, -sides, -sides, sides, sides, bottom_base, false);

        GL11.glPopMatrix();
    }
}
