package dev.darcosse.displayscoreboard.fabric.config.model;

import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardConfig {
    /**
     * List of all active scoreboard objectives.
     * Initialized as ArrayList to support modifications at runtime.
     */
    public List<ScoreboardObjective> objectives = new ArrayList<>();

    public ScoreboardConfig() {
        this.objectives.add(new ScoreboardObjective(
                "deathCount",
                "§c☠ Death Count ☠",
                new BlockPos(0, 0, 0)
        ));
    }
}