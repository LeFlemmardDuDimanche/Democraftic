package org.democraftic.core.system.camera;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.democraftic.core.system.camera.cinematic.Cinematic;
import org.democraftic.core.system.camera.cinematic.CinematicChapter;
import org.democraftic.core.system.camera.cinematic.CinematicScript;

public class CameraManager {

    private final ProtocolManager protocolManager;
    private final Plugin plugin;

    public CameraManager(ProtocolManager protocolManager,Plugin plugin){
        this.protocolManager = protocolManager;
        this.plugin = plugin;
    }

    public void startCinematic(Cinematic cinematic){
        ArmorStand armorStand = (ArmorStand) cinematic.getPlayer().getWorld().spawnEntity(cinematic.getFirstLocation(), EntityType.ARMOR_STAND);
        cinematic.setBodyCam(new CameraBody(armorStand,cinematic.getPlayer()));

        //Start cinematic

        continueCinematic(cinematic);


    }

    private void continueCinematic(Cinematic cinematic){
        CinematicChapter chapter = cinematic.getNextChapter();

        chapter.onChapterStart(cinematic);

        BukkitRunnable runChapter = new BukkitRunnable() {
            @Override
            public void run() {
                chapter.onChapterEnd(cinematic);

                if(!cinematic.isLastChapter()){
                    continueCinematic(cinematic);
                }else{
                    this.cancel();
                }
                this.cancel();
            }
        };
        runChapter.runTaskLater(plugin, chapter.chapterTime);
    }



}
