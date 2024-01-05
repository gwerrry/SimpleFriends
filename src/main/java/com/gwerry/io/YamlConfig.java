package com.gwerry.io;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

//todo doc stuff
public class YamlConfig implements IYamlConfig {
    private final JavaPlugin plugin;
    private final File configFile;
    private final YamlConfiguration yamlConfig;

    public YamlConfig(JavaPlugin plugin, String filename) throws IOException {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), filename);
        if (!(plugin.getDataFolder().exists()) && !plugin.getDataFolder().mkdirs()) {
            Bukkit.getServer().shutdown();
        }

        if (!configFile.exists()) {
            saveDefaultConfiguration();
        }

        this.yamlConfig = YamlConfiguration.loadConfiguration(configFile);
    }

    public YamlConfiguration getConfig() {
        return this.yamlConfig;
    }

    public void saveDefaultConfiguration() throws IOException {

        InputStream input = plugin.getResource(configFile.getName());
        java.nio.file.Files.copy(input, configFile.toPath());

    }

    public void saveConfig() {
        try {
            yamlConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}