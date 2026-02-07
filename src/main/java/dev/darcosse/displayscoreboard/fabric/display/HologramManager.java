package dev.darcosse.displayscoreboard.fabric.display;

import dev.darcosse.displayscoreboard.fabric.config.model.ScoreboardObjective;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;

public class HologramManager {
    public static final String HOLOGRAM_TAG = "ds_hologram";
    private static final double LINE_SPACING = 0.3;

    public static void setScoreboard(MinecraftServer server, ScoreboardObjective objectiveConfig) {
        Scoreboard scoreboard = server.getScoreboard();
        var objective = scoreboard.getNullableObjective(objectiveConfig.getName());
        if (objective == null) return;

        List<ScoreboardEntry> topEntries = scoreboard.getScoreboardEntries(objective).stream()
                .sorted((a, b) -> Integer.compare(b.value(), a.value()))
                .limit(5)
                .toList();

        ServerWorld world = server.getOverworld();
        BlockPos basePos = objectiveConfig.getPosition();

        clearOldHolograms(world, basePos, 5);

        spawnHologramLine(world, basePos.toCenterPos().add(0, 0.3, 0), "§6" + objectiveConfig.getDisplayName());

        for (int i = 0; i < topEntries.size(); i++) {
            ScoreboardEntry score = topEntries.get(i);
            int rank = i + 1;

            String rankPrefix = ScoreboardUtils.getPodiumColor(rank);
            String text = rankPrefix + " §b" + score.owner() + " §7- §c" + score.value();

            spawnHologramLine(world, basePos.toCenterPos().add(0, -(i * LINE_SPACING), 0), text);
        }
    }

    public static void clearOldHolograms(ServerWorld world, BlockPos pos, int limit) {
        Box box = new Box(pos).expand(1.0, 2.0, 1.0);
        var entities = world.getEntitiesByClass(ArmorStandEntity.class, box,
                e -> e.getCommandTags().contains(HOLOGRAM_TAG));
        for (ArmorStandEntity entity : entities) {
            entity.discard();
        }
    }

    private static void spawnHologramLine(ServerWorld world, Vec3d pos, String text) {
        ArmorStandEntity line = new ArmorStandEntity(world, pos.x, pos.y + 1.97, pos.z);

        line.getDataTracker().set(ArmorStandEntity.ARMOR_STAND_FLAGS, (byte)((Byte)line.getDataTracker().get(ArmorStandEntity.ARMOR_STAND_FLAGS) | 0x10));

        line.addCommandTag(HOLOGRAM_TAG);
        line.setInvisible(true);
        line.setNoGravity(true);
        line.setInvulnerable(true);
        line.setCustomName(Text.literal(text));
        line.setCustomNameVisible(true);
        line.setShowArms(false);
        line.setSilent(true);
        line.noClip = true;

        world.spawnEntity(line);
    }
}