package com.latexboard.block;

import com.latexboard.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LatexBoardBlockEntity extends BlockEntity {
    public static final int MAX_LENGTH = 512;

    private String latex = "";

    public LatexBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LATEX_BOARD, pos, state);
    }

    public String getLatex() {
        return latex;
    }

    public void setLatex(String latex) {
        String trimmed = latex.length() > MAX_LENGTH ? latex.substring(0, MAX_LENGTH) : latex;
        if (trimmed.equals(this.latex)) {
            return;
        }
        this.latex = trimmed;
        markDirty();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("latex", latex);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        latex = nbt.getString("latex");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("latex", latex);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
