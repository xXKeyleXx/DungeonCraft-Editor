package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Cauldron extends Block{
    TextureDimensions TOP;
    TextureDimensions SIDE;
    TextureDimensions INNER;
    TextureDimensions BOTTOM;
    TextureDimensions WATER;

    public Cauldron(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("INNER")) {
            JSONArray inner = (JSONArray) textures.get("INNER");
            INNER = new TextureDimensions(this.id, Integer.parseInt(inner.get(0).toString()), Integer.parseInt(inner.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " INNER texture.");
        }
        if (textures.containsKey("TOP")) {
            JSONArray top = (JSONArray) textures.get("TOP");
            TOP = new TextureDimensions(this.id, Integer.parseInt(top.get(0).toString()), Integer.parseInt(top.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " TOP texture.");
        }
        if (textures.containsKey("SIDE")) {
            JSONArray side = (JSONArray) textures.get("SIDE");
            SIDE = new TextureDimensions(this.id, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " SIDE texture.");
        }
        if (textures.containsKey("BOTTOM")) {
            JSONArray bottom = (JSONArray) textures.get("BOTTOM");
            BOTTOM = new TextureDimensions(this.id, Integer.parseInt(bottom.get(0).toString()), Integer.parseInt(bottom.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " BOTTOM texture.");
        }
        if (textures.containsKey("WATER")) {
            JSONArray water = (JSONArray) textures.get("WATER");
            WATER = new TextureDimensions(this.id, Integer.parseInt(water.get(0).toString()), Integer.parseInt(water.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " WATER texture.");
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
        Renderer.renderHorizontal(this.BOTTOM, edge, edge, -edge, -edge, base, false);

        // Sides
        Renderer.renderVertical(SIDE, edge, edge, -edge, edge, bottom, height);
        Renderer.renderVertical(SIDE, edge, -edge, -edge, -edge, bottom, height);
        Renderer.renderVertical(SIDE, edge, edge, edge, -edge, bottom, height);
        Renderer.renderVertical(SIDE, -edge, edge, -edge, -edge, bottom, height);

        // Inside faces
        Renderer.renderVertical(INNER, inside, inside, -inside, inside, base, insideheight);
        Renderer.renderVertical(INNER, inside, -inside, -inside, -inside, base, insideheight);
        Renderer.renderVertical(INNER, inside, inside, inside, -inside, base, insideheight);
        Renderer.renderVertical(INNER, -inside, inside, -inside, -inside, base, insideheight);

        // Contents
        if (data > 0) {
            Renderer.renderHorizontal(WATER, inside, inside, -inside, -inside, base + ((insideheight - .0625f) * (data / 3f)), false);
        }

        // Top
        Renderer.renderHorizontal(TOP, edge, edge, -edge, -edge, height - .5f, false);

        GL11.glPopMatrix();
    }
}
