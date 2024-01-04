package com.gwerry.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.gwerry.CustomPlayer;
import com.gwerry.Messages;
import com.gwerry.PlayerManager;
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
        Player sendPlayer = (Player)sender;
        CustomPlayer senderCustomPlayer = PlayerManager.getPlayer(sendPlayer.getUniqueId());
        if(args.length == 0) {
            sendPlayer.sendMessage(Messages.FRIEND_HELP_TEXT);
            return true;
        }

        //
        // no arg commands
        //
        if(args[0].equalsIgnoreCase("help")) {
            sendPlayer.sendMessage(Messages.FRIEND_HELP_TEXT);
            return true;
        }

        else if(args[0].equalsIgnoreCase("list")) {
            String toSend = Messages.FRIEND_LIST;
            StringBuilder friendList = new StringBuilder();
            ArrayList<UUID> uuids = senderCustomPlayer.getFriendList();

            for(UUID uuid : uuids) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                String toAppend = Messages.FRIEND_LIST_SINGLE.replace("%friend_name%", p.getName());
                if(p.isOnline()) friendList.append(toAppend.replace("%online_status%", Messages.ONLINE_TEXT) + "\n");
                else friendList.append(toAppend.replace("%online_status%", Messages.OFFLINE_TEXT) + "\n");
            }
            sendPlayer.sendMessage(toSend.replace("%friends%", friendList.toString()));

            return true;
        }

        //
        // single arg commands
        //

        if(args.length != 2) {
            sendPlayer.sendMessage(Messages.FRIEND_CMD_USAGE);
            return true;
        }

        if(args[0].equalsIgnoreCase("invite")) {
            Player p = Bukkit.getPlayer(args[1]);
            if(p == null) {
                sendPlayer.sendMessage(Messages.PLAYER_NOT_ONLINE);
                return true;
            }

            CustomPlayer target = PlayerManager.getPlayer(p.getUniqueId());
            senderCustomPlayer.inviteFriend(target);
        } else if(args[0].equalsIgnoreCase("kick")) {
            senderCustomPlayer.kickFriend(args[1]);
        } else if (args[0].equalsIgnoreCase("accept")) {
            senderCustomPlayer.acceptInvite(args[1]);
        } else if (args[0].equalsIgnoreCase("deny")) {
            senderCustomPlayer.denyInvite(args[1]);
        } else {
            sendPlayer.sendMessage(Messages.FRIEND_CMD_USAGE);
        }

        return true;
    }
}
