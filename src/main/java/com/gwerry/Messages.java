package com.gwerry;

public class Messages {
    public static final String ONLY_PLAYER_COMMAND = "§cThat command can not be executed by console.";
    public static final String NO_PERMISSION = "§cNo permission. :(";
    public static final String PLAYER_NOT_ONLINE = "§cThat player is not currently online.";
    public static final String NOT_EVEN_POSSIBLE = "§4This LITERALLY should not be possible.";

    public static final String FRIEND_REQUEST_RECIEVER = "§aFriend request from §b%sender_name%§a. Type §b\"/friend accept/deny name\"§a.\nThis request expires in 120 seconds.";
    public static final String FRIEND_REQUEST_SENDER = "§aSent §b%reciever_name%§a a friend request.";

    public static final String FRIEND_REQUEST_ACCEPT = "§aYou are now friends with §b%sender_name%§a!";
    public static final String FRIEND_REMOVE = "§cYou are no longer friends with §b%reciever_name%§c!";
    public static final String ALREADY_FRIENDS = "§aYou are already friends with §b%reciever_name%§a!";

    public static final String FRIEND_LIST = "§a~~~Friends~~~\n%friends%";
    public static final String FRIEND_LIST_SINGLE = "§a> §b%friend_name%§a | %online_status%";

    public static final String CANT_FRIEND_SELF = "§cYou can't friend yourself.";
}