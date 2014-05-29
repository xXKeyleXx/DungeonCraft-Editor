package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Button extends Block{
    TextureDimensions SIDE;

    public Button(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("SIDE")) {
            JSONArray side = (JSONArray) textures.get("SIDE");
            SIDE = new TextureDimensions(this.id, Integer.parseInt(side.get(0).toString()), Integer.parseInt(side.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " SIDE texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "BUTTON";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float faceX1, faceX2;
        float faceZ1, faceZ2;
        float back_dX, back_dZ;
        float button_radius = .1f;

        switch (blockData) {
            case 1:
                // East
                faceX1 = x + button_radius;
                faceX2 = x + button_radius;
                faceZ1 = z - button_radius + .5f;
                faceZ2 = z + button_radius + .5f;
                back_dX = -button_radius;
                back_dZ = 0;
                break;
            case 2:
                // West
                faceX1 = x + 1 - button_radius;
                faceX2 = x + 1 - button_radius;
                faceZ1 = z - button_radius + .5f;
                faceZ2 = z + button_radius + .5f;
                back_dX = button_radius;
                back_dZ = 0;
                break;
            case 3:
                // South
                faceX1 = x - button_radius + .5f;
                faceX2 = x + button_radius + .5f;
                faceZ1 = z + button_radius;
                faceZ2 = z + button_radius;
                back_dX = 0;
                back_dZ = -button_radius;
                break;
            case 4:
            default:
                // North
                faceX1 = x - button_radius + .5f;
                faceX2 = x + button_radius + .5f;
                faceZ1 = z + 1 - button_radius;
                faceZ2 = z + 1 - button_radius;
                back_dX = 0;
                back_dZ = button_radius;
                break;
        }

        // Button face
        Renderer.renderVertical(SIDE, faceX1, faceZ1, faceX2, faceZ2, y - button_radius + .5f, button_radius * 2);

        // Sides
        Renderer.renderVertical(SIDE, faceX1, faceZ1, faceX1 + back_dX, faceZ1 + back_dZ, y - button_radius + .5f, button_radius * 2);
        Renderer.renderVertical(SIDE, faceX2, faceZ2, faceX2 + back_dX, faceZ2 + back_dZ, y - button_radius + .5f, button_radius * 2);

        // Top/Bottom
        Renderer.renderHorizontal(SIDE, faceX1, faceZ1, faceX2 + back_dX, faceZ2 + back_dZ, y - button_radius + .5f, false);
        Renderer.renderHorizontal(SIDE, faceX1, faceZ1, faceX2 + back_dX, faceZ2 + back_dZ, y + button_radius + .5f, false);

    }
}
