package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Facing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Chest extends Block {

    TextureDimensions FRONT;
    TextureDimensions SIDE;
    TextureDimensions BOTTOM;
    TextureDimensions FRONT_LEFT;
    TextureDimensions FRONT_RIGHT;
    TextureDimensions BACK_LEFT;
    TextureDimensions BACK_RIGHT;

    public Chest(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("FRONT")) {
            JSONArray front = (JSONArray) textures.get("FRONT");
            FRONT = new TextureDimensions(this.id, Integer.parseInt(front.get(0).toString()), Integer.parseInt(front.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " FRONT texture.");
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
        if (textures.containsKey("FRONT_LEFT")) {
            JSONArray frontLeft = (JSONArray) textures.get("FRONT_LEFT");
            FRONT_LEFT = new TextureDimensions(this.id, Integer.parseInt(frontLeft.get(0).toString()), Integer.parseInt(frontLeft.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " FRONT_LEFT texture.");
        }
        if (textures.containsKey("FRONT_RIGHT")) {
            JSONArray frontRight = (JSONArray) textures.get("FRONT_RIGHT");
            FRONT_RIGHT = new TextureDimensions(this.id, Integer.parseInt(frontRight.get(0).toString()), Integer.parseInt(frontRight.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " FRONT_RIGHT texture.");
        }
        if (textures.containsKey("BACK_LEFT")) {
            JSONArray backLeft = (JSONArray) textures.get("BACK_LEFT");
            BACK_LEFT = new TextureDimensions(this.id, Integer.parseInt(backLeft.get(0).toString()), Integer.parseInt(backLeft.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " BACK_LEFT texture.");
        }
        if (textures.containsKey("BACK_RIGHT")) {
            JSONArray backRight = (JSONArray) textures.get("BACK_RIGHT");
            BACK_RIGHT = new TextureDimensions(this.id, Integer.parseInt(backRight.get(0).toString()), Integer.parseInt(backRight.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " BACK_RIGHT texture.");
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

        GL11.glPushMatrix();
        GL11.glTranslatef(x+.5f, y+.5f, z+.5f);

        boolean have_left;
        boolean have_right;
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
                // Facing South
                have_right = (chunk.getAdjBlockId(x, y, z, Facing.EAST) == id);
                have_left = (chunk.getAdjBlockId(x, y, z, Facing.WEST) == id);
                break;
        }

        if (have_left) {
            Renderer.renderVertical(FRONT_RIGHT, -full, edges, edges, edges, -bottom, height);
            Renderer.renderVertical(BACK_RIGHT, -full, -edges, edges, -edges, -bottom, height);
            Renderer.renderVertical(SIDE, edges, -edges, edges, edges, -bottom, height);
            Renderer.renderHorizontal(BOTTOM, -full, -edges, edges, edges, edges, true);
            Renderer.renderHorizontal(BOTTOM, -full, -edges, edges, edges, -bottom, true);
        } else if (have_right) {
            Renderer.renderVertical(FRONT_LEFT, -edges, edges, full, edges, -bottom, height);
            Renderer.renderVertical(BACK_LEFT, -edges, -edges, full, -edges, -bottom, height);
            Renderer.renderVertical(SIDE, -edges, -edges, -edges, edges, -bottom, height);
            Renderer.renderHorizontal(BOTTOM, -edges, -edges, full, edges, edges, true);
            Renderer.renderHorizontal(BOTTOM, -edges, -edges, full, edges, -bottom, true);
        } else {
            Renderer.renderVertical(FRONT, -edges, edges, edges, edges, -bottom, height);
            Renderer.renderVertical(SIDE, -edges, -edges, edges, -edges, -bottom, height);
            Renderer.renderVertical(SIDE, -edges, -edges, -edges, edges, -bottom, height);
            Renderer.renderVertical(SIDE, edges, -edges, edges, edges, -bottom, height);
            Renderer.renderHorizontal(BOTTOM, -edges, -edges, edges, edges, edges, false);
            Renderer.renderHorizontal(BOTTOM, -edges, -edges, edges, edges, -bottom, false);
        }

        GL11.glPopMatrix();
    }
}
