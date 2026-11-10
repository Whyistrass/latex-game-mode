package com.latexboard.client;

import com.latexboard.block.LatexBoardBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public final class LatexBoardClientHooks {
    private LatexBoardClientHooks() {
    }

    public static void openEditScreen(LatexBoardBlockEntity board) {
        MinecraftClient.getInstance().setScreen(new LatexBoardEditScreen(board));
    }
}
