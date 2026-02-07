package dev.darcosse.displayscoreboard.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.darcosse.displayscoreboard.fabric.DisplayScoreboard;
import dev.darcosse.displayscoreboard.fabric.config.model.DisplayScoreboardConfig;
import dev.darcosse.displayscoreboard.fabric.config.model.ScoreboardConfig;
import dev.darcosse.displayscoreboard.fabric.config.model.ScoreboardObjective;
import dev.darcosse.displayscoreboard.fabric.display.HologramManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final String CONFIG_FILE = "displayscoreboard.json";
    private static DisplayScoreboardConfig config;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Loads the configuration from the JSON file or creates a default one.
     */
    public static void loadConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE);

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, DisplayScoreboardConfig.class);

                // Ensure internal structures are not null and mutable
                if (config.scoreboardConfig == null) {
                    config.scoreboardConfig = new ScoreboardConfig();
                }
                if (config.scoreboardConfig.objectives == null) {
                    config.scoreboardConfig.objectives = new ArrayList<>();
                } else {
                    config.scoreboardConfig.objectives = new ArrayList<>(config.scoreboardConfig.objectives);
                }

            } catch (IOException e) {
                DisplayScoreboard.LOGGER.error("Failed to load config, creating default: {}", e.getMessage());
                config = new DisplayScoreboardConfig();
                saveConfig();
            }
        } else {
            config = new DisplayScoreboardConfig();
            saveConfig();
        }

        DisplayScoreboard.LOGGER.info("[ConfigManager] Successfully loaded config with {} scoreboard(s)",
                getObjectives().size());
    }

    /**
     * Saves the current memory configuration to the physical JSON file.
     */
    public static void saveConfig() {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE);
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
            DisplayScoreboard.LOGGER.info("[ConfigManager] Config saved successfully");
        } catch (IOException e) {
            DisplayScoreboard.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    /**
     * Resets and reloads the configuration.
     */
    public static void reloadConfig() {
        config = null;
        loadConfig();
    }

    /**
     * Adds a new objective if the name (Primary Key) is unique.
     */
    public static boolean addObjective(ScoreboardObjective newObj) {
        if (config == null) loadConfig();

        boolean exists = config.scoreboardConfig.objectives.stream()
                .anyMatch(obj -> obj.getName().equalsIgnoreCase(newObj.getName()));

        if (exists) return false;

        config.scoreboardConfig.objectives.add(newObj);
        saveConfig();
        return true;
    }

    /**
     * Removes an objective based on its unique name.
     */
    public static boolean removeObjective(String name, MinecraftServer server) {
        if (config == null) loadConfig();

        var toRemove = config.scoreboardConfig.objectives.stream()
                .filter(obj -> obj.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (toRemove != null) {
            dev.darcosse.displayscoreboard.fabric.display.HologramManager.clearOldHolograms(
                    server.getOverworld(), toRemove.getPosition(), 5
            );
            config.scoreboardConfig.objectives.remove(toRemove);
            saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Modifies a specific property of an existing objective.
     */
    public static boolean modifyObjective(String name, String property, Object value) {
        if (config == null) loadConfig();
        for (ScoreboardObjective obj : config.scoreboardConfig.objectives) {
            if (obj.getName().equalsIgnoreCase(name)) {
                switch (property.toLowerCase()) {
                    case "displayname" -> obj.displayName = (String) value;
                    case "position" -> obj.position = (BlockPos) value;
                    case "limit" -> obj.limit = (int) value;
                }
                saveConfig();
                return true;
            }
        }
        return false;
    }

    public static List<ScoreboardObjective> getObjectives() {
        if (config == null) loadConfig();
        return config.scoreboardConfig.objectives;
    }

    public static int getScoreboardRefreshInterval() {
        if (config == null) loadConfig();
        return config.scoreboardRefreshInterval;
    }

    public static boolean isScoreboardHologramsEnabled() {
        if (config == null) loadConfig();
        return config.enableScoreboardHolograms;
    }

    public static DisplayScoreboardConfig getConfig() {
        if (config == null) loadConfig();
        return config;
    }
}