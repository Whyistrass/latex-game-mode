package com.latexboard.client.render;

import com.latexboard.LatexBoardMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class LatexTextureCache {
    private static final int MAX_ENTRIES = 64;
    private static final int MAX_TEXTURE_SIZE = 2048;
    private static final float FONT_SIZE = 48.0F;

    private static final Map<String, CachedTexture> CACHE = new LinkedHashMap<>(32, 0.75F, true);

    private static int nextId = 0;

    private LatexTextureCache() {
    }

    public record CachedTexture(Identifier id, int width, int height) {
    }

    @Nullable
    public static CachedTexture get(String latex) {
        if (latex == null || latex.isBlank()) {
            return null;
        }
        CachedTexture cached = CACHE.get(latex);
        if (cached != null) {
            return cached;
        }
        try {
            CachedTexture created = render(latex);
            if (created == null) {
                return null;
            }
            CachedTexture evicted = null;
            if (CACHE.size() >= MAX_ENTRIES) {
                String eldestKey = CACHE.keySet().iterator().next();
                evicted = CACHE.remove(eldestKey);
            }
            CACHE.put(latex, created);
            if (evicted != null) {
                MinecraftClient.getInstance().getTextureManager().destroyTexture(evicted.id());
            }
            return created;
        } catch (Exception e) {
            LatexBoardMod.LOGGER.debug("Failed to render LaTeX: {}", latex, e);
            return null;
        }
    }

    @Nullable
    private static CachedTexture render(String latex) {
        TeXFormula formula = new TeXFormula(latex);
        float fontSize = FONT_SIZE;
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        if (width > MAX_TEXTURE_SIZE || height > MAX_TEXTURE_SIZE) {
            float scale = Math.min(MAX_TEXTURE_SIZE / (float) width, MAX_TEXTURE_SIZE / (float) height);
            fontSize *= scale;
            icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
            width = icon.getIconWidth();
            height = icon.getIconHeight();
        }

        width = Math.max(1, width);
        height = Math.max(1, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, width, height);
        graphics.setComposite(AlphaComposite.SrcOver);
        icon.setForeground(Color.BLACK);
        icon.paintIcon(null, graphics, 0, 0);
        graphics.dispose();

        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);
                int a = (argb >>> 24) & 0xFF;
                int r = (argb >>> 16) & 0xFF;
                int g = (argb >>> 8) & 0xFF;
                int b = argb & 0xFF;
                nativeImage.setColor(x, y, (a << 24) | (b << 16) | (g << 8) | r);
            }
        }

        NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
        texture.setFilter(true, false);
        Identifier id = new Identifier(LatexBoardMod.MOD_ID, "formula_" + nextId++);
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
        return new CachedTexture(id, width, height);
    }
}
