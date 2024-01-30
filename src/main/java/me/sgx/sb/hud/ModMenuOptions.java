package me.sgx.sb.hud;

import me.sgx.sb.Client;
import me.sgx.sb.hud.options.OptionBuilder;
import me.sgx.sb.util.Maths;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModMenuOptions extends GameOptionsScreen {
    private OptionListWidget list;

    public ModMenuOptions(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("sb.options"));
    }

    @Override
    protected void init() {
        list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        list.addSingleOptionEntry(OptionBuilder.buildBooleanButton("sb.options.marioMode", Text.translatable("sb.options.marioMode.tooltip"), Client.marioMode, value -> Client.marioMode = value));
        list.addSingleOptionEntry(OptionBuilder.buildDoubleSlider("sb.options.screenShake", Text.translatable("sb.options.screenShake.tooltip"), Client.screenShake, new Maths.Range(0.0, 5.0), 100.0, value -> Client.screenShake = value.floatValue()));
        list.addSingleOptionEntry(OptionBuilder.buildDoubleSlider("sb.options.rotateSpeed", Text.translatable("sb.options.rotateSpeed.tooltip"), Client.rotateSpeed, new Maths.Range(-10.0, 10.0), 100.0, value -> Client.rotateSpeed = value.floatValue()));
        list.addSingleOptionEntry(OptionBuilder.buildDoubleSlider("sb.options.rotateSharpness", Text.translatable("sb.options.rotateSharpness.tooltip"), Client.rotateSharpness, new Maths.Range(0.0, 100.0), 100.0, value -> Client.rotateSharpness = value.floatValue()));
        list.addSingleOptionEntry(OptionBuilder.buildDoubleSlider("sb.options.scoreFlashSpeed", Text.translatable("sb.options.scoreFlashSpeed.tooltip"), Client.scoreFlashSpeed, new Maths.Range(0.0, 20.0), 100.0, value -> Client.scoreFlashSpeed = value.floatValue()));

        addSelectableChild(list);
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            if(client != null) client.setScreen(this.parent);
        }).position(width / 2 - 100, height - 27).size(200, 20).build());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(drawContext);
        list.render(drawContext, mouseX, mouseY, delta);

        drawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(drawContext, mouseX, mouseY, delta);
    }
}
