package me.sgx.sb.mixin;

import me.sgx.sb.Client;
import me.sgx.sb.hud.FallingButton;
import me.sgx.sb.util.Maths;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique private static final FallingButton fallingButton = new FallingButton(117, 162, 20, 20);

    @Inject(at=@At("HEAD"), method="mouseClicked")
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if(Maths.isPointInsideAabb((float) mouseX, (float) mouseY, fallingButton.getX(), fallingButton.getY(), fallingButton.getWidth(), fallingButton.getHeight()))
                fallingButton.onClick(mouseX, mouseY);
        }
    }

    @Inject(at=@At("HEAD"), method="init")
    public void init(CallbackInfo callbackInfo) {
        fallingButton.x = ((Screen) (Object) this).width / 2.0f - 124.0f;
        fallingButton.y = ((Screen) (Object) this).height / 4.0f + 96.0f;
        fallingButton.vx = 0.0f;
        fallingButton.vy = 0.0f;
        fallingButton.canTouch = true;
        fallingButton.rawAngle = 0.0f;
        fallingButton.angle = 0.0f;
        fallingButton.touchFlashState = 0.0f;
        fallingButton.playing = false;
    }

    @Inject(at=@At("HEAD"), method="render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo callbackInfo) {
        fallingButton.render(context, mouseX, mouseY, delta);
        context.drawText(MinecraftClient.getInstance().textRenderer, "Score: " + Client.score, 5, 5, new Color(1.0f, 1.0f, MathHelper.clamp(1.0f - fallingButton.touchFlashState, 0.0f, 1.0f)).getRGB(), true);
    }
}