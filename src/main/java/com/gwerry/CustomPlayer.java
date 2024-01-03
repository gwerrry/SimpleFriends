//todo docs
package com.gwerry;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class CustomPlayer {
    private final Player player; // player object
    private ArrayList<UUID> friends; // uuif of all friends

    public CustomPlayer(Player p, ArrayList<UUID> friends) {
        this.player = p;
        this.friends = (friends != null) ? friends : new ArrayList<UUID>();
    }

    public void inviteFriend(CustomPlayer p) {
        if(p == this) {
            player.sendMessage(Messages.CANT_FRIEND_SELF);
            return;
        }

        if(p == null) {
            player.sendMessage(Messages.PLAYER_NOT_ONLINE);
            return;
        }

        Player recieverPlayer = p.getPlayer();

        if(isFriend(p)) {
            player.sendMessage(Messages.ALREADY_FRIENDS.replace("%reciever_name%", recieverPlayer.getName()));
            return;
        }

        player.sendMessage(Messages.FRIEND_REQUEST_SENDER.replace("%reciever_name%", recieverPlayer.getName()));
        recieverPlayer.sendMessage(Messages.FRIEND_REQUEST_RECIEVER.replace("%sender_name%", player.getName()));

        PlayerManager.createFriendRequest(player.getUniqueId(), recieverPlayer.getUniqueId());
    }

    //todo implement this
    //public void kickFriend(String name);

    public boolean isFriend(CustomPlayer other) {
        return friends.contains(other.getPlayer().getUniqueId());
    }

    public ArrayList<UUID> getFriendRequests() {
        return PlayerManager.getFriendRequests(this);
    }

    public ArrayList<UUID> getFriendList() {
        return friends;
    }

    public Player getPlayer() {
        return player;
    }

    public void savePlayerData() {
        PlayerManager.savePlayer(this);
    }
}
