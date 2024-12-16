package com.williamchand.authenticationmc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AuthStorage {
    private static final Path FILE_PATH = Path.of("auth.json");
    private static final Gson GSON = new Gson();

    public static void save(Map<String, String> data) {
        try (Writer writer = Files.newBufferedWriter(FILE_PATH)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> load() {
        if (!Files.exists(FILE_PATH)) {
            return new HashMap<>();
        }
        try (Reader reader = Files.newBufferedReader(FILE_PATH)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            return GSON.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
