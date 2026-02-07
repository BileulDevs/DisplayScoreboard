package dev.darcosse.displayscoreboard.fabric.display;

public class ScoreboardUtils {

    /**
     * Returns the formatted rank prefix with appropriate colors.
     */
    public static String getPodiumColor(int rank) {
        return switch (rank) {
            case 1 -> "§e#" + rank; // Gold
            case 2 -> "§7#" + rank; // Silver
            case 3 -> "§c#" + rank; // Bronze (Dark Red)
            default -> "§f#" + rank; // White
        };
    }
}