package com.latexboard.registry;

import com.latexboard.LatexBoardMod;
import com.latexboard.block.LatexBoardBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class ModBlocks {
    public static final Block LATEX_BOARD = Registry.register(
            Registries.BLOCK,
            new Identifier(LatexBoardMod.MOD_ID, "latex_board"),
            new LatexBoardBlock(AbstractBlock.Settings.create()
                    .strength(1.0F)
                    .sounds(BlockSoundGroup.WOOD))
    );

    public static final Item LATEX_BOARD_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier(LatexBoardMod.MOD_ID, "latex_board"),
            new BlockItem(LATEX_BOARD, new Item.Settings())
    );

    private static final RegistryKey<ItemGroup> FUNCTIONAL_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("minecraft", "functional_blocks"));

    private ModBlocks() {
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(FUNCTIONAL_GROUP).register(entries -> entries.add(LATEX_BOARD_ITEM));
    }
}
