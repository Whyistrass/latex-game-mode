package com.latexboard;

import com.latexboard.network.LatexUpdatePacket;
import com.latexboard.registry.ModBlockEntities;
import com.latexboard.registry.ModBlocks;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatexBoardMod implements ModInitializer {
    public static final String MOD_ID = "latexboard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntities.register();
        LatexUpdatePacket.registerServer();
    }
}
