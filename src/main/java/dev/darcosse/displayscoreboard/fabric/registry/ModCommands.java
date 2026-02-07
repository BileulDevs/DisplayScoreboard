package dev.darcosse.displayscoreboard.fabric.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.darcosse.displayscoreboard.fabric.DisplayScoreboard;
import dev.darcosse.displayscoreboard.fabric.command.DisplayScoreboardCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
        DisplayScoreboard.LOGGER.info("[ModCommands] Registered");
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("displayscoreboard")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("reload").executes(DisplayScoreboardCommands::reloadConfig))
                .then(literal("list").executes(DisplayScoreboardCommands::listScoreboards))
                .then(literal("add")
                        .then(argument("name", StringArgumentType.word())
                                .suggests(DisplayScoreboardCommands.SUGGEST_MINECRAFT_OBJECTIVES)
                                .then(argument("displayName", StringArgumentType.string())
                                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                                .executes(DisplayScoreboardCommands::addScoreboard)))))
                .then(literal("remove")
                        .then(argument("name", StringArgumentType.word())
                                .suggests(DisplayScoreboardCommands.SUGGEST_CONFIG_OBJECTIVES)
                                .executes(DisplayScoreboardCommands::removeScoreboard)))
                .then(literal("modify")
                        .then(argument("name", StringArgumentType.word())
                                .suggests(DisplayScoreboardCommands.SUGGEST_CONFIG_OBJECTIVES)
                                .then(literal("displayName")
                                        .then(argument("value", StringArgumentType.string())
                                                .executes(ctx -> DisplayScoreboardCommands.modifyScoreboard(ctx, "displayName"))))
                                .then(literal("position")
                                        .then(argument("value", BlockPosArgumentType.blockPos())
                                                .executes(ctx -> DisplayScoreboardCommands.modifyScoreboard(ctx, "position"))))))
        );
    }
}