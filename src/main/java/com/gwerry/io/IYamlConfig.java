/**
 * @file IYamlConfig.java
 * @author gwerry
 * @brief The IYamlConfig is a Bukkit config file interface.
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

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

/**
 * @brief The IYamlConfig is a Bukkit config file interface.
 *
 * @author gwerry
 * @version 1.0
 */
public interface IYamlConfig {

    /**
     * @brief Gets the configuration.
     *
     * @return Current configuration
     */
    YamlConfiguration getConfig();

    /**
     * @brief Saves the default, pre-set, configuration.
     *
     * @throws IOException This usually should have to work with copying files.
     */
    void saveDefaultConfiguration() throws IOException;


    /**
     * @brief Saves the current configuration.
     *
     * @throws IOException This usually should have to work with saving files.
     */
    void saveConfig() throws IOException;
}