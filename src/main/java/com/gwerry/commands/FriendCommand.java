package com.gwerry.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.gwerry.Messages;
import com.gwerry.SimpleFriends;

public class FriendCommand extends Command {
    private SimpleFriends plugin;

    public FriendCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        plugin = SimpleFriends.getInstance();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            plugin.getLogger().info(Messages.ONLY_PLAYER_COMMAND);
            return true;
        }

        sender.sendMessage("test");

        return true;
    }
}
