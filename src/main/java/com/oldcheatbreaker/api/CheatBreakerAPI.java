package com.oldcheatbreaker.api;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.oldcheatbreaker.api.command.CheatBreakerBannedCommand;
import com.oldcheatbreaker.api.command.CheatBreakerCommand;
import com.oldcheatbreaker.api.handler.*;
import com.oldcheatbreaker.api.listener.AuthorizationListener;
import com.oldcheatbreaker.api.listener.ConnectionListener;
import com.oldcheatbreaker.api.listener.RegisterChannelListener;
import com.oldcheatbreaker.api.listener.UnregisterChannelListener;
import com.oldcheatbreaker.api.net.cheatbreaker.CBNetHandler;
import com.oldcheatbreaker.api.net.cheatbreaker.CBNetHandlerImpl;
import com.oldcheatbreaker.api.net.cheatbreaker.event.CBPacketReceivedEvent;
import com.oldcheatbreaker.api.net.cheatbreaker.event.CBPacketSentEvent;
import com.oldcheatbreaker.nethandler.CBPacket;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public final class CheatBreakerAPI extends JavaPlugin implements Listener {

    public Set<UUID> playersRunningCheatBreaker, playersNotRegistered = new HashSet<>();

    public final Map<UUID, List<CBPacket>> cbPacketQueue = new HashMap<>();

    @Setter private CBNetHandler netHandler = new CBNetHandlerImpl();

    private String messageChannel;

    private CooldownHandler cooldownHandler;
    private StaffModuleHandler staffModuleHandler;
    private TeammatesHandler teammatesHandler;
    private NotificationHandler notificationHandler;
    private VoiceChatHandler voiceChatHandler;
    private WorldHandler worldHandler;
    private ServerRuleHandler serverRuleHandler;

    private HologramHandler hologramHandler;
    private NametagHandler nametagHandler;
    private TitleHandler titleHandler;
    private WaypointHandler waypointHandler;

    public static CheatBreakerAPI getInstance() {
        return JavaPlugin.getPlugin(CheatBreakerAPI.class);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = getConfig();

        this.messageChannel = "CBRM-Client";

        this.cooldownHandler = new CooldownHandler(this);
        this.staffModuleHandler = new StaffModuleHandler(this);
        this.teammatesHandler = new TeammatesHandler(this);
        this.notificationHandler = new NotificationHandler(this);
        this.voiceChatHandler = new VoiceChatHandler(this);
        this.worldHandler = new WorldHandler(this);
        this.serverRuleHandler = new ServerRuleHandler(this);

        this.hologramHandler = new HologramHandler(this);
        this.nametagHandler = new NametagHandler(this);
        this.titleHandler = new TitleHandler(this);
        this.waypointHandler = new WaypointHandler(this);

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, messageChannel);
        messenger.registerIncomingPluginChannel(this, messageChannel, (channel, player, bytes) -> {
            CBPacket packet = CBPacket.handle(netHandler, bytes, player);
            CBPacketReceivedEvent event = new CBPacketReceivedEvent(player, packet);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled())
                packet.process(netHandler);
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
     * Check if a {@link UUID} is banned on CheatBreaker Reimagined
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

    public boolean sendPacket(Player player, CBPacket packet) {
        if (isRunningCheatBreaker(player)) {
            player.sendPluginMessage(this, messageChannel, CBPacket.getPacketData(packet));
            Bukkit.getPluginManager().callEvent(new CBPacketSentEvent(player, packet));
            return true;
        } else if (!playersNotRegistered.contains(player.getUniqueId())) {
            cbPacketQueue.putIfAbsent(player.getUniqueId(), new ArrayList<>());
            cbPacketQueue.get(player.getUniqueId()).add(packet);
            return false;
        }
        return false;
    }
}