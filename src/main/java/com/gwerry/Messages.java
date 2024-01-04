/**
 * @file Messages.java
 * @author gwerry
 * @brief The Messages class contains a bunch of static String that represent various messages used in the plugin.
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

/**
 * @brief The Messages class contains a bunch of static String that represent various messages used in the plugin.
 * These messages are used to communicate with the player in different situations such as when a player joins or leaves,
 * sends a friend invite, accepts a friend invite, removes a friend, and more.
 * The messages are color-coded using Minecraft's color codes, which are represented by the "§" character followed by a hexadecimal digit.
 *
 * @author gwerry
 * @since 1.0
 */
public class Messages {
    public static String FRIEND_CMD = "friend";
    public static ArrayList<String> FRIEND_CMD_ALIASES = new ArrayList<>();
    public static String ONLY_PLAYER_COMMAND = "§cThat command can not be executed by console.";
    public static String NO_PERMISSION = "§cNo permission. :(";
    public static String PLAYER_NOT_ONLINE = "§cThat player is not currently online.";
    public static String NOT_EVEN_POSSIBLE = "§4This LITERALLY should not be possible.";
    public static String FRIEND_CMD_DESCRIPTION = "Simple friends command. Use \"/f help\" for a list of commands.";
    public static String FRIEND_CMD_USAGE = "For usage info, use \"/f help\" for a list of commands.";
    public static String FRIEND_NOT_FOUND = "§cYou are not friends with that player.";
    public static String PLAYER_NOT_EXISTS = "§cThat player does not exist.";

    public static String FRIEND_REQUEST_RECIEVER = "§aFriend invite from §b%sender_name%§a. Type §b\"/friend accept/deny <name>\"§a.\nThis invite expires in 120 seconds.";
    public static String FRIEND_REQUEST_SENDER = "§aSent §b%reciever_name%§a a friend invite.";

    public static String FRIEND_REQUEST_ACCEPT = "§aYou are now friends with §b%sender_name%§a!";
    public static String FRIEND_REMOVE = "§cYou are no longer friends with §b%reciever_name%§c!";
    public static String ALREADY_FRIENDS = "§aYou are already friends with §b%reciever_name%§a!";
    public static String FRIEND_REQUEST_ALREADY_SENT = "§aYou already sent §b%reciever_name%§a a friend request.";
    public static String FRIEND_REQUEST_ALREADY_RECIEVED = "§b%reciever_name%§a already sent you a friend request. Use §b\"/friend accept/deny <name>\" §ato accept or deny.";
    public static String FRIEND_REQUEST_NOT_EXIST = "§cYou have no friend request from §b%reciever_name%§c.";
    public static String FRIEND_REQUEST_DENY_SENDER = "§cRejected friend invite from §b%reciever_name%§c.";
    public static String FRIEND_REQUEST_DENY_RECEIVER = "§cYour friend invite to §b%sender_name% §cwas rejected.";

    public static String FRIEND_LIST = "§a>          §aFriends          §a<\n%friends%";
    public static String FRIEND_LIST_SINGLE = "§a> §b%friend_name%§a | %online_status%";
    public static String ONLINE_TEXT = "§aOnline";
    public static String OFFLINE_TEXT = "§cOffline";

    public static String CANT_FRIEND_SELF = "§cYou can't friend yourself.";
    public static String FRIEND_JOIN = "§aYour friend §b%friend_name% §ajoined.";
    public static String FRIEND_LEAVE = "§cYour friend §b%friend_name% §cleft.";

    public static String FRIEND_HELP_TEXT =
    "§a>          §3SimpleFriends Commands        §a<\n" +
    "§a> §b/friend help - Displays this help text.\n" +
    "§a> §b/friend invite <player> - Sends a friend invite to the specified player. Only works if they are online.\n" +
    "§a> §b/friend kick <player> - Removes the specified player from your friends list.\n" +
    "§a> §b/friend list - Lists your friends\n" +
    "§a>  You can also use /f instead of /friend  <";
}