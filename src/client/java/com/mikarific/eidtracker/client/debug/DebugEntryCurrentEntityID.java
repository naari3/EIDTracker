package com.mikarific.eidtracker.client.debug;

import com.mikarific.eidtracker.client.EIDTrackerClient;
import com.mikarific.eidtracker.client.EntityIDSource;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DebugEntryCurrentEntityID implements DebugScreenEntry {
    @Override
    public void display(@NonNull DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
       if (EIDTrackerClient.currentEntityID != null) {
            displayer.addLine("EID" + (EIDTrackerClient.entityIDSource == EntityIDSource.GUESS ? " [est]" : "") + ": " + EIDTrackerClient.currentEntityID + " / " + String.format("%.2f%%", EIDTrackerClient.overflowPercentage));
        }
    }
}
