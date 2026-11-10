package com.latexboard.client;

import com.latexboard.network.LatexUpdatePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public final class LatexUpdateSender {
    private LatexUpdateSender() {
    }

    public static void send(BlockPos pos, String latex) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeString(latex);
        ClientPlayNetworking.send(LatexUpdatePacket.ID, buf);
    }
}
