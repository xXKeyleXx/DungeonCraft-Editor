package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class PressurePlate extends Block{
    TextureDimensions PLATE;

    public PressurePlate(short id) {
        super(id);
    }

    @Override
    public void readTextures(JSONObject textures) throws BlockTypeLoadException {
        if (textures.containsKey("TOP")) {
            JSONArray top = (JSONArray) textures.get("TOP");
            PLATE = new TextureDimensions(this.id, Integer.parseInt(top.get(0).toString()), Integer.parseInt(top.get(1).toString()), TEX16, TEX_Y, false);
        } else {
            throw new BlockTypeLoadException(id + " TOP texture.");
        }
    }

    @Override
    public String getBlockType() {
        return "PRESSURE_PLATE";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        float radius = 0.4f;

        // The plate itself
        Renderer.renderHorizontal(PLATE, x + radius + .5f, z + radius+ .5f, x - radius+ .5f, z - radius+ .5f, y + .05f , false);

        // Sides
        Renderer.renderVertical(PLATE, x + radius+ .5f, z + radius+ .5f, x + radius+ .5f, z - radius+ .5f, y, 0.05f);
        Renderer.renderVertical(PLATE, x - radius+ .5f, z + radius+ .5f, x - radius+ .5f, z - radius+ .5f, y, 0.05f);
        Renderer.renderVertical(PLATE, x + radius+ .5f, z + radius+ .5f, x - radius+ .5f, z + radius+ .5f, y, 0.05f);
        Renderer.renderVertical(PLATE, x + radius+ .5f, z - radius+ .5f, x - radius+ .5f, z - radius+ .5f, y, 0.05f);
    }
}