package com.mikarific.eidtracker.tracker;

import com.mikarific.eidtracker.mixins.EntityAccessor;
import com.mikarific.eidtracker.networking.NetworkingHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class TrackerManager {
    private static final Map<ServerPlayer, Integer> trackerIds = new HashMap<>();
    private static final Map<ServerPlayer, Integer> trackerTicks = new HashMap<>();
    private static final Map<ServerPlayer, Double> trackerRates = new HashMap<>();

    public static void addTracker(ServerPlayer player) {
        trackerIds.put(player, null);
        trackerTicks.put(player, null);
        trackerRates.put(player, 0.0);
    }

    public static void removeTracker(ServerPlayer player, int tickCount) {
        sendRate(player, tickCount);
        trackerIds.remove(player);
        trackerTicks.remove(player);
        trackerRates.remove(player);
    }

    public static boolean isTracking(ServerPlayer player) {
        return trackerIds.containsKey(player);
    }

    public static void updateTracker(int tickCount) {
        int currentEntityId = EntityAccessor.getCurrentId().get();
        for (ServerPlayer player : trackerIds.keySet()) {
            Integer trackerId = trackerIds.get(player);
            Integer trackerTick = trackerTicks.get(player);

            if (trackerId == null) {
                trackerIds.replace(player, currentEntityId);
                trackerId = currentEntityId;
            }
            if (trackerTick == null) {
                trackerTicks.replace(player, tickCount);
                trackerTick = tickCount;
            }

            int deltaId = currentEntityId - trackerId;
            int deltaTicks = tickCount - trackerTick;
            double rate = deltaTicks > 0 ? deltaId / (double) deltaTicks : 0.0;
            trackerRates.replace(player, rate);

            if (currentEntityId >= trackerId + 4194304) {
                MutableComponent translatable = Component.translatable("commands.eid.track.finished").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                MutableComponent fallback = Component.literal("Finished tracking the Current Entity ID.").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                player.sendSystemMessage(NetworkingHandler.hasClientMod(player.getUUID()) ? translatable : fallback);
                removeTracker(player, tickCount);
            }
        }
    }

    public static void sendRate(ServerPlayer player, int tickCount) {
        int currentEntityId = EntityAccessor.getCurrentId().get();
        int trackerId = trackerIds.get(player) != null ? trackerIds.get(player) : currentEntityId;
        int trackerTick = trackerTicks.get(player) != null ? trackerTicks.get(player) : tickCount;

        double time = (tickCount - trackerTick) / 1200.0;
        int total = currentEntityId - trackerId;
        double rate = trackerRates.get(player);

        long ticks = rate > 0 ? Math.round((long) (4294967296L / rate)) : Long.MAX_VALUE;

        int hours = (int) Math.floor(ticks / 72000.0);
        int minutes = (int) Math.floor((ticks / 1200.0) % 60);
        double seconds = (ticks / 20.0) % 20;

        MutableComponent displayTime = Component.literal(String.format("%.2f", time)).withStyle(ChatFormatting.BOLD);
        MutableComponent displayTotal = Component.literal(String.valueOf(total)).withStyle(ChatFormatting.BOLD);
        MutableComponent displayRate = Component.literal(String.format("%.1f", rate)).withStyle(ChatFormatting.BOLD);
        MutableComponent displayTicks = ticks != Long.MAX_VALUE ? Component.literal(String.valueOf(ticks)).withStyle(ChatFormatting.BOLD) : Component.literal("∞").withStyle(ChatFormatting.BOLD);
        MutableComponent displayHours = ticks != Long.MAX_VALUE ? Component.literal(String.valueOf(hours)).withStyle(ChatFormatting.BOLD) : Component.literal("∞").withStyle(ChatFormatting.BOLD);
        MutableComponent displayMinutes = ticks != Long.MAX_VALUE ? Component.literal(String.valueOf(minutes)).withStyle(ChatFormatting.BOLD) : Component.literal("∞").withStyle(ChatFormatting.BOLD);
        MutableComponent displaySeconds = ticks != Long.MAX_VALUE ? Component.literal(String.format("%.2f", seconds)).withStyle(ChatFormatting.BOLD) : Component.literal("∞").withStyle(ChatFormatting.BOLD);

        MutableComponent translatable = Component.translatable("commands.eid.track", displayTime, displayTotal, displayRate, displayTicks, displayHours, displayMinutes, displaySeconds);
        MutableComponent fallback = Component.literal(String.format("Current Entity ID (§l%s§r min), total: §l%s§r, (§l%s§r/tick):\n - Overflow (start to finish) in §l%s§r ticks (§l%s§rh§l%s§rm§l%s§rs)", displayTime.getString(), displayTotal.getString(), displayRate.getString(), displayTicks.getString(), displayHours.getString(), displayMinutes.getString(), displaySeconds.getString()));
        player.sendSystemMessage(NetworkingHandler.hasClientMod(player.getUUID()) ? translatable : fallback);
    }
}
