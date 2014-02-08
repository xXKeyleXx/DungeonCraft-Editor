package de.keyle.dungeoncraft.editor.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util {
    public static boolean isDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isFloat(String number) {
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isByte(String number) {
        try {
            Byte.parseByte(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static String readFile(File file) {
        try {
            return Files.toString(file, Charsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(File file, String content) {
        try {
            Files.write(content.getBytes(Charsets.UTF_8), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
