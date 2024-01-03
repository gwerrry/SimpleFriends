package com.gwerry;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.entity.Player;

import com.gwerry.CustomPlayer;
import com.gwerry.io.LocalDB;

public class PlayerManager {
    private static HashMap<UUID, CustomPlayer> players = new HashMap<>();
    private static LocalDB db;
    private static List<Pair<UUID, UUID>> friendRequests = Collections.synchronizedList(new ArrayList()<>());

    public static void init() {
        db = SimpleFriends.getDB();
    }

    public static HashMap<UUID, CustomPlayer> getPlayers() {
        return players;
    }

    public static void addPlayer(UUID uuid) {
        CustomPlayer player = db.loadPlayer(uuid);
        if(player.getPlayer() == null) {
            throw new NullPointerException("Player is null. UUID: " + uuid);
            return;
        }
        players.putIfAbsent(uuid, player);
    }

    public static void removePlayer(UUID uuid) {
        if(players.containsKey(uuid)) {
            savePlayer(players.get(uuid));
            players.remove(uuid);
        }
    }

    public static boolean isOnline(UUID uuid) {
        return players.containsKey(uuid);
    }

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

    public static void updateFriendsRemove(UUID affected, UUID toRemove) {
        db.savePlayerRemoveFriend(affected, toRemove);
    }

    public static void updateFriendsAdd(UUID affected, UUID toRemove) {
        db.savePlayerAddFriend(affected, toRemove);
    }

    public static void savePlayer(CustomPlayer player) {
        db.savePlayer(player);
    }
}
