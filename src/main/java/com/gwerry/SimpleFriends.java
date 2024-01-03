package com.gwerry;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;
import com.gwerry.io.LocalDB;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class SimpleFriends extends JavaPlugin {
    private static SimpleFriends instance;
    private static LocalDB userData;

    public static SimpleFriends getInstance() {
        return instance;
    }

    public static LocalDB getDB() {
        return userData;
    }

    @Override
    public void onEnable() {
        System.out.println("hello");
    }

    @Override
    public void onLoad() {
        instance = this;
        userData = new LocalDB(this.getDataFolder().getAbsolutePath(), "friends.db");
        //commands registered here
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
