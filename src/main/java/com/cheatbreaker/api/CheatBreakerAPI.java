/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cheatbreaker.api;

import com.cheatbreaker.api.command.CheatBreakerBannedCommand;
import com.cheatbreaker.api.command.CheatBreakerCommand;
import com.cheatbreaker.api.handler.*;
import com.cheatbreaker.api.listener.*;
import com.cheatbreaker.api.net.*;
import com.cheatbreaker.api.net.event.*;
import com.cheatbreaker.nethandler.CBPacket;
import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public final class CheatBreakerAPI extends JavaPlugin implements Listener {

    @Getter
    private String messageChannel;

    public final Set<UUID> playersRunningCheatBreaker = new HashSet<>();
    public final Set<UUID> playersNotRegistered = new HashSet<>();

    @Setter
    private CBNetHandler netHandlerServer = new CBNetHandlerImpl();
    public final Map<UUID, List<CBPacket>> packetQueue = new HashMap<>();

    private CooldownHandler cooldownHandler;
    private HologramHandler hologramHandler;
    private NametagHandler nametagHandler;
    private NotificationHandler notificationHandler;
    private ServerRuleHandler serverRuleHandler;
    private StaffModuleHandler staffModuleHandler;
    private TeammatesHandler teammatesHandler;
    private TitleHandler titleHandler;
    private VoiceChatHandler voiceChatHandler;
    private WaypointHandler waypointHandler;
    private WorldHandler worldHandler;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = getConfig();

        this.messageChannel = config.getString("message-channel", "CB-Client");
        this.cooldownHandler = new CooldownHandler(this);
        this.hologramHandler = new HologramHandler(this);
        this.nametagHandler = new NametagHandler(this);
        this.notificationHandler = new NotificationHandler(this);
        this.serverRuleHandler = new ServerRuleHandler(this);
        this.staffModuleHandler = new StaffModuleHandler(this);
        this.teammatesHandler = new TeammatesHandler(this);
        this.titleHandler = new TitleHandler(this);
        this.voiceChatHandler = new VoiceChatHandler(this);
        this.waypointHandler = new WaypointHandler(this);
        this.worldHandler = new WorldHandler(this);

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, messageChannel);
        messenger.registerIncomingPluginChannel(this, messageChannel, (channel, player, bytes) -> {
            CBPacket packet = CBPacket.handle(netHandlerServer, bytes, player);
            CBPacketReceivedEvent event = new CBPacketReceivedEvent(player, packet);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled())
                packet.process(netHandlerServer);
        });

        Bukkit.getPluginManager().registerEvents(new RegisterChannelListener(this), this);
        Bukkit.getPluginManager().registerEvents(new UnregisterChannelListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AuthorizationListener(this), this);

        if (config.getBoolean("commands.isbanned.enabled", true))
            getCommand("isbanned").setExecutor(new CheatBreakerBannedCommand());

        if (config.getBoolean("commands.cheatbreaker.enabled", true))
            getCommand("cheatbreaker").setExecutor(new CheatBreakerCommand());
    }

    /**
     * Check if a player is running CheatBreaker
     *
     * @param player The {@link Player}
     * @return If the player is running CheatBreaker
     */
    public boolean isRunningCheatBreaker(Player player) {
        return isRunningCheatBreaker(player.getUniqueId());
    }

    /**
     * Check if a player is running CheatBreaker
     *
     * @param playerUuid The {@link UUID} of the {@link Player}
     * @return If the player is running CheatBreaker
     */
    public boolean isRunningCheatBreaker(UUID playerUuid) {
        return playersRunningCheatBreaker.contains(playerUuid);
    }

    /**
     * @return List of {@link Player}'s that are running CheatBreaker
     */
    public Set<Player> getPlayersRunningCheatBreaker() {
        return ImmutableSet.copyOf(playersRunningCheatBreaker
                .stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet())
        );
    }

    /**
     * Get the protocol version of a {@link Player}
     *
     * @param player The {@link Player}
     * @return The version that the user is on as a {@link String}
     */
    public String getVersion(Player player) {
        switch (ProtocolLibrary.getProtocolManager().getProtocolVersion(player)) {
            case 5:
                return "1.7";
            case 47:
                return "1.8";
            default:
                return "N/A";
        }
    }

    /**
     * Check if a {@link UUID} is banned on CheatBreaker
     *
     * @param playerUuid     The {@link UUID} of the {@link Player}
     * @param resultListener A {@link Consumer<Boolean>} that returns if the {@link UUID} is banned
     */
    public void isCheatBreakerBanned(UUID playerUuid, Consumer<Boolean> resultListener) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String apiHost = getConfig().getString("api.host", "api.cheatbreaker.com");
            String apiKey = getConfig().getString("api.key", "e14437c2-148b-47ce-8594-06737aa6b747");
            String completeUrl = "http://" + apiHost + "/api/" + apiKey + "/uuid/" + playerUuid.toString() + "/banned";
            try {
                URL link = new URL(completeUrl);
                URLConnection request = link.openConnection();
                request.connect();

                JsonElement root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
                if (root == null) {
                    resultListener.accept(false);
                    return;
                }
                JsonElement banned = root.getAsJsonObject().get("banned");
                if (banned.isJsonNull()) {
                    resultListener.accept(false);
                    return;
                }
                resultListener.accept(banned.getAsBoolean());
            } catch (Exception exception) {
                System.err.println("[CheatBreakerAPI] Error whilst executing FETCH ban task for " + playerUuid);
                System.err.println("  Host: " + apiHost + "(Complete URL: " + completeUrl + ")");
                System.err.println("  Key: " + apiKey);
                System.err.println("  Message: " + exception.getMessage());
            }
        });
    }

    /*
     *  This is a boolean to indicate whether or not a CB message was sent.
     *  An example use-case is when you want to send a CheatBreaker
     *  notification if a player is running CheatBreaker, and a chat
     *  message if not.
     */
    public boolean sendPacket(Player player, CBPacket packet) {
        if (isRunningCheatBreaker(player)) {
            player.sendPluginMessage(this, messageChannel, CBPacket.getPacketData(packet));
            Bukkit.getPluginManager().callEvent(new CBPacketSentEvent(player, packet));
            return true;
        } else if (!playersNotRegistered.contains(player.getUniqueId())) {
            packetQueue.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            packetQueue.get(player.getUniqueId()).add(packet);
            return false;
        }
        return false;
    }

    public static CheatBreakerAPI getInstance() {
        return JavaPlugin.getPlugin(CheatBreakerAPI.class);
    }
}