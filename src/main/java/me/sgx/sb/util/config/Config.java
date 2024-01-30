package me.sgx.sb.util.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Config {
    private final HashMap<String, String> config = new HashMap<>();
    private final String path;

    public Config(String folder, String file) {
        path = FabricLoader.getInstance().getConfigDir().toString() + '\\' + folder + '\\' + file;

        try {
            Path filePath = Path.of(path);
            Files.createDirectories(filePath.getParent());
            if(!Files.exists(filePath)) Files.createFile(filePath);

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;

            while((line = reader.readLine()) != null) {
                String[] data = line.split("=");
                config.put(data[0], data[1]);
                System.out.println(line);
            }

            reader.close();
        } catch(Exception ignored) { }

    }

    public void setValue(String key, Object value) {
        config.put(key, value.toString());
    }
    public void save() {
        try {
            Path filePath = Path.of(path);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for(String key : config.keySet()) {
                writer.write(key + '=' + config.getOrDefault(key, "null"));
                writer.newLine();
            }

            writer.close();
        } catch(Exception ignored) { }
    }

    public String getStringOrDefault(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }
    public float getFloatOrDefault(String key, float defaultValue) {
        return Float.parseFloat(config.getOrDefault(key, String.valueOf(defaultValue)));
    }
    public double getDoubleOrDefault(String key, double defaultValue) {
        return Double.parseDouble(config.getOrDefault(key, String.valueOf(defaultValue)));
    }
    public int getIntOrDefault(String key, int defaultValue) {
        return Integer.parseInt(config.getOrDefault(key, String.valueOf(defaultValue)));
    }
    public long getLongOrDefault(String key, long defaultValue) {
        return Long.parseLong(config.getOrDefault(key, String.valueOf(defaultValue)));
    }
    public boolean getBoolOrDefault(String key, boolean defaultValue) {
        return Boolean.parseBoolean(config.getOrDefault(key, String.valueOf(defaultValue)));
    }
}