package net.sixik.sdmmarket.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.sixik.sdmmarket.common.network.misc.SendOpenMarketScreenS2C;

import java.util.Collection;

public class MarketCommands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sdmmarket")
                .then(Commands.literal("open_market")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> openMarket(context.getSource(), null))
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(context -> openMarket(context.getSource(), EntityArgument.getPlayers(context, "player")))
                        )
                )
        );
    }


    private static int openMarket(CommandSourceStack source, Collection<ServerPlayer> profiles) {
        if(profiles != null) {
            for (ServerPlayer profile : profiles) {
                new SendOpenMarketScreenS2C().sendTo(profile);
            }
        } else if(source.getPlayer() != null) {
            new SendOpenMarketScreenS2C().sendTo(source.getPlayer());
        }
        return 1;
    }












    public static void registerCommands(CommandDispatcher<CommandSourceStack> commandSourceStackCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        registerCommands(commandSourceStackCommandDispatcher);
    }
}
