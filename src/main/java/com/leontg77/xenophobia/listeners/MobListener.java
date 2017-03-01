/*
 * Project: Xenophobia
 * Class: com.leontg77.xenophobia.listener.MobListener
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.xenophobia.listeners;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;

/**
 * Mob listener class.
 *
 * @author LeonTG77
 */
public class MobListener implements Listener {
    private final Disguise disguise;

    public MobListener(Disguise disguise) {
        this.disguise = disguise;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DisguiseAPI.disguiseToAll(player, disguise);
    }

    @EventHandler
    public void on(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        DisguiseAPI.disguiseToAll(entity, disguise);
    }

    @EventHandler
    public void on(ChunkLoadEvent event) {
        Arrays.stream(event.getChunk().getEntities())
                .filter(entity -> entity instanceof LivingEntity)
                .filter(living -> !DisguiseAPI.isDisguised(living) || DisguiseAPI.getDisguise(living).getType() != disguise.getType())
                .forEach(living -> DisguiseAPI.disguiseToAll(living, disguise));
    }

    @EventHandler
    public void on(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        ProjectileSource shooter = potion.getShooter();

        if (shooter instanceof Witch) {
            event.setCancelled(true);
        }
    }
}