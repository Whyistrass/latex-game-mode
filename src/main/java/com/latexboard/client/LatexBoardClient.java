package com.latexboard.client;

import com.latexboard.client.render.LatexBoardRenderer;
import com.latexboard.registry.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class LatexBoardClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.LATEX_BOARD, LatexBoardRenderer::new);
    }
}
