/**
 * @file YamlConfig.java
 * @author gwerry
 * @brief The YamlConfig class is a class that implements the IYamlConfig interface.
 * @version 1.0
 * @date 2024/01/06
 *
 * Copyright 2024 gwerry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gwerry.io;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @brief YamlConfig is a class that implements the IYamlConfig interface.
 * It manages the YAML configuration file of the plugin.
 * It provides methods to get the configuration, save the default configuration, and save the configuration.
 *
 * @author gwerry
 * @version 1.0
 */
public class YamlConfig implements IYamlConfig {
    private final JavaPlugin plugin;
    private final File configFile;
    private final YamlConfiguration yamlConfig;

    /**
     * @brief Constructs a new YamlConfig object.
     * @param plugin The JavaPlugin object associated with the plugin.
     * @param filename The name of the configuration file.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * @brief Returns the YAML configuration.
     * @return The YAML configuration.
     */
    public YamlConfiguration getConfig() {
        return this.yamlConfig;
    }

    /**
     * @brief Saves the default configuration to the configuration file.
     * @throws IOException If an I/O error occurs.
     */
    public void saveDefaultConfiguration() throws IOException {
        InputStream input = plugin.getResource(configFile.getName());
        Files.copy(input, configFile.toPath());
    }

    /**
     * @brief Saves the configuration to the configuration file.
     */
    public void saveConfig() {
        try {
            yamlConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}