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

public class Cauldron extends Block{
    TextureDimensions top;
    TextureDimensions side;
    TextureDimensions inner;
    TextureDimensions bottom;
    TextureDimensions water;

    public Cauldron(short id) {
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
                    if (textureObject.containsKey("INNER")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("INNER");
                        inner = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " INNER texture.");
                    }
                    if (textureObject.containsKey("TOP")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("TOP");
                        top = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " TOP texture.");
                    }
                    if (textureObject.containsKey("SIDE")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("SIDE");
                        side = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " SIDE texture.");
                    }
                    if (textureObject.containsKey("BOTTOM")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("BOTTOM");
                        bottom = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " BOTTOM texture.");
                    }
                    if (textureObject.containsKey("WATER")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("WATER");
                        water = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " WATER texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "CAULDRON";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float edge = .48f;
        float bottom = -.5f;
        float height = 1f;
        float base = -.3125f;
        float inside = (.375f * (edge * 2f)); // Modified to account for scaling
        float insideheight = 1f - (.5f + base);

        byte data = chunk.getData(x, y, z);
        if (data > 3) {
            data = 3;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(x+.5f, y+.5f, z+.5f);

        // Base
        Renderer.renderHorizontal(this.bottom, edge, edge, -edge, -edge, base, false);

        // Sides
        Renderer.renderVertical(side, edge, edge, -edge, edge, bottom, height);
        Renderer.renderVertical(side, edge, -edge, -edge, -edge, bottom, height);
        Renderer.renderVertical(side, edge, edge, edge, -edge, bottom, height);
        Renderer.renderVertical(side, -edge, edge, -edge, -edge, bottom, height);

        // Inside faces
        Renderer.renderVertical(inner, inside, inside, -inside, inside, base, insideheight);
        Renderer.renderVertical(inner, inside, -inside, -inside, -inside, base, insideheight);
        Renderer.renderVertical(inner, inside, inside, inside, -inside, base, insideheight);
        Renderer.renderVertical(inner, -inside, inside, -inside, -inside, base, insideheight);

        // Contents
        if (data > 0) {
            Renderer.renderHorizontal(water, inside, inside, -inside, -inside, base + ((insideheight - .0625f) * (data / 3f)),false);
        }

        // Top
        Renderer.renderHorizontal(top, edge, edge, -edge, -edge, height - .5f,false);

        GL11.glPopMatrix();
    }
}
