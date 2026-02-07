package dev.darcosse.displayscoreboard.fabric.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.darcosse.displayscoreboard.fabric.config.ConfigManager;
import dev.darcosse.displayscoreboard.fabric.config.model.ScoreboardObjective;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DisplayScoreboardCommands {

    public static final SuggestionProvider<ServerCommandSource> SUGGEST_MINECRAFT_OBJECTIVES = (context, builder) -> {
        var scoreboard = context.getSource().getServer().getScoreboard();
        return CommandSource.suggestMatching(scoreboard.getObjectiveNames(), builder);
    };

    public static final SuggestionProvider<ServerCommandSource> SUGGEST_CONFIG_OBJECTIVES = (context, builder) ->
            CommandSource.suggestMatching(ConfigManager.getObjectives().stream().map(ScoreboardObjective::getName), builder);

    public static int reloadConfig(CommandContext<ServerCommandSource> context) {
        try {
            ConfigManager.reloadConfig();
            context.getSource().sendFeedback(() -> Text.translatable("command.displayscoreboard.reload.success"), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.displayscoreboard.reload.error", e.getMessage()));
            return 0;
        }
    }

    public static int addScoreboard(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String displayName = StringArgumentType.getString(context, "displayName");
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos").down();

        ScoreboardObjective newObj = new ScoreboardObjective(name, displayName, pos);

        if (ConfigManager.addObjective(newObj)) {
            context.getSource().sendFeedback(() -> Text.translatable("command.displayscoreboard.add.success", name), true);
            return 1;
        } else {
            context.getSource().sendError(Text.translatable("command.displayscoreboard.add.exists", name));
            return 0;
        }
    }

    public static int removeScoreboard(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        if (ConfigManager.removeObjective(name, context.getSource().getServer())) {
            context.getSource().sendFeedback(() -> Text.translatable("command.displayscoreboard.remove.success", name), true);
            return 1;
        } else {
            context.getSource().sendError(Text.translatable("command.displayscoreboard.remove.not_found", name));
            return 0;
        }
    }

    public static int modifyScoreboard(CommandContext<ServerCommandSource> context, String property) {
        String name = StringArgumentType.getString(context, "name");
        Object value;

        if (property.equals("position")) {
            value = BlockPosArgumentType.getBlockPos(context, "value").down();
        } else {
            value = StringArgumentType.getString(context, "value");
        }

        if (ConfigManager.modifyObjective(name, property, value)) {
            context.getSource().sendFeedback(() -> Text.translatable("command.displayscoreboard.modify.success", property, name), true);
            return 1;
        } else {
            context.getSource().sendError(Text.translatable("command.displayscoreboard.remove.not_found", name));
            return 0;
        }
    }

    public static int listScoreboards(CommandContext<ServerCommandSource> context) {
        var objectives = ConfigManager.getObjectives();
        if (objectives.isEmpty()) {
            context.getSource().sendFeedback(() -> Text.literal("§cAucun hologramme configuré."), false);
            return 1;
        }
        context.getSource().sendFeedback(() -> Text.literal("§6--- Hologrammes Actifs ---"), false);
        for (ScoreboardObjective obj : objectives) {
            context.getSource().sendFeedback(() -> Text.literal("§e" + obj.getName() + " §7(§f" + obj.getDisplayName() + "§7)"), false);
        }
        return 1;
    }
}