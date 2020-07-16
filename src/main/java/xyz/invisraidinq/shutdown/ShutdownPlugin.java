package xyz.invisraidinq.shutdown;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
/*
    This code is free to be used by anyone, but please
    credit me if you use it (InvisRaidinq)
 */
public class ShutdownPlugin extends JavaPlugin {

    public int shutdownTime = 30;
    public boolean inShutdownMode = false;
    public String hubName = "Hub-1";

    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new ShutdownListeners(this);
        new ShutdownCommand(this);
        Bukkit.getConsoleSender().sendMessage(CC.translate("&6ShutdownServer &7version" + getDescription().getVersion() + " has been &aenabled"));
    }
}
