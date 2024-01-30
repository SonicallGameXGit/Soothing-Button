package me.sgx.sb.hud;

import me.sgx.sb.Client;
import me.sgx.sb.util.Maths;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FallingButton extends ButtonWidget {
    public static final Identifier DEFAULT_TEXTURE = new Identifier("sb", "textures/button.png");
    public static final Identifier MARIO_TEXTURE = new Identifier("sb", "textures/mario.png");
    public static final Identifier MARIO_HOVERED_TEXTURE = new Identifier("sb", "textures/mario_hovered.png");
    public static final Identifier MARIO_FALLING_TEXTURE = new Identifier("sb", "textures/mario_falling.png");

    public static final SoundEvent JUMP_SOUND = SoundEvent.of(new Identifier("sb", "jump"));
    public static final SoundEvent MARIO_HIT_SOUND = SoundEvent.of(new Identifier("sb", "mario_hit"));
    public static final SoundEvent MARIO_QUOTE_SOUND = SoundEvent.of(new Identifier("sb", "mario_quote"));

    public static final float GRAVITY = 302.76f, BOUNCINESS = 0.5f, JUMP_WIDTH = 1024.0f, JUMP_HEIGHT = -663.0f, BORDER_ACCELERATION = 0.2f, AIR_ACCELERATION = 0.000038f;

    public float x, y, vx, vy, rawAngle, angle;
    public boolean canTouch = false, playing = false;

    public float touchFlashState = 0.0f, marioQuoteTimer = 0.0f, marioQuoteTime = (float) Math.random() * 20.0f + 5.0f;

    public static long lastFrameTime = System.currentTimeMillis();

    public FallingButton(int x, int y, int width, int height) {
        super(x, y, width, height, Text.of(" "), button -> {
            FallingButton fallingButton = (FallingButton) button;
            fallingButton.vx = (float) Math.random() * JUMP_WIDTH - JUMP_WIDTH / 2.0f;
            fallingButton.vy = JUMP_HEIGHT;

            ((FallingButton) button).playing = true;

            MinecraftClient client = MinecraftClient.getInstance();
            if(client != null)
                client.getSoundManager().play(PositionedSoundInstance.master(JUMP_SOUND, 1.0f, 0.06f));
        }, textSupplier -> MutableText.of(TextContent.EMPTY));

        this.x = x;
        this.y = y;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        long time = System.currentTimeMillis();
        float realDelta = (time - lastFrameTime) / 1000.0f;
        lastFrameTime = time;

        boolean anyTouched = false;
        if(playing) {
            vy += GRAVITY * realDelta;

            x += vx * realDelta;
            y += vy * realDelta;

            if(x > context.getScaledWindowWidth() - getWidth()) {
                x = context.getScaledWindowWidth() - getWidth();

                vx *= -BOUNCINESS;
                vy /= 1.0f + BORDER_ACCELERATION;

                anyTouched = true;
            }
            if(x <= 0.0f) {
                x = 0.0f;

                vx *= -BOUNCINESS;
                vy /= 1.0f + BORDER_ACCELERATION;

                anyTouched = true;
            }
            if(y > context.getScaledWindowHeight() - getHeight()) {
                y = context.getScaledWindowHeight() - getHeight();

                vy *= -BOUNCINESS;
                vx /= 1.0f + BORDER_ACCELERATION;

                anyTouched = true;
            }
            if(y <= 0.0f) {
                y = 0.0f;

                vy *= -BOUNCINESS;
                vx /= 1.0f + BORDER_ACCELERATION;

                anyTouched = true;
            }

            if(anyTouched) {
                rawAngle = (float) Math.round(rawAngle / 90.0) * 90.0f;

                if(canTouch) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(Client.marioMode ? MARIO_HIT_SOUND : SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f - (float) Math.max(Math.min(new Vec2f(vx, vy).length() / ((JUMP_WIDTH + JUMP_HEIGHT) / 2.0) / 10.0f, 0.7), 0.0), (float) Math.max(Math.min(new Vec2f(vx, vy).length() / ((JUMP_WIDTH + JUMP_HEIGHT) / 2.0) / 10.0f, 0.5), 0.0)));

                    Client.score++;
                    touchFlashState = 1.0f;

                    canTouch = false;
                }
            } else canTouch = true;

            if(Client.marioMode && anyTouched) {
                marioQuoteTimer += realDelta;

                if(marioQuoteTimer >= marioQuoteTime) {
                    marioQuoteTimer = 0.0f;
                    marioQuoteTime = (float) Math.random() * 20.0f + 5.0f;

                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(MARIO_QUOTE_SOUND, 1.0f, 0.15f));
                }
            } else marioQuoteTimer = 0.0f;

            rawAngle += vx * Client.rotateSpeed * realDelta;
            angle = MathHelper.lerp(Client.rotateSharpness * realDelta, angle, rawAngle);

            vx /= 1.0f + AIR_ACCELERATION * realDelta;
            vy /= 1.0f + AIR_ACCELERATION * realDelta;

            touchFlashState = MathHelper.lerp(Client.scoreFlashSpeed * realDelta, touchFlashState, 0.0f);
        }

        boolean hovered = Maths.isPointInsideAabb(mouseX, mouseY, getX(), getY(), getWidth(), getHeight());

        context.getMatrices().push();
        context.getMatrices().translate(getX() + getWidth() / 2.0f, getY() + getHeight() / 2.0f, 0.0f);
        context.getMatrices().multiply(new Quaternionf().rotateAxis((float) Math.toRadians(Math.round(angle / 5.0f) * 5.0f), new Vector3f(0.0f, 0.0f, 1.0f)));
        context.getMatrices().translate(-(getX() + getWidth() / 2.0f), -(getY() + getHeight() / 2.0f), 0.0f);

        if(!Client.marioMode) {
            super.render(context, mouseX, mouseY, delta);
            drawTexture(context, DEFAULT_TEXTURE, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        } else {
            Identifier texture = MARIO_TEXTURE;
            if(!anyTouched && playing) texture = MARIO_FALLING_TEXTURE;
            if(hovered) texture = MARIO_HOVERED_TEXTURE;

            context.setShaderColor(0.0f, 0.0f, 0.0f, 0.2f);
            drawTexture(context, texture, getX() + (int) (Math.sin(Math.toRadians(Math.round(angle / 5.0f) * 5.0f)) * 1.5), getY() + (int) (Math.cos(Math.toRadians(Math.round(angle / 5.0f) * 5.0f)) * 1.5), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
            context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            drawTexture(context, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        }


        context.getMatrices().pop();

        context.drawBorder(getX(), getY(), getWidth(), getHeight(), 0xFF0000);
        context.getMatrices().translate(MathHelper.lerp(touchFlashState, 0.0f, (float) (Math.random() * vx * Client.screenShake / JUMP_WIDTH)), MathHelper.lerp(touchFlashState, 0.0f, (float) (Math.random() * vy * Client.screenShake / JUMP_HEIGHT)), 0.0f);

        setX((int) x);
        setY((int) y);
    }
}
