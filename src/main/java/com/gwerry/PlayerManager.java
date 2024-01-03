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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import com.gwerry.io.LocalDB;

/**
 * @brief The PlayerManager class is responsible for managing players in the SimpleFriends plugin.
 * It provides methods to add, remove, and retrieve players, as well as manage friend requests.
 * This class uses a HashMap to store players and a List to store friend requests.
 *
 * @author gwerry
 * @since 1.0
 */
public class PlayerManager {
    private static HashMap<UUID, CustomPlayer> players = new HashMap<>();
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
     * @return A HashMap of all online players.
     */
    public static HashMap<UUID, CustomPlayer> getPlayers() {
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
        players.putIfAbsent(uuid, player);
        return player;
    }

    /**
     * @brief Removes a player from the manager.
     *
     * @param uuid The UUID of the player to remove.
     */
    public static void removePlayer(UUID uuid) {
        if(players.containsKey(uuid)) {
            savePlayer(players.get(uuid));
            players.remove(uuid);
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
    public static void createFriendRequest(UUID send, UUID other) {
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
    }

    /**
     * @brief Retrieves the friend requests of a player.
     *
     * @param player The player whose friend requests to retrieve.
     * @return An ArrayList of UUIDs representing the friend requests of the player.
     */
    public static ArrayList<UUID> getFriendRequests(CustomPlayer player) {
        ArrayList<UUID> others = new ArrayList<>();
        UUID toSearch = player.getPlayer().getUniqueId();

        synchronized(friendRequests) {
            for(Pair<UUID, UUID> request : friendRequests) {
                if(request.first == toSearch) {
                    others.add(request.second);
                    continue;
                }
                if(request.second == toSearch) {
                    others.add(request.first);
                    continue;
                }
            }
        }

        return others;
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
    public static void updateFriendsAdd(UUID affected, UUID toRemove) {
        db.savePlayerAddFriend(affected, toRemove);
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
