package de.keyle.dungeoncraft.editor.editors.world.render.block;

import de.keyle.dungeoncraft.editor.editors.world.render.BlockTypeLoadException;
import de.keyle.dungeoncraft.editor.editors.world.render.ChunkSchematic;
import de.keyle.dungeoncraft.editor.editors.world.render.Renderer;
import de.keyle.dungeoncraft.editor.editors.world.render.TextureDimensions;
import de.keyle.dungeoncraft.editor.util.Facing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX_Y;

public class Portal extends Block{
    TextureDimensions SIDE;

    public Portal(short id) {
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
        return "PORTAL";
    }

    @Override
    public void render(byte blockData, int x, int y, int z, ChunkSchematic chunk) {
        // Check to see where adjoining Portal spaces are, so we know which
        // faces to draw
        boolean drawWestEast = false;
        if (chunk.getAdjBlockId(x, y, z, Facing.WEST) == id ||
                chunk.getAdjBlockId(x, y, z, Facing.EAST) == id) {
            drawWestEast = true;
        }

        if (drawWestEast) {
            Renderer.renderVertical(SIDE, x, z + .2f, x + 1, z + .2f, y, 1.0f);
            Renderer.renderVertical(SIDE, x, z + .8f, x + 1, z + .8f, y, 1.0f);
        } else {
            Renderer.renderVertical(SIDE, x + .2f, z, x + .2f, z + 1, y, 1.0f);
            Renderer.renderVertical(SIDE, x + .8f, z, x + .8f, z + 1, y, 1.0f);
        }
    }
}