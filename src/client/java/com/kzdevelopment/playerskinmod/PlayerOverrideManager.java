package com.kzdevelopment.playerskinmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlayerOverrideManager {
    private static final Map<UUID, String> nameOverrides = new HashMap<>();
    private static final Map<UUID, String> skinOverrides = new HashMap<>();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("playerskin_overrides.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void setOverride(UUID uuid, String fakeUsername) {
        if (fakeUsername == null || fakeUsername.trim().isEmpty()) {
            removeOverride(uuid);
        } else {
            nameOverrides.put(uuid, fakeUsername);
            skinOverrides.put(uuid, fakeUsername);
            save();
        }
    }

    public static void removeOverride(UUID uuid) {
        nameOverrides.remove(uuid);
        skinOverrides.remove(uuid);
        save();
    }

    public static String getNameOverride(UUID uuid) { return nameOverrides.get(uuid); }
    public static String getSkinOverride(UUID uuid) { return skinOverrides.get(uuid); }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) return;
        try {
            String json = Files.readString(CONFIG_PATH);
            Map<String, String> loaded = GSON.fromJson(json, Map.class);
            if (loaded != null) {
                loaded.forEach((uuidStr, fakeName) -> {
                    UUID uuid = UUID.fromString(uuidStr);
                    nameOverrides.put(uuid, fakeName);
                    skinOverrides.put(uuid, fakeName);
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void save() {
        try {
            Map<String, String> toSave = new HashMap<>();
            nameOverrides.forEach((uuid, name) -> toSave.put(uuid.toString(), name));
            Files.writeString(CONFIG_PATH, GSON.toJson(toSave));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
