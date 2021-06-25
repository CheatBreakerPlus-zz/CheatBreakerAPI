package com.cheatbreaker.api.command;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.api.util.Tuple;
import org.bukkit.*;
import org.bukkit.command.*;
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


        CheatBreakerAPI.getInstance().isCheatBreakerBanned(uuidStringTuple.getKey(), value ->
                sender.sendMessage((value ? ChatColor.RED : ChatColor.GREEN) + uuidStringTuple.getValue() + " is" + (value ? "" : " not") + " currently CheatBreaker banned."));

        return false;

    }
}