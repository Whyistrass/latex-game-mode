package com.latexboard.client;

import com.latexboard.block.LatexBoardBlockEntity;
import com.latexboard.client.render.LatexTextureCache;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class LatexBoardEditScreen extends Screen {
    private final LatexBoardBlockEntity board;
    private TextFieldWidget input;
    private String previewSource = "";
    private LatexTextureCache.CachedTexture preview;
    private long lastEditTime;

    public LatexBoardEditScreen(LatexBoardBlockEntity board) {
        super(Text.translatable("gui.latexboard.title"));
        this.board = board;
    }

    @Override
    protected void init() {
        int fieldWidth = Math.min(320, this.width - 40);
        input = new TextFieldWidget(this.textRenderer, this.width / 2 - fieldWidth / 2, 40, fieldWidth, 20,
                Text.translatable("gui.latexboard.hint"));
        input.setMaxLength(LatexBoardBlockEntity.MAX_LENGTH);
        input.setText(board.getLatex());
        input.setPlaceholder(Text.translatable("gui.latexboard.hint"));
        input.setChangedListener(text -> lastEditTime = System.currentTimeMillis());
        addSelectableChild(input);
        setInitialFocus(input);

        int buttonY = this.height - 40;
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.latexboard.confirm"), button -> {
            LatexUpdateSender.send(board.getPos(), input.getText());
            close();
        }).dimensions(this.width / 2 - 105, buttonY, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.latexboard.cancel"), button -> close())
                .dimensions(this.width / 2 + 5, buttonY, 100, 20).build());

        previewSource = input.getText();
        preview = LatexTextureCache.get(previewSource);
    }

    @Override
    public void tick() {
        input.tick();
        if (!input.getText().equals(previewSource) && System.currentTimeMillis() - lastEditTime > 250) {
            previewSource = input.getText();
            preview = LatexTextureCache.get(previewSource);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        input.render(context, mouseX, mouseY, delta);

        if (!previewSource.equals(input.getText())) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui.latexboard.rendering"),
                    this.width / 2, 110, 0xAAAAAA);
        } else if (previewSource.isBlank()) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui.latexboard.empty"),
                    this.width / 2, 110, 0x888888);
        } else if (preview == null) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("gui.latexboard.error"),
                    this.width / 2, 110, 0xFF5555);
        } else {
            int maxWidth = Math.min(320, this.width - 40);
            int maxHeight = Math.min(160, this.height - 160);
            float scale = Math.min(maxWidth / (float) preview.width(), maxHeight / (float) preview.height());
            int drawWidth = Math.max(1, (int) (preview.width() * scale));
            int drawHeight = Math.max(1, (int) (preview.height() * scale));
            context.drawTexture(preview.id(), this.width / 2 - drawWidth / 2,
                    80 + (maxHeight - drawHeight) / 2, 0, 0, drawWidth, drawHeight,
                    preview.width(), preview.height());
        }

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
