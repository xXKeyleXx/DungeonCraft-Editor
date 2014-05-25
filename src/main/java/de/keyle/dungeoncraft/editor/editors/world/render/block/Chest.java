package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Facing;
import de.keyle.dungeoncraft.editor.util.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Chest extends Block {

    TextureDimensions front;
    TextureDimensions side;
    TextureDimensions bottom;
    TextureDimensions front_left;
    TextureDimensions front_right;
    TextureDimensions back_left;
    TextureDimensions back_right;

    public Chest(short id) {
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
                        front = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT texture.");
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
                    if (textureObject.containsKey("FRONT_LEFT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("FRONT_LEFT");
                        front_left = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT_LEFT texture.");
                    }
                    if (textureObject.containsKey("FRONT_RIGHT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("FRONT_RIGHT");
                        front_right = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " FRONT_RIGHT texture.");
                    }
                    if (textureObject.containsKey("BACK_LEFT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("BACK_LEFT");
                        back_left = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " BACK_LEFT texture.");
                    }
                    if (textureObject.containsKey("BACK_RIGHT")) {
                        JSONArray sideArray = (JSONArray) textureObject.get("BACK_RIGHT");
                        back_right = new TextureDimensions(this.id, data, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y , false);
                    } else {
                        throw new BlockTypeLoadException(id + " BACK_RIGHT texture.");
                    }
                }
            }
        }
    }

    @Override
    public String getBlockType() {
        return "CHEST";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float edges = .45f;
        float bottom = .49f;
        float height = .94f;
        float full = .5f;

        byte orientation = chunk.getData(x, y, z);

        // Use GL to rotate these properly
        GL11.glPushMatrix();
        GL11.glTranslatef(x+.5f, y+.5f, z+.5f);

        // Find out if we have adjacent chests, and rotate.  Our "have_right" and
        // "have_left" booleans are a little bit at odds with the orientation of the
        // chest itself, since my "left/right" is looking *at* the front of the
        // chest, not pointing out in the direction the chest is Facing.  Alas.
        boolean have_left = false;
        boolean have_right = false;
        switch (orientation) {
            case 4:
                // Facing West
                have_right = (chunk.getAdjBlockId(x, y, z, Facing.SOUTH) == id);
                have_left = (chunk.getAdjBlockId(x, y, z, Facing.NORTH) == id);
                GL11.glRotatef(270f, 0f, 1f, 0f);
                break;
            case 5:
                // Facing East
                have_right = (chunk.getAdjBlockId(x, y, z, Facing.NORTH) == id);
                have_left = (chunk.getAdjBlockId(x, y, z, Facing.SOUTH) == id);
                GL11.glRotatef(90f, 0f, 1f, 0f);
                break;
            case 2:
                // Facing North
                have_right = (chunk.getAdjBlockId(x, y, z, Facing.WEST) == id);
                have_left = (chunk.getAdjBlockId(x, y, z, Facing.EAST) == id);
                GL11.glRotatef(180f, 0f, 1f, 0f);
                break;
            case 3:
            default:
                // Facing South (this is what chests with a data of "0" will show up as,
                // weirdly, in Minecraft itself)
                have_right = (chunk.getAdjBlockId(x, y, z, Facing.EAST) == id);
                have_left = (chunk.getAdjBlockId(x, y, z, Facing.WEST) == id);
                break;
        }

        // Render appropriately
        if (have_left) {
            Renderer.renderVertical(front_right, -full, edges, edges, edges, -bottom, height);
            Renderer.renderVertical(back_right, -full, -edges, edges, -edges, -bottom, height);
            Renderer.renderVertical(side, edges, -edges, edges, edges, -bottom, height);
            Renderer.renderHorizontal(this.bottom, -full, -edges, edges, edges, edges, true);
            Renderer.renderHorizontal(this.bottom, -full, -edges, edges, edges, -bottom, true);
        } else if (have_right) {
            Renderer.renderVertical(front_left, -edges, edges, full, edges, -bottom, height);
            Renderer.renderVertical(back_left, -edges, -edges, full, -edges, -bottom, height);
            Renderer.renderVertical(side, -edges, -edges, -edges, edges, -bottom, height);
            Renderer.renderHorizontal(this.bottom, -edges, -edges, full, edges, edges, true);
            Renderer.renderHorizontal(this.bottom, -edges, -edges, full, edges, -bottom, true);
        } else {
            Renderer.renderVertical(front, -edges, edges, edges, edges, -bottom, height);
            Renderer.renderVertical(side, -edges, -edges, edges, -edges, -bottom, height);
            Renderer.renderVertical(side, -edges, -edges, -edges, edges, -bottom, height);
            Renderer.renderVertical(side, edges, -edges, edges, edges, -bottom, height);
            Renderer.renderHorizontal(this.bottom, -edges, -edges, edges, edges, edges,false);
            Renderer.renderHorizontal(this.bottom, -edges, -edges, edges, edges, -bottom,false);
        }

        // Pop the matrix
        GL11.glPopMatrix();
    }
}
