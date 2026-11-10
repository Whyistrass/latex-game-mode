package com.latexboard.network;

import com.latexboard.LatexBoardMod;
import com.latexboard.block.LatexBoardBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class LatexUpdatePacket {
    public static final Identifier ID = new Identifier(LatexBoardMod.MOD_ID, "update");

    private LatexUpdatePacket() {
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            String latex = buf.readString();
            server.execute(() -> applyUpdate(player, pos, latex));
        });
    }

    private static void applyUpdate(ServerPlayerEntity player, BlockPos pos, String latex) {
        if (latex.length() > LatexBoardBlockEntity.MAX_LENGTH) {
            return;
        }
        ServerWorld world = player.getServerWorld();
        if (!world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4)) {
            return;
        }
        if (player.squaredDistanceTo(Vec3d.ofCenter(pos)) > 64.0) {
            return;
        }
        if (!world.canPlayerModifyAt(player, pos)) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LatexBoardBlockEntity board) {
            board.setLatex(latex);
        }
    }
}
