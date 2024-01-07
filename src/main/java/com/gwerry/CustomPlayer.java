/**
 * @file CustomPlayer.java
 * @author gwerry
 * @brief The CustomPlayer class is a class that represents a custom player with a list of friends.
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

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.gwerry.utils.UuidUtil;

/**
 * @brief A class that represents a custom player with a list of friends.
 *
 * @author gwerry
 * @version 1.0
 */
public class CustomPlayer {
    private final Player player; // player object
    private ArrayList<UUID> friends; // uuif of all friends

    /**
     * @brief Constructs a new CustomPlayer with the given player and friends.
     * @param p the player object
     * @param friends the list of friends' UUIDs, or null if none
     */
    public CustomPlayer(Player p, ArrayList<UUID> friends) {
        this.player = p;
        this.friends = (friends != null) ? friends : new ArrayList<UUID>();
    }

    /**
     * @brief Invites another CustomPlayer to be a friend.
     * @param p the CustomPlayer to invite
     */
    public void inviteFriend(CustomPlayer p) {
        if(p == this) {
            player.sendMessage(Data.CANT_FRIEND_SELF);
            return;
        }

        if(p == null) {
            player.sendMessage(Data.PLAYER_NOT_ONLINE);
            return;
        }

        Player recieverPlayer = p.getPlayer();

        if(isFriend(p)) {
            player.sendMessage(Data.ALREADY_FRIENDS.replace("%reciever_name%", recieverPlayer.getName()));
            return;
        }

        boolean created = PlayerManager.createFriendRequest(player.getUniqueId(), recieverPlayer.getUniqueId());
        if(created) {
            player.sendMessage(Data.FRIEND_REQUEST_SENDER.replace("%reciever_name%", recieverPlayer.getName()));
            recieverPlayer.sendMessage(Data.FRIEND_REQUEST_RECIEVER.replace("%sender_name%", player.getName()));
        }
    }

    /**
     * @brief Removes a friend by name.
     * @param name the name of the friend to remove
     */
    public void kickFriend(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
             player.sendMessage(Data.PLAYER_NOT_EXISTS);
            return;
        }

        if(!friends.contains(id)){
            player.sendMessage(Data.FRIEND_NOT_FOUND.replace("%reciever_name%", name));
            return;
        }

        friends.remove(id);

        savePlayerData();

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.removeFriend(player.getUniqueId());
            target.getPlayer().sendMessage(Data.FRIEND_REMOVE.replace("%reciever_name%", player.getName()));
        }

        PlayerManager.updateFriendsRemove(id, player.getUniqueId());

        player.sendMessage(Data.FRIEND_REMOVE.replace("%reciever_name%", name));
    }

    /**
     * @brief Checks if another CustomPlayer is a friend.
     * @param other the other CustomPlayer to check
     * @return true if they are friends, false otherwise
     */
    public boolean isFriend(CustomPlayer other) {
        return friends.contains(other.getPlayer().getUniqueId());
    }

    /**
     * @brief Accepts a friend request from another player by name.
     * @param name the name of the player who sent the request
     */
    public void acceptInvite(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
            player.sendMessage(Data.PLAYER_NOT_EXISTS);
            return;
        }

        if(!getIncomingFriendRequests().contains(id)) {
            player.sendMessage(Data.FRIEND_REQUEST_NOT_EXIST.replace("%reciever_name%", name));
            return;
        }

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.addFriend(player.getUniqueId());
            target.getPlayer().sendMessage(Data.FRIEND_REQUEST_ACCEPT.replace("%sender_name%", player.getName()));
        }

        PlayerManager.updateFriendsAdd(id, player.getUniqueId());

        addFriend(id);
        savePlayerData();
        player.sendMessage(Data.FRIEND_REQUEST_ACCEPT.replace("%sender_name%", name));
        PlayerManager.removeRequest(id, player.getUniqueId());
    }

    /**
     * @brief Denies a friend request from another player by name.
     * @param name the name of the player who sent the request
     */
    public void denyInvite(String name) {
        UUID id = UuidUtil.getUuid(name);
        if(id == null) {
            player.sendMessage(Data.PLAYER_NOT_EXISTS);
            return;
        }

        if(!getIncomingFriendRequests().contains(id)) {
            player.sendMessage(Data.FRIEND_REQUEST_NOT_EXIST.replace("%reciever_name%", name));
            return;
        }

        CustomPlayer target = PlayerManager.getPlayer(id);
        if(target != null) {
            target.getPlayer().sendMessage(Data.FRIEND_REQUEST_DENY_RECEIVER.replace("%sender_name%", name));
        }

        player.sendMessage(Data.FRIEND_REQUEST_DENY_SENDER.replace("%reciever_name%", name));

        PlayerManager.removeRequest(id, player.getUniqueId());
    }

    /**
     * @brief Adds a friend by UUID.
     * @param other the UUID of the friend to add
     */
    public void addFriend(UUID other) {
        friends.add(other);
    }

    /**
     * @brief Removes a friend by UUID.
     * @param other the UUID of the friend to remove
     */
    public void removeFriend(UUID other) {
        friends.remove(other);
    }

    /**
     * @brief Gets the list of incoming friend requests.
     * @return an ArrayList of UUIDs representing the incoming requests
     */
    public ArrayList<UUID> getIncomingFriendRequests() {
        return PlayerManager.getIncomingFriendRequests(this);
    }

    /**
     * @brief Gets the list of outgoing friend requests.
     * @return an ArrayList of UUIDs representing the outgoing requests
     */
    public ArrayList<UUID> getOutgoingFriendRequests() {
        return PlayerManager.getOutgoingFriendRequests(this);
    }

    /**
     * @brief Gets the list of friends.
     * @return an ArrayList of UUIDs representing the friends
     */
    public ArrayList<UUID> getFriendList() {
        return friends;
    }

    /**
     * @brief Gets the player object.
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @brief Saves the player data to the file system.
     */
    public void savePlayerData() {
        PlayerManager.savePlayer(this);
    }
}
