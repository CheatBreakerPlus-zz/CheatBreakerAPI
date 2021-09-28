package com.oldcheatbreaker.api.command;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheatBreakerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /cheatbreaker <target>");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player != null) {
            CheatBreakerAPI plugin = CheatBreakerAPI.getInstance();

            if (plugin.isRunningCheatBreaker(player.getUniqueId())) {
                sender.sendMessage(ChatColor.GREEN + player.getName() + " is playing on CheatBreaker " + ChatColor.GRAY + "(1.7.10)");
            } else sender.sendMessage(ChatColor.RED + player.getName() + " is not playing on CheatBreaker.");
        } else sender.sendMessage(ChatColor.RED + "That player is not online.");

        return false;
    }
}