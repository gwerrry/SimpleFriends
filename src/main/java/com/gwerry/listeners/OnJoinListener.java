/**
 * @file OnJoinListener.java
 * @author gwerry
 * @brief The OnJoinListener class handles the event when a player joins the server.
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
package com.gwerry.listeners;

import com.gwerry.CustomPlayer;
import com.gwerry.Data;
import com.gwerry.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * @brief The OnJoinListener class handles the event when a player joins the server.
 * It implements the Listener interface, which means it can register events.
 * When a player joins, it sends a message to all of the joining player's friends notifying them of the player's arrival.
 *
 * @author gwerry
 * @since 1.0
 */
public class OnJoinListener implements Listener {

    /**
     * @brief This method is called whenever a player joins the server.
     * It first retrieves the CustomPlayer object associated with the joining player.
     * Then, it iterates over the joining player's friend list, and for each friend that is online,
     * it sends them a message notifying them that the player has joined the server.
     *
     * @param event The PlayerJoinEvent object containing information about the joining player.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        CustomPlayer player = PlayerManager.addPlayer(p.getUniqueId());
        String name = p.getName();

        for(UUID uuid : player.getFriendList()) {
            CustomPlayer other = PlayerManager.getPlayer(uuid);
            if(other != null) other.getPlayer().sendMessage(Data.FRIEND_JOIN.replace("%friend_name%", name));
        }
    }
}