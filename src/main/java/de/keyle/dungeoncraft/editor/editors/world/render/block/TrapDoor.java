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

public class TrapDoor extends Block {
    TextureDimensions TOP;
    TextureDimensions SIDE;

    public TrapDoor(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("SIDE")) {
            JSONArray side = (JSONArray) textures.get("SIDE");
            TOP = new TextureDimensions(this.id, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " SIDE texture.");
        }
        if (textures.containsKey("SIDE")) {
            JSONArray side = (JSONArray) textures.get("SIDE");
            SIDE = new TextureDimensions(this.id, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y * 0.1875f, false);
        } else {
            throw new BlockTypeLoadException(id + " SIDE texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "TRAPDOOR";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float twidth = .1f;
        //float twidth_h = twidth/2f;
        float toff = .02f;

        boolean swung = false;
        if ((blockData & 0x4) == 0x4) {
            swung = true;
        }
        int dir = (blockData & 0x3);


        // Use GL to rotate these properly
        GL11.glPushMatrix();
        GL11.glTranslatef(x + .5f, y + 1.4f, z + .5f);
        if (swung) {
            if (dir == 0) {
                // South
                GL11.glRotatef(-90f, 1f, 0f, 0f);
            } else if (dir == 1) {
                // North
                GL11.glRotatef(90f, 1f, 0f, 0f);
            } else if (dir == 2) {
                // East
                GL11.glRotatef(90f, 0f, 0f, 1f);
            } else {
                // West
                GL11.glRotatef(-90f, 0f, 0f, 1f);
            }
        }

        // First the faces
        //this.renderHorizontal(textureId, .5f-toff, .5f-toff, -.5f+toff, -.5f+toff, -.5f+toff);
        Renderer.renderHorizontal(TOP, .5f - toff, .5f - toff, -.5f + toff, -.5f + toff, -.5f + toff + twidth, false);

        // Now the sides
        Renderer.renderNonstandardVertical(SIDE,
                .5f - toff, -.5f + toff, .5f - toff,
                -.5f + toff, -.5f + toff + twidth, .5f - toff);
        Renderer.renderNonstandardVertical(SIDE,
                .5f - toff, -.5f + toff, .5f - toff,
                .5f - toff, -.5f + toff + twidth, -.5f + toff);
        Renderer.renderNonstandardVertical(SIDE,
                -.5f + toff, -.5f + toff, -.5f + toff,
                -.5f + toff, -.5f + toff + twidth, .5f - toff);
        Renderer.renderNonstandardVertical(SIDE,
                -.5f + toff, -.5f + toff, -.5f + toff,
                .5f - toff, -.5f + toff + twidth, -.5f + toff);

        GL11.glPopMatrix();
    }
}