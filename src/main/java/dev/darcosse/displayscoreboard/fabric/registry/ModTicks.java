package dev.darcosse.displayscoreboard.fabric.registry;

import dev.darcosse.displayscoreboard.fabric.DisplayScoreboard;
import dev.darcosse.displayscoreboard.fabric.config.ConfigManager;
import dev.darcosse.displayscoreboard.fabric.display.HologramManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModTicks {
    private static int tickCounter = 0;

    /**
     * Registers the server-side tick event to update holograms.
     */
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;

            int interval = ConfigManager.getScoreboardRefreshInterval();

            if (tickCounter % interval != 0) {
                return;
            }

            if (ConfigManager.isScoreboardHologramsEnabled()) {
                ConfigManager.getObjectives().forEach(obj -> {
                    HologramManager.setScoreboard(server, obj);
                });
            }
        });

        DisplayScoreboard.LOGGER.info("[ModTicks] Successfully registered Tick Events");
    }
}