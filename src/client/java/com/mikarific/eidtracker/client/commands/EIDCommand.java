package com.mikarific.eidtracker.client.commands;

import com.mikarific.eidtracker.client.masa.MasaHelper;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class EIDCommand {
    public static boolean singleplayerEntityIdFix = false;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
        if (environment.includeIntegrated && !MasaHelper.getTweak("TWEAK_SINGLEPLAYER_ENTITY_ID_FIX")) {
            dispatcher.register(Commands.literal("eid")
                    .then(Commands.literal("fix")
                            .executes(context -> fix(context.getSource(), !singleplayerEntityIdFix))
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                    .executes(context -> fix(context.getSource(), BoolArgumentType.getBool(context, "value")))
                            )
                    )
            );
        }
    }

    private static int fix(CommandSourceStack source, boolean value) {
        singleplayerEntityIdFix = value;
        source.sendSuccess(() -> Component.translatable("commands.eid.fix" + (value ? ".true" : ".false")), true);
        return Command.SINGLE_SUCCESS;
    }
}
