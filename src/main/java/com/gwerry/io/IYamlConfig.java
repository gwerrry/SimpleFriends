package com.gwerry.io;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

//todo doc stuff
public interface IYamlConfig {

    /**
     * Gets the configuration.
     *
     * @return Current configuration
     */
    YamlConfiguration getConfig();

    /**
     * Saves the default, pre-set, configuration.
     *
     * @throws IOException This usually should have to work with copying files.
     */
    void saveDefaultConfiguration() throws IOException;


    /**
     * Saves the current configuration.
     *
     * @throws IOException This usually should have to work with saving files.
     */
    void saveConfig() throws IOException;
}