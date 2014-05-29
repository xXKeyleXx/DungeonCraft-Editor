package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class WallSign extends Block {
    TextureDimensions FRONT;

    public WallSign(short id) {
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
    }

    @Override
    public String getBlockType() {
        return "WALLSIGN";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float faceX1, faceX2;
        float faceZ1, faceZ2;
        float back_dX, back_dZ;
        float sign_length = 0.5f;


        switch (blockData) {
            case 2:
                // North
                faceX1 = x - sign_length + .5f;
                faceX2 = x + sign_length + .5f;
                faceZ1 = z + .95f;
                faceZ2 = z + .95f;
                back_dX = 0f;
                back_dZ = 0.05f;
                break;
            case 3:
                // South
                faceX1 = x - sign_length + .5f;
                faceX2 = x + sign_length + .5f;
                faceZ1 = z + .05f;
                faceZ2 = z + .05f;
                back_dX = 0f;
                back_dZ = -0.05f;
                break;
            case 4:
                // West
                faceX1 = x + .95f;
                faceX2 = x + .95f;
                faceZ1 = z - sign_length + .5f;
                faceZ2 = z + sign_length + .5f;
                back_dX = 0.05f;
                back_dZ = 0f;
                break;
            case 5:
            default:
                // East
                faceX1 = x + .05f;
                faceX2 = x + .05f;
                faceZ1 = z - sign_length + .5f;
                faceZ2 = z + sign_length + .5f;
                back_dX = -0.05f;
                back_dZ = 0f;
                break;
        }

        // Face
        Renderer.renderVertical(FRONT, faceX1, faceZ1, faceX2, faceZ2, y + .3f, 0.5f);

        // Sides
        Renderer.renderVertical(FRONT, faceX1, faceZ1, faceX1 + back_dX, faceZ1 + back_dZ, y + .3f, 0.5f);
        Renderer.renderVertical(FRONT, faceX2, faceZ2, faceX2 + back_dX, faceZ2 + back_dZ, y + .3f, 0.5f);

        // Top/Bottom
        Renderer.renderHorizontal(FRONT, faceX1, faceZ1, faceX2 + back_dX, faceZ2 + back_dZ, y + .3f, false);
        Renderer.renderHorizontal(FRONT, faceX1, faceZ1, faceX2 + back_dX, faceZ2 + back_dZ, y + .8f, false);
    }
}