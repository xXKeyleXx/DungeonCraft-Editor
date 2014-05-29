package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Lever extends Block {
    TextureDimensions base;
    TextureDimensions lever;

    static float box_height = .1875f;
    static float box_length = .25f;
    static float box_width = .375f;
    static float box_height_h = box_height * 0.5f;
    static float box_length_h = box_length * 0.5f;
    static float box_width_h = box_width * 0.5f;

    public Lever(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("BASE")) {
            JSONArray sideArray = (JSONArray) textures.get("BASE");
            base = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " BASE texture.");
        }
        if (textures.containsKey("LEVER")) {
            JSONArray sideArray = (JSONArray) textures.get("LEVER");
            lever = new TextureDimensions(this.id, Integer.parseInt(sideArray.get(0).toString()), Integer.parseInt(sideArray.get(1).toString()), TEX16, TEX_Y * 0.625f, false);
        } else {
            throw new BlockTypeLoadException(id + " LEVER texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "LEVER";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        //TODO Render the 4th side
        byte data = chunk.getData(x, y, z);
        boolean thrown = false;
        if ((data & 0x8) == 0x8) {
            thrown = true;
        }
        data &= 7;

        // First draw the base
        switch (data) {
            case 1:
                Renderer.renderVertical(base, x + 1f - box_height, z + 0.5f - box_width_h, x + 1, z + 0.5f + box_width_h, y - box_length_h + 0.5f, box_length);
                Renderer.renderVertical(base, x + 1f - box_height_h, z - box_width, x + box_height, z - box_width, y - box_length, box_length);
                Renderer.renderVertical(base, x + 1f + box_height_h, z + box_width, x + box_height, z - box_width, y - box_length, box_length);
                Renderer.renderHorizontal(base, x + 1f, z - box_width, x + box_height, z + box_width, y, false);
                Renderer.renderHorizontal(base, x + 1f, z - box_width, x + box_height, z + box_width, y + box_length, false);
                break;
            case 2:
                Renderer.renderVertical(base, x + .5f, z + box_width, x + .5f - box_height, z + box_width, y - box_length, box_length);
                Renderer.renderVertical(base, x + .5f, z - box_width, x + .5f - box_height, z - box_width, y - box_length, box_length);
                Renderer.renderVertical(base, x + .5f - box_height, z + box_width, x + .5f - box_height, z - box_width, y - box_length, box_length);
                Renderer.renderHorizontal(base, x + .5f, z - box_width, x + .5f - box_height, z + box_width, y, false);
                Renderer.renderHorizontal(base, x + .5f, z - box_width, x + .5f - box_height, z + box_width, y + box_length, false);
                break;
            case 3:
                Renderer.renderVertical(base, x - box_width, z - .5f, x - box_width, z - .5f + box_height, y - box_length, box_length);
                Renderer.renderVertical(base, x + box_width, z - .5f, x + box_width, z - .5f + box_height, y - box_length, box_length);
                Renderer.renderVertical(base, x - box_width, z - .5f + box_height, x + box_width, z - .5f + box_height, y - box_length, box_length);
                Renderer.renderHorizontal(base, x - box_width, z - .5f, x + box_width, z - .5f + box_height, y, false);
                Renderer.renderHorizontal(base, x - box_width, z - .5f, x + box_width, z - .5f + box_height, y + box_length, false);
                break;
            case 4:
                Renderer.renderVertical(base, x - box_width, z + .5f, x - box_width, z + .5f - box_height, y, box_length);
                Renderer.renderVertical(base, x + box_width, z + .5f, x + box_width, z + .5f - box_height, y - box_length, box_length);
                Renderer.renderVertical(base, x - box_width, z + .5f - box_height, x + box_width, z + .5f - box_height, y - box_length, box_length);
                Renderer.renderHorizontal(base, x - box_width, z + .5f, x + box_width, z + .5f - box_height, y, false);
                Renderer.renderHorizontal(base, x - box_width, z + .5f, x + box_width, z + .5f - box_height, y + box_length, false);
                break;
            case 5:
                Renderer.renderVertical(base, x - box_width + .5f, z + box_length + .5f, x - box_width + .5f, z - box_length + .5f, y, box_height);
                Renderer.renderVertical(base, x + box_width + .5f, z + box_length + .5f, x + box_width + .5f, z - box_length + .5f, y, box_height);
                Renderer.renderVertical(base, x - box_width + .5f, z + box_length + .5f, x + box_width + .5f, z + box_length + .5f, y, box_height);
                Renderer.renderVertical(base, x + box_width + .5f, z - box_length + .5f, x - box_width + .5f, z - box_length + .5f, y, box_height);
                Renderer.renderHorizontal(base, x - box_width + .5f, z - box_length + .5f, x + box_width + .5f, z + box_length + .5f, y + box_height, false);
                break;
            case 6:
            default:
                Renderer.renderVertical(base, x - box_length + .5f, z + box_width + .5f, x - box_length + .5f, z - box_width + .5f, y, box_height);
                Renderer.renderVertical(base, x + box_length + .5f, z + box_width + .5f, x + box_length + .5f, z - box_width + .5f, y, box_height);
                Renderer.renderVertical(base, x - box_length + .5f, z + box_width + .5f, x + box_length + .5f, z + box_width + .5f, y, box_height);
                Renderer.renderVertical(base, x + box_length + .5f, z - box_width + .5f, x - box_length + .5f, z - box_width + .5f, y, box_height);
                Renderer.renderHorizontal(base, x - box_length + .5f, z - box_width + .5f, x + box_length + .5f, z + box_width + .5f, y + box_height, false);
                break;
        }

        // Now draw the lever itself
        if (thrown) {
            switch (data) {
                case 1:
                    Renderer.renderRectDecoration(lever, x, y + 1, z, -135, 0f, 1f, .6f, 0f);
                    break;
                case 2:
                    Renderer.renderRectDecoration(lever, x, y + 1, z, 135, 0f, 1f, -.6f, 0f);
                    break;
                case 3:
                    Renderer.renderRectDecoration(lever, x, y + 1, z, 135, 1f, 0f, 0f, .6f);
                    break;
                case 4:
                    Renderer.renderRectDecoration(lever, x, y + 1, z, -135, 1f, 0f, 0f, -.6f);
                    break;
                case 5:
                    Renderer.renderRectDecoration(lever, x, y, z, -45, 1f, 0f, 0f, 0f);
                    break;
                case 6:
                    Renderer.renderRectDecoration(lever, x, y, z, 45, 0f, 1f, 0f, 0f);
                    break;
            }
        } else {
            switch (data) {
                case 1:
                    Renderer.renderRectDecoration(lever, x, y, z, -45, 0f, 1f, -.6f, 0f);
                    break;
                case 2:
                    Renderer.renderRectDecoration(lever, x, y, z, 45, 0f, 1f, .6f, 0f);
                    break;
                case 3:
                    Renderer.renderRectDecoration(lever, x, y, z, 45, 1f, 0f, 0f, -.6f);
                    break;
                case 4:
                    Renderer.renderRectDecoration(lever, x, y, z, -45, 1f, 0f, 0f, .6f);
                    break;
                case 5:
                    Renderer.renderRectDecoration(lever, x, y, z, 45, 1f, 0f, 0f, 0f);
                    break;
                case 6:
                    Renderer.renderRectDecoration(lever, x, y, z, -45, 0f, 1f, 0f, 0f);
                    break;
            }
        }
    }
}