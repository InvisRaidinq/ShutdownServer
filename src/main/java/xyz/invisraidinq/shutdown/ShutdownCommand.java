package xyz.invisraidinq.shutdown;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
/*
    This code is free to be used by anyone, but please
    credit me if you use it (InvisRaidinq)
 */
public class ShutdownCommand implements CommandExecutor {

    private ShutdownPlugin plugin;
    List<Player> players = new ArrayList<>();

    public ShutdownCommand(ShutdownPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("shutdown").setExecutor(this);
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("shutdown")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if(player.hasPermission("shutdown.admin")) {
                    if(!plugin.inShutdownMode) {
                        player.sendMessage(CC.translate("&cThe server is now in shutdown mode. This cannot be halted"));
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(CC.translate("&CA server shutdown has started. Please leave this server"));
                        Bukkit.broadcastMessage("");
                        startShutdown();
                    } else {
                        player.sendMessage(CC.translate("&cThe server is already in shutdown mode"));
                        return false;
                    }
                } else {
                    player.sendMessage(CC.translate("&cNo Permission"));
                    return false;
                }
            } else {
                sender.sendMessage(CC.translate("&cYou're the console, why not just press stop..."));
                return false;
            }
        }
        return false;
    }

    private void startShutdown() {
        plugin.inShutdownMode = true;
        kickToHubs();
        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(plugin.shutdownTime != -1) {
                    if(plugin.shutdownTime != 0) {
                        Bukkit.broadcastMessage(CC.translate("&CThis server will close in " + plugin.shutdownTime));
                        plugin.shutdownTime--;
                    } else {
                        plugin.shutdownTime--;
                        plugin.getServer().shutdown();
                    }
                }
            }
        }, 0L, 20L);
    }

    private void kickToHubs() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(player);
        }
        new BukkitRunnable() {
            public void run() {
                Player player = players.get(0);
                if(player != null && player.isOnline()) {
                    sendPlayerToHub(player);
                }
                players.remove(player);
                if(players.size() == 0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void sendPlayerToHub(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(plugin.hubName);
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }
}
