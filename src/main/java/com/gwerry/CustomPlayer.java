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

        boolean created = PlayerManager.createFriendRequest(player.getUniqueId(), recieverPlayer.getUniqueId());
        if(created) {
            player.sendMessage(Messages.FRIEND_REQUEST_SENDER.replace("%reciever_name%", recieverPlayer.getName()));
            recieverPlayer.sendMessage(Messages.FRIEND_REQUEST_RECIEVER.replace("%sender_name%", player.getName()));
        }
    }

    public void kickFriend(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
             player.sendMessage(Messages.PLAYER_NOT_EXISTS);
            return;
        }

        if(!friends.contains(id)){
            player.sendMessage(Messages.FRIEND_NOT_FOUND.replace("%reciever_name%", name));
            return;
        }

        friends.remove(id);

        savePlayerData();

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.removeFriend(player.getUniqueId());
            target.getPlayer().sendMessage(Messages.FRIEND_REMOVE.replace("%reciever_name%", player.getName()));
        }

        PlayerManager.updateFriendsRemove(id, player.getUniqueId());

        player.sendMessage(Messages.FRIEND_REMOVE.replace("%reciever_name%", name));
    }

    public boolean isFriend(CustomPlayer other) {
        return friends.contains(other.getPlayer().getUniqueId());
    }

    public void acceptInvite(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
            player.sendMessage(Messages.PLAYER_NOT_EXISTS);
            return;
        }

        if(!getIncomingFriendRequests().contains(id)) {
            player.sendMessage(Messages.FRIEND_REQUEST_NOT_EXIST.replace("%reciever_name%", name));
            return;
        }

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.addFriend(player.getUniqueId());
            target.getPlayer().sendMessage(Messages.FRIEND_REQUEST_ACCEPT.replace("%sender_name%", player.getName()));
        }

        PlayerManager.updateFriendsAdd(id, player.getUniqueId());

        addFriend(id);
        savePlayerData();
        player.sendMessage(Messages.FRIEND_REQUEST_ACCEPT.replace("%sender_name%", name));
        PlayerManager.removeRequest(id, player.getUniqueId());
    }

    public void denyInvite(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
            player.sendMessage(Messages.PLAYER_NOT_EXISTS);
            return;
        }

        if(!getIncomingFriendRequests().contains(id)) {
            player.sendMessage(Messages.FRIEND_REQUEST_NOT_EXIST.replace("%reciever_name%", name));
            return;
        }

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.getPlayer().sendMessage(Messages.FRIEND_REQUEST_DENY_RECEIVER.replace("%sender_name%", name));
        }

        player.sendMessage(Messages.FRIEND_REQUEST_DENY_SENDER.replace("%reciever_name%", name));

        PlayerManager.removeRequest(id, player.getUniqueId());
    }

    public void addFriend(UUID other) {
        friends.add(other);
    }

    public void removeFriend(UUID other) {
        friends.remove(other);
    }
    public ArrayList<UUID> getIncomingFriendRequests() {
        return PlayerManager.getIncomingFriendRequests(this);
    }

    public ArrayList<UUID> getOutgoingFriendRequests() {
        return PlayerManager.getOutgoingFriendRequests(this);
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
