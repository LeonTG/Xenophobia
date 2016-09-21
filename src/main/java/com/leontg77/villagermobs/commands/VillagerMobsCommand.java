/*
 * Project: VillagerMobs
 * Class: com.leontg77.villagermobs.commands.VillagerMobsCommand
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

package com.leontg77.villagermobs.commands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Lists;
import com.leontg77.villagermobs.Main;
import com.leontg77.villagermobs.protocol.MobDisguiseAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Villager Mobs command class.
 *
 * @author LeonTG77
 */
public class VillagerMobsCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "villagermobs.manage";

    private final MobDisguiseAdapter disguise;
    private final Main plugin;

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public VillagerMobsCommand(Main plugin, MobDisguiseAdapter disguise) {
        this.disguise = disguise;
        this.plugin = plugin;
    }

    private boolean enabled = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /villagermobs <info|enable|disable>");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG77");
            sender.sendMessage(Main.PREFIX + "Version: §a" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (enabled) {
                sender.sendMessage(Main.PREFIX + "Villager Mobs is already enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Villager Mobs has been enabled.");
            enabled = true;

            manager.addPacketListener(disguise);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "Villager Mobs is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Villager Mobs has been disabled.");
            enabled = false;

            manager.removePacketListener(disguise);
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /villagermobs <info|enable|disable>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = Lists.newArrayList();
        List<String> list = Lists.newArrayList();

        if (args.length != 1) {
            return toReturn;
        }

        list.add("info");

        if (sender.hasPermission(PERMISSION)) {
            list.add("enable");
            list.add("disable");
        }

        // make sure to only tab complete what starts with what they
        // typed or everything if they didn't type anything
        toReturn.addAll(list
                .stream()
                .filter(str -> args[args.length - 1].isEmpty() || str.startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList()));

        return toReturn;
    }
}