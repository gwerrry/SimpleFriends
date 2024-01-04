/**
 * @file LocalDB.java
 * @author gwerry
 * @brief The LocalDB class is responsible for managing the local database operations for the SimpleFriends plugin.
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
package com.gwerry.io;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.gwerry.CustomPlayer;
import com.gwerry.SimpleFriends;

/**
 * @brief The LocalDB class is responsible for managing the local database operations for the SimpleFriends plugin.
 * It provides methods to initialize and close the database connection, and to load player data from the database.
 * The class uses Apache Commons DBCP for connection pooling and SQLite as the database.
 *
 * @author gwerry
 * @since 1.0
 */
public class LocalDB {

    private final String SEP_DELIM = "|";
    private final SimpleFriends plugin;
    private final Logger logger;
    private BasicDataSource dataSource;

    /**
     * @brief Constructs a new LocalDB object.
     *
     * @param name The name of the database file.
     * @param path The path to the directory where the database file is located.
     */
    public LocalDB(String name, String path) {
        plugin = SimpleFriends.getInstance();
        logger = plugin.getLogger();

        File pathFolder = new File(path);
        if(!pathFolder.exists()) pathFolder.mkdirs();

        File dbFile = new File(path, name);

        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(30);
        dataSource.setMaxIdle(15);
        dataSource.setMinIdle(2);

        try (Connection conn = dataSource.getConnection()) {
            fillDefaultDB(conn);
            logger.info("Successfully setup database at: " + dbFile.getAbsolutePath());
        } catch (SQLException e) {
            logger.severe("Could not create/connect to database file at: " + dbFile.getAbsolutePath());
        }
    }

    /**
     * @brief Closes the connection pool, waiting for all active connections to finish their tasks before closing.
     */
    public void deinit() {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to close database connection pool!");
        }
    }

    /**
     * @brief Loads a player's data from the database.
     *
     * @param playerId The UUID of the player whose data is to be loaded.
     * @return The CustomPlayer object representing the player, or null if the player's data could not be loaded.
     */
    public CustomPlayer loadPlayer(UUID playerId) {
        CustomPlayer player = null;
        String selectSql = "SELECT user_friends FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(selectSql);

            pstmt.setString(1, playerId.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String friendListStr = rs.getString("user_friends");
                ArrayList<UUID> friendList = new ArrayList<>();

                try{
                    for (String friendIdStr : friendListStr.split(SEP_DELIM)) friendList.add(UUID.fromString(friendIdStr));
                } catch(Exception e) {}

                Player p = Bukkit.getPlayer(playerId);
                player = new CustomPlayer(p, friendList);
            } else {
                // Player not found in the database, so insert a new player
                String insertSql = "INSERT INTO users (id, user_friends) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);

                insertStmt.setString(1, playerId.toString());
                insertStmt.setString(2, "");  // Assuming no friends initially

                insertStmt.executeUpdate();

                Player p = Bukkit.getPlayer(playerId);
                player = new CustomPlayer(p, new ArrayList<>());  // Assuming no friends initially
            }

            rs.close();
            pstmt.closeOnCompletion();
        } catch (SQLException e) {
            logger.severe("Could not load player from database! UUID: " + playerId);
        }

        return player;
    }

    /**
     * @brief Saves a player's data to the database.
     *
     * @param player The CustomPlayer object representing the player whose data is to be saved.
     */
    public void savePlayer(CustomPlayer player) {
        String uuid = player.getPlayer().getUniqueId().toString();
        StringBuilder friendListBuilder = new StringBuilder();
        for(UUID friendUUID : player.getFriendList()) friendListBuilder.append(friendUUID.toString() + SEP_DELIM);

        String friendList = friendListBuilder.toString();
        if(friendList.endsWith(SEP_DELIM)) friendList = friendList.substring(0, friendList.length() - 1);

        String sql = "UPDATE users SET user_friends = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, friendList);
            pstmt.setString(2, uuid);
            pstmt.executeUpdate();

            pstmt.closeOnCompletion();
        } catch (SQLException e) {
            logger.severe("Could not update user in database! User: " + player.getPlayer().getName() + "\nUUID: " + uuid);
        }
    }

    /**
     * @brief Removes a friend from a player's friend list in the database.
     *
     * @param toUpdate The UUID of the player whose friend list is to be updated.
     * @param toRemove The UUID of the friend to be removed from the player's friend list.
     */
    public void savePlayerRemoveFriend(UUID toUpdate, UUID toRemove) {
        String getSQL = "SELECT user_friends FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(getSQL);

            pstmt.setString(1, toUpdate.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String friendListStr = rs.getString("user_friends");
                String toRemoveIdStr = toRemove.toString();

                friendListStr += SEP_DELIM;
                friendListStr = friendListStr.replace(toRemoveIdStr + SEP_DELIM, "");
                if (friendListStr.endsWith(SEP_DELIM)) {
                    friendListStr = friendListStr.substring(0, friendListStr.length() - 1);
                }

                // Update the friend list of the player
                String updateSQL = "UPDATE users SET user_friends = ? WHERE id = ?";
                pstmt = conn.prepareStatement(updateSQL);
                pstmt.setString(1, friendListStr);
                pstmt.setString(2, toUpdate.toString());
                pstmt.executeUpdate();
            }

            rs.close();
            pstmt.closeOnCompletion();
        } catch (SQLException e) {
            logger.severe("Could not do friend removal for player in database! UUID: " + toUpdate);
        }
    }

    /**
     * Adds a friend to a player's friend list in the database.
     *
     * @param toUpdate The UUID of the player whose friend list is to be updated.
     * @param toAdd The UUID of the friend to be added to the player's friend list.
     */
    public void savePlayerAddFriend(UUID toUpdate, UUID toAdd) {
        String getSQL = "SELECT user_friends FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(getSQL);

            pstmt.setString(1, toUpdate.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String friendListStr = rs.getString("user_friends");
                String toAddIdStr = toAdd.toString();

                // Add the id of the friend
                friendListStr += SEP_DELIM + toAddIdStr;

                // Update the friend list of the player
                String updateSQL = "UPDATE users SET user_friends = ? WHERE id = ?";
                pstmt = conn.prepareStatement(updateSQL);
                pstmt.setString(1, friendListStr);
                pstmt.setString(2, toUpdate.toString());
                pstmt.executeUpdate();
            }

            rs.close();
            pstmt.closeOnCompletion();
        } catch (SQLException e) {
            logger.severe("Could not add friend for player in database! UUID: " + toUpdate);
        }
    }

    /**
     * @brief Fills database with default paramaters.
     *
     * Structure is as folllows
     * "delimiter" is defined as SEP_DELIM in this class
     * users {
     *     id varchar // UUID of player
     *     user_friends varchar // player's friends' uuids separated delimeter
     * }
     *
     * @param conn Database Connection.
     */
    private void fillDefaultDB(Connection conn) {
        // no need for prepared statements here no user input
        try (Statement stmt = conn.createStatement()) {
            // Create the "users" table
            stmt.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "id TEXT PRIMARY KEY,"
                    + "user_friends TEXT"
                    + ");");

            logger.info("Database setup sucessfully.");
        } catch (SQLException exception) {
            logger.severe("Failed to initialize tables for database!");
        }
    }
}