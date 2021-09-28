package com.oldcheatbreaker.api.command;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheatBreakerBannedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /isbanned <target>");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Reading banlist, this may take a while.");

        Player target = Bukkit.getServer().getPlayer(args[0]);
        Tuple<UUID, String> uuidStringTuple;

        if (target == null) {
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "That player has not played before.");
                return true;
            }
            uuidStringTuple = new Tuple<>(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        } else uuidStringTuple = new Tuple<>(target.getUniqueId(), target.getName());


        try {
            CheatBreakerAPI.getInstance().isCheatBreakerBanned(uuidStringTuple.getKey(), value ->
                    sender.sendMessage((value ? ChatColor.RED : ChatColor.GREEN) + uuidStringTuple.getValue() + " is" + (value ? "" : " not") + " currently CheatBreaker+ banned."));
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "The bans API seems to be down, try again later!");
        }

        return false;

    }
}