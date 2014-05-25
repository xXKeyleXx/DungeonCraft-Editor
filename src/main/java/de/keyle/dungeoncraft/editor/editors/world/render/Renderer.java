/*
 * This file is part of DungeonCraft-Editor
 *
 * Copyright (C) 2011-2014 Keyle
 * DungeonCraft-Editor is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft-Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft-Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.dungeoncraft.editor.editors.world.render;

import de.keyle.dungeoncraft.editor.util.Facing;
import org.lwjgl.opengl.GL11;

import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX16;
import static de.keyle.dungeoncraft.editor.editors.world.render.MinecraftConstants.TEX64;

public class Renderer {
    /**
     * Renders a nonstandard vertical rectangle (nonstandard referring primarily to
     * the texture size (ie: when we're not pulling a single element out of a 16x16
     * grid).  This differs from renderVertical also in that we specify two full
     * (x, y, z) coordinates for the bounds, instead of passing in y and a height.
     *
     * @param dimensions dimensions of the texture
     * @param x1         from x
     * @param y1         from y
     * @param z1         from z
     * @param x2         to x
     * @param y2         to y
     * @param z2         to z
     */
    public static void renderNonstandardVertical(TextureDimensions dimensions, float x1, float y1, float z1, float x2, float y2, float z2) {
        renderNonstandardVertical(dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), dimensions.getTexHeight(), x1, y1, z1, x2, y2, z2);
    }

    /**
     * Renders a nonstandard vertical rectangle (nonstandard referring primarily to
     * the texture size (ie: when we're not pulling a single element out of a 16x16
     * grid).  This differs from renderVertical also in that we specify two full
     * (x, y, z) coordinates for the bounds, instead of passing in y and a height.
     * Texture coordinates are passed in as the usual float from 0 to 1.
     *
     * @param tx  X index within the texture
     * @param ty  Y index within the texture
     * @param tdx Width of texture
     * @param tdy Height of texture
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    public static void renderNonstandardVertical(float tx, float ty, float tdx, float tdy, float x1, float y1, float z1, float x2, float y2, float z2) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        GL11.glTexCoord2f(tx, ty + tdy);
        GL11.glVertex3f(x1, y1, z1);

        GL11.glTexCoord2f(tx + tdx, ty + tdy);
        GL11.glVertex3f(x2, y1, z2);

        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x1, y2, z1);

        GL11.glTexCoord2f(tx + tdx, ty);
        GL11.glVertex3f(x2, y2, z2);

        // unflipped textures
        /*
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x1, y1, z1);

        GL11.glTexCoord2f(tx + tdx, ty);
        GL11.glVertex3f(x2, y1, z2);

        GL11.glTexCoord2f(tx, ty + tdy);
        GL11.glVertex3f(x1, y2, z1);

        GL11.glTexCoord2f(tx + tdx, ty + tdy);
        GL11.glVertex3f(x2, y2, z2);
         */
        GL11.glEnd();
    }

    public static void renderVertical(TextureDimensions dimensions, float x1, float z1, float x2, float z2, float y, float height) {
        renderVertical(dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), dimensions.getTexHeight(), x1, z1, x2, z2, y, height);
    }

    /**
     * Renders a somewhat-arbitrary vertical rectangle.  Pass in (x, z) pairs for the endpoints,
     * and information about the height.  The texture variables given are in terms of 1/16ths of
     * the texture square, which means that for the default Minecraft 16x16 texture, they're in
     * pixels.
     *
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param y      The lower part of the rectangle
     * @param height Height of the rectangle.
     */
    public static void renderVertical(float tx, float ty, float tdx, float tdy, float x1, float z1, float x2, float z2, float y, float height) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x1, y + height, z1);

        GL11.glTexCoord2f(tx + tdx, ty);
        GL11.glVertex3f(x2, y + height, z2);

        GL11.glTexCoord2f(tx, ty + tdy);
        GL11.glVertex3f(x1, y, z1);

        GL11.glTexCoord2f(tx + tdx, ty + tdy);
        GL11.glVertex3f(x2, y, z2);
        GL11.glEnd();
    }

    /**
     * Renders an arbitrary horizontal rectangle (will be orthogonal).  The texture parameters
     * are specified in terms of 1/16ths of the texture (which equates to one pixel, when using
     * the default 16x16 Minecraft texture.
     *
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param y
     */
    public static void renderHorizontal(TextureDimensions dimensions, float x1, float z1, float x2, float z2, float y, boolean flip_tex) {
        renderHorizontal(x1, z1, x2, z2, y, dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), dimensions.getTexHeight(), flip_tex);
    }

    /**
     * Renders an arbitrary horizontal rectangle (will be orthogonal).  The texture parameters
     * are specified in terms of 1/16ths of the texture (which equates to one pixel, when using
     * the default 16x16 Minecraft texture.
     *
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param y
     */
    public static void renderHorizontal(float x1, float z1, float x2, float z2, float y, float tex_start_x, float tex_start_y, float tex_width, float tex_height, boolean flip_tex) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        if (flip_tex) {
            GL11.glTexCoord2f(tex_start_x, tex_start_y);
            GL11.glVertex3f(x1, y, z2);

            GL11.glTexCoord2f(tex_start_x + tex_width, tex_start_y);
            GL11.glVertex3f(x2, y, z2);

            GL11.glTexCoord2f(tex_start_x, tex_start_y + tex_height);
            GL11.glVertex3f(x1, y, z1);

            GL11.glTexCoord2f(tex_start_x + tex_width, tex_start_y + tex_height);
            GL11.glVertex3f(x2, y, z1);
        } else {
            GL11.glTexCoord2f(tex_start_x, tex_start_y);
            GL11.glVertex3f(x1, y, z1);

            GL11.glTexCoord2f(tex_start_x + tex_width, tex_start_y);
            GL11.glVertex3f(x1, y, z2);

            GL11.glTexCoord2f(tex_start_x, tex_start_y + tex_height);
            GL11.glVertex3f(x2, y, z1);

            GL11.glTexCoord2f(tex_start_x + tex_width, tex_start_y + tex_height);
            GL11.glVertex3f(x2, y, z2);
        }
        GL11.glEnd();
    }

    /**
     * Renders a nonstandard horizontal rectangle (nonstandard referring primarily to
     * the texture size (ie: when we're not pulling a single element out of a 16x16
     * grid).
     *
     * @param dimensions dimensions of the texture
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param y
     */
    public static void renderNonstandardHorizontal(TextureDimensions dimensions, float x1, float z1, float x2, float z2, float y) {
        renderNonstandardHorizontal(dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), dimensions.getTexHeight(), x1, z1, x2, z2, y);
    }

    /**
     * Renders a nonstandard horizontal rectangle (nonstandard referring primarily to
     * the texture size (ie: when we're not pulling a single element out of a 16x16
     * grid).
     *
     * @param tx  X index within the texture
     * @param ty  Y index within the texture
     * @param tdx Width of texture
     * @param tdy Height of texture
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param y
     */
    public static void renderNonstandardHorizontal(float tx, float ty, float tdx, float tdy, float x1, float z1, float x2, float z2, float y) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x1, y, z1);

        GL11.glTexCoord2f(tx + tdx, ty);
        GL11.glVertex3f(x1, y, z2);

        GL11.glTexCoord2f(tx, ty + tdy);
        GL11.glVertex3f(x2, y, z1);

        GL11.glTexCoord2f(tx + tdx, ty + tdy);
        GL11.glVertex3f(x2, y, z2);
        GL11.glEnd();
    }

    public static void renderHorizontalAskew(TextureDimensions dimensions, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float y) {
        renderHorizontalAskew(dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), dimensions.getTexHeight(), x1, z1, x2, z2, x3, z3, x4, z4, y);
    }

    /**
     * Render a surface on a horizontal plane; pass in all four verticies.  This can result,
     * obviously, in non-rectangular and non-orthogonal shapes.
     *
     * @param tx
     * @param ty
     * @param tdx
     * @param tdy
     * @param x1
     * @param z1
     * @param x2
     * @param z2
     * @param x3
     * @param z3
     * @param x4
     * @param z4
     * @param y
     */
    public static void renderHorizontalAskew(float tx, float ty, float tdx, float tdy, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float y) {
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3f(x1, y, z1);

        GL11.glTexCoord2f(tx + tdx, ty);
        GL11.glVertex3f(x2, y, z2);

        GL11.glTexCoord2f(tx, ty + tdy);
        GL11.glVertex3f(x3, y, z3);

        GL11.glTexCoord2f(tx + tdx, ty + tdy);
        GL11.glVertex3f(x4, y, z4);
        GL11.glEnd();
    }

    /**
     * Renders a decoration which is supposed to be a "cross" in a single block.
     */
    public static void renderCrossDecoration(TextureDimensions dimensions, int x, int y, int z) {
        float width = dimensions.getTexWidth() / TEX16;
        float width_h = width / 2f;
        float height = dimensions.getTexHeight() / TEX16;

        renderNonstandardVertical(dimensions, x - width_h, y + height, z - width_h, x + width_h, y, z + width_h);
        renderNonstandardVertical(dimensions, x + width_h, y + height, z - width_h, x - width_h, y, z + width_h);
    }

    /**
     * Renders a rectangular decoration.
     *
     * @param dimensions     Dimension of the Texture
     * @param x              Block X
     * @param y              Block Y
     * @param z              Block Z
     * @param rotate_degrees Degrees to rotate, use zero for no rotation
     * @param rotate_x       Use 1.0f to rotate in the X direction (passed to glRotatef)
     * @param rotate_z       Use 1.0f to rotate in the Z direction (passed to glRotatef)
     * @param x_off          X offset, so it's not just in the center
     * @param z_off          Z offset, so it's not just in the center
     */
    public static void renderRectDecoration(TextureDimensions dimensions, float x, float y, float z, int rotate_degrees, float rotate_x, float rotate_z, float x_off, float z_off) {

        boolean do_rotate = false;
        float tx = 0, ty = 0, tz = 0;
        if (rotate_degrees != 0) {
            tx = x;
            ty = y;
            tz = z;
            x = x_off;
            y = 0;
            z = z_off;
            do_rotate = true;
        }

        float width = dimensions.getTexWidth() / TEX16;
        float width_h = width / 2f;
        float height = dimensions.getTexHeight() / TEX16;
        float top_tex_height;
        if (height > width) {
            top_tex_height = dimensions.getTexWidth() / 2f;
        } else {
            top_tex_height = dimensions.getTexHeight();
        }

        // Math is for suckers; let's let the video hardware take care of rotation
        // Relatedly, is this how I should be drawing *everything?*  Draw relative
        // to the origin for the actual verticies, and then translate?
        if (do_rotate) {
            GL11.glPushMatrix();
            GL11.glTranslatef(tx, ty, tz);
            GL11.glRotatef((float) rotate_degrees, rotate_x, 0f, rotate_z);
        }

        // First draw the borders
        renderNonstandardVertical(dimensions, x - width_h, y + height, z - width_h, x + width_h, y, z - width_h);
        renderNonstandardVertical(dimensions, x - width_h, y + height, z + width_h, x + width_h, y, z + width_h);
        renderNonstandardVertical(dimensions, x + width_h, y + height, z - width_h, x + width_h, y, z + width_h);
        renderNonstandardVertical(dimensions, x - width_h, y + height, z + width_h, x - width_h, y, z - width_h);

        // Now the top
        renderNonstandardHorizontal(dimensions.getTexLeft(), dimensions.getTexTop(), dimensions.getTexWidth(), top_tex_height, x - width_h, z - width_h, x + width_h, z + width_h, y + height);

        if (do_rotate) {
            GL11.glPopMatrix();
        }
    }

    /**
     * Renders a "grid" decoration (in the manner of crops and netherwart)
     *
     * @param dimensions Texture dimensions
     * @param x
     * @param y
     * @param z
     */
    public void renderGridDecoration(TextureDimensions dimensions, int x, int y, int z) {
        float width_h = dimensions.getTexWidth() / 16 / 2;
        float side_offset = .25f;
        float bottom = y - .5f;
        float top = bottom + dimensions.getTexHeight() / 16;

        renderNonstandardVertical(dimensions, x - width_h, top, z + side_offset, x + width_h, bottom, z + side_offset);

        renderNonstandardVertical(dimensions, x - width_h, top, z - side_offset, x + width_h, bottom, z - side_offset);

        renderNonstandardVertical(dimensions, x + side_offset, top, z - width_h, x + side_offset, bottom, z + width_h);

        renderNonstandardVertical(dimensions, x - side_offset, top, z - width_h, x - side_offset, bottom, z + width_h);
    }

    /**
     * Renders a block sized face with the given facing.
     *
     * @param dimensions Texture dimensions
     * @param x
     * @param y
     * @param z
     * @param facing     The face that should be drawn
     */
    public static void renderBlockFace(TextureDimensions dimensions, float x, float y, float z, Facing facing) {
        float curFace[][];
        switch (facing) {
            case UP:
                curFace = new float[][]{{-0.5f, +0.5f, +0.5f}, {-0.5f, +0.5f, -0.5f}, {+0.5f, +0.5f, +0.5f}, {+0.5f, +0.5f, -0.5f}};
                break;
            case DOWN:
                curFace = new float[][]{{-0.5f, -0.5f, +0.5f}, {-0.5f, -0.5f, -0.5f}, {+0.5f, -0.5f, +0.5f}, {+0.5f, -0.5f, -0.5f}};
                break;
            case NORTH:
                curFace = new float[][]{{-0.5f, +0.5f, -0.5f}, {+0.5f, +0.5f, -0.5f}, {-0.5f, -0.5f, -0.5f}, {+0.5f, -0.5f, -0.5f}};
                break;
            case SOUTH:
                curFace = new float[][]{{-0.5f, +0.5f, +0.5f}, {+0.5f, +0.5f, +0.5f}, {-0.5f, -0.5f, +0.5f}, {+0.5f, -0.5f, +0.5f}};
                break;
            case WEST:
                curFace = new float[][]{{-0.5f, +0.5f, +0.5f}, {-0.5f, +0.5f, -0.5f}, {-0.5f, -0.5f, +0.5f}, {-0.5f, -0.5f, -0.5f}};
                break;
            case EAST:
                curFace = new float[][]{{+0.5f, +0.5f, +0.5f}, {+0.5f, +0.5f, -0.5f}, {+0.5f, -0.5f, +0.5f}, {+0.5f, -0.5f, -0.5f}};
                break;
            default:
                return;
        }

        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(dimensions.getTexLeft(), dimensions.getTexTop());
        GL11.glVertex3f(x + curFace[0][0], y + curFace[0][1], z + curFace[0][2]);

        GL11.glTexCoord2f(dimensions.getTexLeft() + dimensions.getTexWidth(), dimensions.getTexTop());
        GL11.glVertex3f(x + curFace[1][0], y + curFace[1][1], z + curFace[1][2]);

        GL11.glTexCoord2f(dimensions.getTexLeft(), dimensions.getTexTop() + dimensions.getTexHeight());
        GL11.glVertex3f(x + curFace[2][0], y + curFace[2][1], z + curFace[2][2]);

        GL11.glTexCoord2f(dimensions.getTexLeft() + dimensions.getTexWidth(), dimensions.getTexTop() + dimensions.getTexHeight());
        GL11.glVertex3f(x + curFace[3][0], y + curFace[3][1], z + curFace[3][2]);
        GL11.glEnd();
    }

    /**
     * This is actually used for rendering "decoration" type things which are on
     * the floor (eg: minecart tracks, redstone wires, etc)
     *
     * @param dimensions
     * @param x
     * @param y
     * @param z
     */
    public void renderFloor(TextureDimensions dimensions, int x, int y, int z) {
        renderBlockFace(dimensions, x, y + TEX64, z, Facing.DOWN);
    }
}