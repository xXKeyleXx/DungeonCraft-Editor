/*
 * This file is part of DungeonCraft
 *
 * Copyright (C) 2013-2014 Keyle & xXLupoXx
 * DungeonCraft is licensed under the GNU Lesser General Public License.
 *
 * DungeonCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DungeonCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.keyle.dungeoncraft.editor.util.config;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class ConfigurationJson {
    public File jsonFile;
    private Object config;

    public ConfigurationJson(String path) {
        this(new File(path));
    }

    public ConfigurationJson(File file) {
        jsonFile = file;
    }

    public Object getObject() {
        if (config == null) {
            config = new JSONObject();
        }
        return config;
    }

    public boolean load() {
        config = new JSONObject();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(jsonFile));
            JSONParser parser = new JSONParser();
            config = parser.parse(reader);
        } catch (ParseException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return true;
    }

    public boolean save() {
        BufferedWriter writer = null;
        try {
            // http://jsonformatter.curiousconcept.com/
            // http://jsoneditoronline.org/
            writer = new BufferedWriter(new FileWriter(jsonFile));
            if(config instanceof JSONObject) {
                writer.write(((JSONObject)config).toJSONString());
            } else if(config instanceof JSONArray) {
                writer.write(((JSONArray)config).toJSONString());
            } else {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            //DebugLogger.printThrowable(e);
            return false;
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearConfig() {
        config = new JSONObject();
    }

    public static Object load(InputStream is) {
        Object config = new JSONObject();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            JSONParser parser = new JSONParser();
            config = parser.parse(reader);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return config;
    }
}