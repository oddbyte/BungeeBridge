package oddbyte.bungeecommandexecutor;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;

public class BungeeCommandExecutor extends Plugin implements Listener {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().registerChannel("oddbytecommandsender:bungeecordchannel");
        //getLogger().info("BungeeCommandExecutor enabled and channel registered.");
    }

    @Override
    public void onDisable() {
        getProxy().unregisterChannel("oddbytecommandsender:bungeecordchannel");
        //getLogger().info("BungeeCommandExecutor disabled and channel unregistered.");
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("oddbytecommandsender:bungeecordchannel")) {
            return;
        }

        byte[] data = event.getData();
        String message = new String(data).trim();
        //getLogger().info("Received plugin message: " + message);
        String[] parts = message.split(":", 2);

        if (parts.length != 2) {
            getLogger().warning("Invalid message format.");
            return;
        }

        String playerName = parts[0].trim();
        String command = parts[1].trim();

        //getLogger().info("Searching for player: " + playerName);
        //getLogger().info("Player name bytes: " + bytesToHex(playerName.getBytes()));

        ProxiedPlayer player = getProxy().getPlayer(playerName);

        // Debug: List all connected players
        Collection<ProxiedPlayer> players = getProxy().getPlayers();
        //getLogger().info("Connected players:");
        for (ProxiedPlayer p : players) {
            //getLogger().info("- " + p.getName() + " (bytes: " + bytesToHex(p.getName().getBytes()) + ")");
        }

        if (player != null) {
            //getLogger().info("Dispatching command '" + command + "' for player " + playerName);
            getProxy().getPluginManager().dispatchCommand(player, command);
        } else {
            getLogger().warning("Player " + playerName + " not found.");
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
