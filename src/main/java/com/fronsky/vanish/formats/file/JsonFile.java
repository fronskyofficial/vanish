package com.fronsky.vanish.formats.file;

import com.fronsky.vanish.formats.logging.BasicLogger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.fronsky.vanish.logic.file.IFile;

import java.io.*;
import java.util.Arrays;

public class JsonFile implements IFile<JsonObject> {
    private JsonObject data;
    private final String filePath;
    private final BasicLogger logger = new BasicLogger();

    public JsonFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean load() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(filePath);
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(true);
            this.data = gson.fromJson(jsonReader, JsonObject.class);
            reader.close();
            return true;
        } catch (IOException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    @Override
    public void save() {
        // Not implemented, as this class only reads JSON data from files
    }

    @Override
    public void reload() {
        this.load();
    }

    @Override
    public JsonObject get() {
        return this.data;
    }
}
