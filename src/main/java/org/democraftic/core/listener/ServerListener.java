package org.democraftic.core.listener;

import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.democraftic.core.Democraftic;

public class ServerListener implements Listener {

    private final Democraftic plugin;

    public ServerListener(Democraftic plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerLoadEvent(ServerLoadEvent event){
        Democraftic.worldSystem.loadWorld("World-Mining");
    }

}
