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

package de.keyle.dungeoncraft.editor.editors.region;

import javax.swing.*;

public class MinSpinner extends AbstractSpinnerModel {
    int value = 0;
    int minimum;
    int maximum;
    int stepSize;
    MaxSpinner maxSpinner = null;

    public MinSpinner(int value, int minimum, int maximum, int stepSize) {
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.stepSize = stepSize;
    }

    public void setMaxSpinner(MaxSpinner spinner) {
        maxSpinner = spinner;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public int getIntValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            this.value = (Integer) value;
            this.value = Math.max(minimum, this.value);
            this.value = Math.min(maximum, this.value);
            if (maxSpinner != null) {
                if (maxSpinner.getIntValue() < this.value) {
                    maxSpinner.setValue(this.value);
                }
            }
            fireStateChanged();
        }
    }

    @Override
    public Object getNextValue() {
        int newValue = value;
        if (newValue + stepSize <= maximum) {
            newValue += stepSize;
        }
        return newValue;
    }

    @Override
    public Object getPreviousValue() {
        int newValue = value;
        if (newValue - stepSize >= minimum) {
            newValue -= stepSize;
        }
        return newValue;
    }
}