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

package de.keyle.dungeoncraft.editor.util.vector;

import java.awt.*;

public class Region {
    private Vector min;
    private Vector max;
    Color color;

    public Region(Vector min, Vector max) {
        this(min, max, Color.GREEN);
    }

    public Region(Vector min, Vector max, Color color) {
        if (min.getX() > max.getX() || min.getY() > max.getY() || min.getZ() > max.getZ()) {
            throw new IllegalArgumentException("Max vector has to be bigger than the min vector: " + min + " > " + max);
        }
        this.min = min;
        this.max = max;
        this.color = color;
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVectorInside(Vector point) {
        double px = point.getX();
        double py = point.getY();
        double pz = point.getZ();

        return px >= min.getX() && px <= max.getX() && py >= min.getY() && py <= max.getY() && pz >= min.getZ() && pz <= max.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Region region = (Region) o;
        if (!max.equals(region.max)) {
            return false;
        }
        if (!min.equals(region.min)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = min.hashCode();
        result = 31 * result + max.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Region{min=" + min + ", max=" + max + '}';
    }
}