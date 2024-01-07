/**
 * @file PlayerManager.java
 * @author gwerry
 * @brief The PlayerManager class is responsible for managing players in the SimpleFriends plugin.
 * @version 1.0
 * @date 2024/01/03
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

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.gwerry.io.LocalDB;
import com.gwerry.utils.Pair;

/**
 * @brief The PlayerManager class is responsible for managing players in the SimpleFriends plugin.
 * It provides methods to add, remove, and retrieve players, as well as manage friend requests.
 * This class uses a HashMap to store players and a List to store friend requests.
 *
 * @author gwerry
 * @since 1.0
 */
public class PlayerManager {
    private static ConcurrentMap<UUID, CustomPlayer> players = new ConcurrentHashMap<>();
    private static LocalDB db;
    private static List<Pair<UUID, UUID>> friendRequests = Collections.synchronizedList(new ArrayList<>());

    /**
     * @brief Initializes the PlayerManager by getting the database instance from the SimpleFriends plugin.
     */
    public static void init() {
        db = SimpleFriends.getDB();
    }

    /**
     * @brief Retrieves all online players.
     *
     * @return A Map of all online players.
     */
    public static Map<UUID, CustomPlayer> getPlayers() {
        return players;
    }

    /**
     * @brief Retrieves a player by their UUID.
     *
     * @param uuid The UUID of the player.
     * @return The CustomPlayer object representing the player, or null if the player is not found.
     */
    public static CustomPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    /**
     * @brief Adds a player to the manager.
     *
     * @param uuid The UUID of the player to add.
     * @return The CustomPlayer object representing the added player.
     * @throws NullPointerException If the player is null.
     */
    public static CustomPlayer addPlayer(UUID uuid) {
        CustomPlayer player = db.loadPlayer(uuid);
        if(player.getPlayer() == null) throw new NullPointerException("Player is null. UUID: " + uuid);
        synchronized (players) {
            players.putIfAbsent(uuid, player);
        }
        return player;
    }

    /**
     * @brief Removes a player from the manager.
     *
     * @param uuid The UUID of the player to remove.
     */
    public static void removePlayer(UUID uuid) {
        synchronized (players) {
            if(players.containsKey(uuid)) {
                savePlayer(players.get(uuid));
                players.remove(uuid);
            }
        }
    }

    /**
     * @brief Checks if a player is online.
     *
     * @param uuid The UUID of the player to check.
     * @return true if the player is online, false otherwise.
     */
    public static boolean isOnline(UUID uuid) {
        return players.containsKey(uuid);
    }

    /**
     * @brief Creates a friend request between two players.
     *
     * @param send The UUID of the player sending the request.
     * @param other The UUID of the player receiving the request.
     */
    public static boolean createFriendRequest(UUID send, UUID other) {
        CustomPlayer sender = getPlayer(send);
        CustomPlayer reciever = getPlayer(other);

        if(getOutgoingFriendRequests(sender).contains(other)) {
            sender.getPlayer().sendMessage(Data.FRIEND_REQUEST_ALREADY_SENT.replace("%reciever_name%", reciever.getPlayer().getName()));
            return false;
        }

        if(getIncomingFriendRequests(sender).contains(other)) {
            sender.getPlayer().sendMessage(Data.FRIEND_REQUEST_ALREADY_RECIEVED.replace("%reciever_name%", reciever.getPlayer().getName()));
            return false;
        }

        Pair<UUID, UUID> pair = new Pair<>(send, other);
        friendRequests.add(pair);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(120000);
                    synchronized(friendRequests) {
                        friendRequests.remove(pair);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();

        return true;
    }

    /**
     * @brief Retrieves all of the incoming friend requests of a player.
     *
     * @param player The player whose friend requests to retrieve.
     * @return An ArrayList of UUIDs representing the friend requests of the player.
     */
    public static ArrayList<UUID> getIncomingFriendRequests(CustomPlayer player) {
        ArrayList<UUID> others = new ArrayList<>();
        UUID toSearch = player.getPlayer().getUniqueId();

        synchronized(friendRequests) {
            for(Pair<UUID, UUID> request : friendRequests) {
                if(request.second.equals(toSearch)) {
                    others.add(request.first);
                    continue;
                }
            }
        }

        return others;
    }

        /**
     * @brief Retrieves all of the outgoing friend requests of a player.
     *
     * @param player The player whose friend requests to retrieve.
     * @return An ArrayList of UUIDs representing the friend requests of the player.
     */
    public static ArrayList<UUID> getOutgoingFriendRequests(CustomPlayer player) {
        ArrayList<UUID> others = new ArrayList<>();
        UUID toSearch = player.getPlayer().getUniqueId();

        synchronized(friendRequests) {
            for(Pair<UUID, UUID> request : friendRequests) {
                if(request.first.equals(toSearch)) {
                    others.add(request.second);
                    continue;
                }
            }
        }

        return others;
    }

    /**
     * @brief Removes a friend request between the specified players.
     *
     * @param affected The UUID of the player who sent the request.
     * @param toRemove The UUID of the player who recieved the request.
     */
    public static void removeRequest(UUID senderID, UUID recieverID) {
        synchronized(friendRequests) {
            for(Pair<UUID, UUID> request : friendRequests) {
                if(request.first.equals(senderID) && request.second.equals(recieverID)) {
                    friendRequests.remove(request);
                    break;
                }
            }
        }
    }

    /**
     * @brief Updates the friends list of a player when a friend is removed.
     *
     * @param affected The UUID of the player whose friends list to update.
     * @param toRemove The UUID of the friend to remove.
     */
    public static void updateFriendsRemove(UUID affected, UUID toRemove) {
        db.savePlayerRemoveFriend(affected, toRemove);
    }

    /**
     * @brief Updates the friends list of a player when a friend is added.
     *
     * @param affected The UUID of the player whose friends list to update.
     * @param toRemove The UUID of the friend to add.
     */
    public static void updateFriendsAdd(UUID affected, UUID toAdd) {
        db.savePlayerAddFriend(affected, toAdd);
    }

    /**
     * @brief Saves a player's data to the database.
     *
     * @param player The player whose data to save.
     */
    public static void savePlayer(CustomPlayer player) {
        db.savePlayer(player);
    }
}
