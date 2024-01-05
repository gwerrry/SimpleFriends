/**
 * @file Data.java
 * @author gwerry
 * @brief The Data class contains a bunch of static values that the user can change in the config.yml.
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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.gwerry.io.IYamlConfig;

/**
 * @brief The Data class contains a bunch of static values that the user can change in the config.yml.
 * These messages are used to communicate with the player in different situations such as when a player joins or leaves,
 * sends a friend invite, accepts a friend invite, removes a friend, and more.
 * The messages are color-coded using Minecraft's color codes, which are represented by the "§" character followed by a hexadecimal digit.
 *
 * @author gwerry
 * @since 1.0
 */
public class Data {
    public static String FRIEND_CMD;
    public static String FRIEND_CMD_DESCRIPTION;
    public static String FRIEND_CMD_USAGE;
    public static List<String> FRIEND_CMD_ALIASES;

    public static String ONLY_PLAYER_COMMAND;
    public static String NO_PERMISSION;
    public static String PLAYER_NOT_ONLINE;
    public static String NOT_EVEN_POSSIBLE;

    public static String FRIEND_NOT_FOUND;
    public static String PLAYER_NOT_EXISTS;

    public static String FRIEND_REMOVE;
    public static String ALREADY_FRIENDS;

    public static String FRIEND_REQUEST_RECIEVER;
    public static String FRIEND_REQUEST_SENDER;
    public static String FRIEND_REQUEST_ACCEPT;
    public static String FRIEND_REQUEST_ALREADY_SENT;
    public static String FRIEND_REQUEST_ALREADY_RECIEVED;
    public static String FRIEND_REQUEST_NOT_EXIST;
    public static String FRIEND_REQUEST_DENY_SENDER;
    public static String FRIEND_REQUEST_DENY_RECEIVER;

    public static String FRIEND_LIST;
    public static String FRIEND_LIST_SINGLE;
    public static String ONLINE_TEXT;
    public static String OFFLINE_TEXT;

    public static String CANT_FRIEND_SELF;
    public static String FRIEND_JOIN;
    public static String FRIEND_LEAVE;

    public static String FRIEND_HELP_TEXT;

    public static int DB_MAX_CONNECTIONS;
    public static int DB_INITIAL_CONNECTIONS;
    public static int DB_MAX_IDLE_CONNECTIONS;
    public static int DB_MIN_IDLE_CONNECTIONS;

    /**
     * @brief Loads data from the config file.
     * @param config Config file to load data from.
     */
    public static void loadFromFile(IYamlConfig config) {
        YamlConfiguration conf = config.getConfig();
        FRIEND_CMD = conf.getString("friend_cmd.COMMAND");
        FRIEND_CMD_DESCRIPTION = conf.getString("friend_cmd.DESCRIPTION");
        FRIEND_CMD_USAGE = conf.getString("friend_cmd.USAGE");
        FRIEND_CMD_ALIASES = conf.getStringList("friend_cmd.FRIEND_CMD_ALIASES");

        ONLY_PLAYER_COMMAND = conf.getString("messages.ONLY_PLAYER_COMMAND");
        NO_PERMISSION = conf.getString("messages.NO_PERMISSION");
        PLAYER_NOT_ONLINE = conf.getString("messages.PLAYER_NOT_ONLINE");
        NOT_EVEN_POSSIBLE = conf.getString("messages.NOT_EVEN_POSSIBLE");
        FRIEND_NOT_FOUND = conf.getString("messages.FRIEND_NOT_FOUND");
        PLAYER_NOT_EXISTS = conf.getString("messages.PLAYER_NOT_EXISTS");
        FRIEND_REQUEST_RECIEVER = conf.getString("messages.FRIEND_REQUEST_RECIEVER");
        FRIEND_REQUEST_SENDER = conf.getString("messages.FRIEND_REQUEST_SENDER");
        FRIEND_REQUEST_ACCEPT = conf.getString("messages.FRIEND_REQUEST_ACCEPT");
        FRIEND_REMOVE = conf.getString("messages.FRIEND_REMOVE");
        ALREADY_FRIENDS = conf.getString("messages.ALREADY_FRIENDS");
        FRIEND_REQUEST_ALREADY_SENT = conf.getString("messages.FRIEND_REQUEST_ALREADY_SENT");
        FRIEND_REQUEST_ALREADY_RECIEVED = conf.getString("messages.FRIEND_REQUEST_ALREADY_RECIEVED");
        FRIEND_REQUEST_NOT_EXIST = conf.getString("messages.FRIEND_REQUEST_NOT_EXIST");
        FRIEND_REQUEST_DENY_SENDER = conf.getString("messages.FRIEND_REQUEST_DENY_SENDER");
        FRIEND_REQUEST_DENY_RECEIVER = conf.getString("messages.FRIEND_REQUEST_DENY_RECEIVER");
        FRIEND_LIST = conf.getString("messages.FRIEND_LIST");
        FRIEND_LIST_SINGLE = conf.getString("messages.FRIEND_LIST_SINGLE");
        ONLINE_TEXT = conf.getString("messages.ONLINE_TEXT");
        OFFLINE_TEXT = conf.getString("messages.OFFLINE_TEXT");
        CANT_FRIEND_SELF = conf.getString("messages.CANT_FRIEND_SELF");
        FRIEND_JOIN = conf.getString("messages.FRIEND_JOIN");
        FRIEND_LEAVE = conf.getString("messages.FRIEND_LEAVE");
        FRIEND_HELP_TEXT = conf.getString("messages.FRIEND_HELP_TEXT");

        DB_MAX_CONNECTIONS = conf.getInt("database.max_connections");
        DB_INITIAL_CONNECTIONS = conf.getInt("database.initial_connections");
        DB_MAX_IDLE_CONNECTIONS = conf.getInt("database.max_idle_connections");
        DB_MIN_IDLE_CONNECTIONS = conf.getInt("min_idle_connections");
    }
}