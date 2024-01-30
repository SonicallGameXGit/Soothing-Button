package me.sgx.sb;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.sgx.sb.hud.ModMenuOptions;
import me.sgx.sb.util.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class Client implements ClientModInitializer, ModMenuApi {
    public static long score = 0;

    public static float screenShake = 2.5f, rotateSpeed = 3.0f, rotateSharpness = 10.0f, scoreFlashSpeed = 13.6f;
    public static boolean marioMode = false;

    public static Config config;

    @Override
    public void onInitializeClient() {
        config = new Config("Soothing Button", "config.cfg");
        score = config.getLongOrDefault("score", score);
        screenShake = config.getFloatOrDefault("screenShake", screenShake);
        rotateSpeed = config.getFloatOrDefault("rotateSpeed", rotateSpeed);
        rotateSharpness = config.getFloatOrDefault("rotateSharpness", rotateSharpness);
        scoreFlashSpeed = config.getFloatOrDefault("scoreFlashSpeed", scoreFlashSpeed);
        marioMode = config.getBoolOrDefault("marioMode", marioMode);

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            Client.config.setValue("score", Client.score);
            Client.config.setValue("screenShake", Client.screenShake);
            Client.config.setValue("rotateSpeed", Client.rotateSpeed);
            Client.config.setValue("rotateSharpness", Client.rotateSharpness);
            Client.config.setValue("scoreFlashSpeed", Client.scoreFlashSpeed);
            Client.config.setValue("marioMode", Client.marioMode);
            Client.config.save();
        });
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuOptions::new;
    }
}
