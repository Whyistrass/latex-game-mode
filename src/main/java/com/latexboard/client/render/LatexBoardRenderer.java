package com.latexboard.client.render;

import com.latexboard.block.LatexBoardBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class LatexBoardRenderer implements BlockEntityRenderer<LatexBoardBlockEntity> {
    private static final float MARGIN = 0.0625F;
    private static final float OFFSET = 0.001F;

    public LatexBoardRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(LatexBoardBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        LatexTextureCache.CachedTexture texture = LatexTextureCache.get(entity.getLatex());
        if (texture == null) {
            return;
        }

        float maxSize = 1.0F - MARGIN * 2.0F;
        float width = maxSize;
        float height = maxSize;
        float aspect = (float) texture.width() / (float) texture.height();
        if (aspect > 1.0F) {
            height = maxSize / aspect;
        } else {
            width = maxSize * aspect;
        }

        float x0 = 0.5F - width / 2.0F;
        float x1 = 0.5F + width / 2.0F;
        float y0 = 0.5F - height / 2.0F;
        float y1 = 0.5F + height / 2.0F;

        // 左右面水平方向沿 z 轴，水平尺寸用 width
        float z0 = 0.5F - width / 2.0F;
        float z1 = 0.5F + width / 2.0F;
        // 顶/底面另一个水平方向沿 z 轴，尺寸用 height
        float zt0 = 0.5F - height / 2.0F;
        float zt1 = 0.5F + height / 2.0F;

        float xMin = -OFFSET;
        float xMax = 1.0F + OFFSET;
        float yMin = -OFFSET;
        float yMax = 1.0F + OFFSET;
        float zMin = -OFFSET;
        float zMax = 1.0F + OFFSET;

        MatrixStack.Entry entry = matrices.peek();
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture.id()));

        // +Z
        quad(consumer, entry, light,
                x0, y0, zMax, x1, y0, zMax, x1, y1, zMax, x0, y1, zMax,
                0.0F, 0.0F, 1.0F);
        // -Z
        quad(consumer, entry, light,
                x1, y0, zMin, x0, y0, zMin, x0, y1, zMin, x1, y1, zMin,
                0.0F, 0.0F, -1.0F);
        // +X
        quad(consumer, entry, light,
                xMax, y0, z1, xMax, y0, z0, xMax, y1, z0, xMax, y1, z1,
                1.0F, 0.0F, 0.0F);
        // -X
        quad(consumer, entry, light,
                xMin, y0, z0, xMin, y0, z1, xMin, y1, z1, xMin, y1, z0,
                -1.0F, 0.0F, 0.0F);
        // +Y
        quad(consumer, entry, light,
                x1, yMax, zt1, x0, yMax, zt1, x0, yMax, zt0, x1, yMax, zt0,
                0.0F, 1.0F, 0.0F);
        // -Y
        quad(consumer, entry, light,
                x0, yMin, zt1, x1, yMin, zt1, x1, yMin, zt0, x0, yMin, zt0,
                0.0F, -1.0F, 0.0F);
    }

    private static void quad(VertexConsumer consumer, MatrixStack.Entry entry, int light,
                             float x0, float y0, float z0,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float x3, float y3, float z3,
                             float nx, float ny, float nz) {
        vertex(consumer, entry, light, x0, y0, z0, 0.0F, 1.0F, nx, ny, nz);
        vertex(consumer, entry, light, x1, y1, z1, 1.0F, 1.0F, nx, ny, nz);
        vertex(consumer, entry, light, x2, y2, z2, 1.0F, 0.0F, nx, ny, nz);
        vertex(consumer, entry, light, x3, y3, z3, 0.0F, 0.0F, nx, ny, nz);
    }

    private static void vertex(VertexConsumer consumer, MatrixStack.Entry entry, int light,
                               float x, float y, float z, float u, float v,
                               float nx, float ny, float nz) {
        consumer.vertex(entry.getPositionMatrix(), x, y, z)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(entry.getNormalMatrix(), nx, ny, nz)
                .next();
    }
}
