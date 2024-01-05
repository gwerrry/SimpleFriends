package com.gwerry.utils;

import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;

import com.gwerry.CustomPlayer;

import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.server.level.EntityPlayer;

public class NameTagUtil {

    public static void hideNameTag(CustomPlayer player, CustomPlayer otherPlayer) {
        EntityPlayer entityPlayer = ((CraftPlayer) player.getPlayer()).getHandle();
        EntityPlayer entityTarget = ((CraftPlayer) otherPlayer.getPlayer()).getHandle();
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityTarget.getBukkitEntity().getEntityId());
        entityPlayer.c.a(packet);
    }
}