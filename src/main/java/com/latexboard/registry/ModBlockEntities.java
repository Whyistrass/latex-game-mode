package com.latexboard.registry;

import com.latexboard.LatexBoardMod;
import com.latexboard.block.LatexBoardBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntities {
    public static final BlockEntityType<LatexBoardBlockEntity> LATEX_BOARD = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(LatexBoardMod.MOD_ID, "latex_board"),
            FabricBlockEntityTypeBuilder.create(LatexBoardBlockEntity::new, ModBlocks.LATEX_BOARD).build()
    );

    private ModBlockEntities() {
    }

    public static void register() {
    }
}
