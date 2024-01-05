//todo docs
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

public class SimpleFriends extends JavaPlugin {
    private static SimpleFriends instance;
    private static LocalDB userData;
    private static IYamlConfig config;

    public static SimpleFriends getInstance() {
        return instance;
    }

    public static LocalDB getDB() {
        return userData;
    }

    public static IYamlConfig getConfigFile() {
        return config;
    }

    @Override
    public void onEnable() {
        Data.FRIEND_CMD_ALIASES.add("f");
        registerCommand(new FriendCommand(Data.FRIEND_CMD, Data.FRIEND_CMD_DESCRIPTION, Data.FRIEND_CMD_USAGE, Data.FRIEND_CMD_ALIASES));

        getLogger().info("Registering listeners...");
        PluginManager pman = Bukkit.getPluginManager();
        pman.registerEvents(new OnJoinListener(), this);
        pman.registerEvents(new OnLeaveListener(), this);
    }

    @Override
    public void onLoad() {
        instance = this;

        try {
            config = new YamlConfig(this, "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Data.loadFromFile(config);

        userData = new LocalDB("friends.db", this.getDataFolder().getAbsolutePath());
        PlayerManager.init();
    }

    @Override
    public void onDisable(){
        System.out.println("bye bye");
    }

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
