package dev.oddbyte;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeCommandSender extends JavaPlugin implements CommandExecutor, PluginMessageListener {

    @Override
    public void onEnable() {
        this.getCommand("sendbungeecommand").setExecutor(this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "oddbytecommandsender:bungeecordchannel");
        getLogger().info("BungeeCommandSender enabled and channel registered.");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getLogger().info("BungeeCommandSender disabled and channel unregistered.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sendbungeecommand")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    String commandToSend = String.join(" ", args);
                    sendBungeeCommand(player, commandToSend);
                    //player.sendMessage("Command sent to BungeeCord: " + commandToSend);
                    return true;
                } else {
                    player.sendMessage("Usage: /sendbungeecommand <command>");
                    return false;
                }
            } else {
                sender.sendMessage("This command can only be run by a player.");
                return false;
            }
        }
        return false;
    }

    private void sendBungeeCommand(Player player, String command) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        try {
            out.writeUTF(player.getName() + ":" + command);
            getLogger().info("Sending command '" + command + "' for player " + player.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(this, "oddbytecommandsender:bungeecordchannel", byteArray.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // Handle any incoming messages if necessary
    }
}
