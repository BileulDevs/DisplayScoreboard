package dev.darcosse.displayscoreboard.fabric.config.model;

import net.minecraft.util.math.BlockPos;

public class ScoreboardObjective {
    public String name;
    public String displayName;
    public BlockPos position;
    public int limit = 5;

    public ScoreboardObjective() {}

    public ScoreboardObjective(String name, String displayName, BlockPos position) {
        this.name = name;
        this.displayName = displayName;
        this.position = position;
    }

    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public BlockPos getPosition() { return position; }
    public int getLimit() { return limit; }
}