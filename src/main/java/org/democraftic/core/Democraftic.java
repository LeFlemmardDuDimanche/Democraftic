package org.democraftic.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.democraftic.core.commands.SetSpawn;
import org.democraftic.core.commands.Spawn;
import org.democraftic.core.system.teleportation.TeleportationSystem;

import java.io.File;

public final class Democraftic extends JavaPlugin {

    public static TeleportationSystem teleportationSystem;
    public static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        teleportationSystem = new TeleportationSystem(this);
        protocolManager = ProtocolLibrary.getProtocolManager();


        getCommand("spawn").setExecutor(new Spawn(this));
        getCommand("setspawn").setExecutor(new SetSpawn(this));

        registerConfig();
    }

    @Override
    public void onDisable() {


    }

    public void registerConfig(){
        File config = new File(this.getDataFolder(), "config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

}
