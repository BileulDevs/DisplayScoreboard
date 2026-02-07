package dev.darcosse.displayscoreboard.fabric;

import dev.darcosse.displayscoreboard.fabric.registry.ModCommands;
import dev.darcosse.displayscoreboard.fabric.config.ConfigManager;
import dev.darcosse.displayscoreboard.fabric.registry.ModTicks;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DisplayScoreboard implements ModInitializer {
    public static final String MOD_ID = "displayscoreboard";
    public static final Logger LOGGER = LogManager.getLogger("DisplayScoreboard");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing DisplayScoreboard");
        ConfigManager.loadConfig();

        ModCommands.register();
        ModTicks.register();
        LOGGER.info("Initialized DisplayScoreboard");
    }
}