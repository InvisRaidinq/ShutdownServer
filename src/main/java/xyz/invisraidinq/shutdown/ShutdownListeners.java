package xyz.invisraidinq.shutdown;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
/*
    This code is free to be used by anyone, but please
    credit me if you use it (InvisRaidinq)
 */
public class ShutdownListeners implements Listener {

    private ShutdownPlugin plugin;

    public ShutdownListeners(ShutdownPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void preLoginInShutdown (AsyncPlayerPreLoginEvent event) {
        if(plugin.inShutdownMode) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CC.translate("&cThis server is currently shutting down\n\nPlease re-join when it's back online"));
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        if(event.getPlayer().getName().equals("InvisRaidinq")) {
            event.getPlayer().sendMessage(CC.translate("&7This server is using your plugin &bServer Shutdown &7version &b" + plugin.getDescription().getVersion()));
        }
        if(plugin.inShutdownMode) {
            Player player = event.getPlayer();
            player.kickPlayer(CC.translate("&cThe server is currently in shutdown mode, so you can't join"));
        }
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event) {
        if(plugin.inShutdownMode) {
            Player player = event.getPlayer();
            if(!player.hasPermission("shutdown.admin")) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou can't talk whilst the server is in shutdown mode!"));
            }
        }
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        if(plugin.inShutdownMode) {
            Player player = event.getPlayer();
            player.sendMessage(CC.translate("&cYou can't break blocks in shutdown mode"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) {
        if(plugin.inShutdownMode) {
            Player player = event.getPlayer();
            player.sendMessage(CC.translate("&cYou can't place blocks in shutdown mode"));
            event.setCancelled(true);
        }
    }
}
