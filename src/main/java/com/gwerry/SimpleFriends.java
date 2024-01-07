/**
 * @file SimpleFriends.java
 * @author gwerry
 * @brief The SimpleFriends class is the main class of the plugin.
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
package com.gwerry;

import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import com.gwerry.commands.FriendCommand;
import com.gwerry.io.IYamlConfig;
import com.gwerry.io.LocalDB;
import com.gwerry.io.YamlConfig;
import com.gwerry.listeners.OnJoinListener;
import com.gwerry.listeners.OnLeaveListener;

/**
 * @brief The SimpleFriends class extends JavaPlugin and is the main class of the plugin.
 * It handles the enabling, disabling, and loading of the plugin, as well as the registration of commands and listeners.
 * It also provides static methods to get instances of the plugin, the database, and the configuration file.
 *
 * @author gwerry
 * @since 1.0
 */
public class SimpleFriends extends JavaPlugin {
    private static SimpleFriends instance;
    private static LocalDB userData;
    private static IYamlConfig config;

    /**
     * @brief Returns the instance of the SimpleFriends plugin.
     * @return The instance of the SimpleFriends plugin.
     */
    public static SimpleFriends getInstance() {
        return instance;
    }

    /**
     * @brief Returns the database of the SimpleFriends plugin.
     * @return The database of the SimpleFriends plugin.
     */
    public static LocalDB getDB() {
        return userData;
    }

    /**
     * @brief Returns the configuration file of the SimpleFriends plugin.
     * @return The configuration file of the SimpleFriends plugin.
     */
    public static IYamlConfig getConfigFile() {
        return config;
    }

    /**
     * @brief This method is called when the plugin is enabled.
     * It registers the commands and listeners of the plugin.
     */
    @Override
    public void onEnable() {
        getLogger().info("Registering commands...");
        Data.FRIEND_CMD_ALIASES.add("f");
        registerCommand(new FriendCommand(Data.FRIEND_CMD, Data.FRIEND_CMD_DESCRIPTION, Data.FRIEND_CMD_USAGE, Data.FRIEND_CMD_ALIASES));
        getLogger().info("Registered commands!");

        getLogger().info("Registering listeners...");
        PluginManager pman = Bukkit.getPluginManager();
        pman.registerEvents(new OnJoinListener(), this);
        pman.registerEvents(new OnLeaveListener(), this);
        getLogger().info("Registered listeners");
    }

    /**
     * @brief This method is called when the plugin is loaded.
     * It initializes the plugin, loads the configuration file, loads the database, and initializes the PlayerManager.
     */
    @Override
    public void onLoad() {
        getLogger().info("magic");
        instance = this;

        getLogger().info("magic..!\nLoading config.yml");
        try {
            config = new YamlConfig(this, "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data.loadFromFile(config);
        getLogger().info("Loaded config.yml!");

        getLogger().info("Loading database...");
        userData = new LocalDB("friends.db", this.getDataFolder().getAbsolutePath());
        getLogger().info("Loaded database!");

        PlayerManager.init();
        getLogger().info("Finished magic...");
    }

    /**
     * @brief This method is called when the plugin is disabled.
     * Currently, it only prints a goodbye message to the console.
     */
    @Override
    public void onDisable(){
        System.out.println("bye bye");
    }

    /**
     * @brief Registers a command with the server's CommandMap.
     * @param command The command to register.
     */
    private void registerCommand(Command command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(command.getLabel(), command);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
