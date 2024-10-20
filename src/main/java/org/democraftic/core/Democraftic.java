package org.democraftic.core;

import fr.xyness.SCS.SimpleClaimSystem;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;
import org.democraftic.core.commands.MinageTp;
import org.democraftic.core.commands.RegenerateWorld;
import org.democraftic.core.listener.ServerListener;
import org.democraftic.core.system.WorldSystem;

import java.io.File;

public final class Democraftic extends JavaPlugin {
    public static WorldSystem worldSystem;


    @Override
    public void onEnable() {

        registerConfig();
        registerSystems();


        registerCommand();


        registerListener();


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

    private void registerSystems() {
        worldSystem = new WorldSystem(this);
    }

    private void registerCommand(){
        getCommand("minage").setExecutor(new MinageTp(this));
        getCommand("regenMining").setExecutor(new RegenerateWorld(this));
    }

    private void registerListener() {
        getServer().getPluginManager().registerEvents(new ServerListener(this),this);
    }




}
