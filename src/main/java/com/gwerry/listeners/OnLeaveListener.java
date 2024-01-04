/**
 * @file OnLeaveListener.java
 * @author gwerry
 * @brief The OnLeaveListener class handles the event when a player leaves the server.
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

import java.util.UUID;

import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gwerry.CustomPlayer;
import com.gwerry.Messages;
import com.gwerry.PlayerManager;

/**
 * @brief The OnLeaveListener class handles the event when a player leaves the server.
 * It implements the Listener interface, which means it can register events.
 * When a player leaves, it sends a message to all of the leaving player's friends notifying them of the player's departure.
 *
 * @author gwerry
 * @since 1.0
 */
public class OnLeaveListener implements Listener {

    /**
     * This method is called whenever a player leaves the server.
     * It first retrieves the CustomPlayer object associated with the leaving player.
     * Then, it iterates over the leaving player's friend list, and for each friend that is online,
     * it sends them a message notifying them that the player has left the server.
     * Finally, it removes the player from the PlayerManager.
     *
     * @param event The PlayerQuitEvent object containing information about the leaving player.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        CustomPlayer player = PlayerManager.getPlayer(p.getUniqueId());
        String name = p.getName();
        for(UUID uuid : player.getFriendList()) {
            CustomPlayer other = PlayerManager.getPlayer(uuid);
            if(other != null) other.getPlayer().sendMessage(Messages.FRIEND_LEAVE.replace("%friend_name%", name));
        }

        PlayerManager.removePlayer(p.getUniqueId());
    }
}