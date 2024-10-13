package org.democraftic.core.system.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.democraftic.core.Democraftic;
import org.democraftic.core.system.camera.CameraManager;
import org.democraftic.core.system.camera.cinematic.Cinematic;
import org.democraftic.core.system.camera.cinematic.CinematicChapter;
import org.democraftic.core.system.camera.cinematic.CinematicScript;
import org.democraftic.core.system.npc.NPC;

import java.util.HashMap;
import java.util.UUID;

public final class TeleportationSystem {
    public final Plugin plugin;
    public final CameraManager cameraManager;

    public static final int waitTime = 3;

    public HashMap<String,TeleportationInfo> teleportInfos = new HashMap<>();


    public TeleportationSystem(Plugin plugin){
        this.plugin = plugin;
        this.cameraManager = new CameraManager(Democraftic.protocolManager,plugin);

    }

    public void CancelTeleport(Player player){
        if(isPlayerInTeleportation(player)){
            TeleportationInfo tpInfo = getTeleportationInfo(player);
            tpInfo.countdownTask.cancel();
            teleportInfos.remove(player.getName());
        }
    }



    public void WaitTeleportPlayer(Player player,Location destination){
        if(isPlayerInTeleportation(player)){
            if(canPlayerTeleport(player)) {
                CancelTeleport(player);
            }else{
                return;
            }
        }

        TeleportationInfo tpInfo = new TeleportationInfo(destination,System.currentTimeMillis(),getCountDownTask(player,destination));

        teleportInfos.put(player.getName(),tpInfo);
        tpInfo.countdownTask.runTaskTimer(plugin,0,20);

    }

    public void TeleportPlayer(Player player, Location destination){
        Location playerPosition = player.getLocation().clone();

        if(playerPosition.getWorld().getName().equals(destination.getWorld().getName())){
            TeleportAnimation(player,destination);
        }


    }

    public void TeleportAnimation(Player player,Location destination){
        NPC npc = new NPC(
                Democraftic.protocolManager,
                UUID.randomUUID());

        Location playerPosition = player.getLocation().clone();

        Location firstAirPosition = new Location(playerPosition.getWorld(),playerPosition.getX(),
                playerPosition.getY()+20,playerPosition.getZ(),0,90);

        Location secondAirPosition = new Location(destination.getWorld(),destination.getX(),
                destination.getY()+20,destination.getZ(),0,90);

        npc.spawn(player, playerPosition);

        CinematicScript script = getTeleportCinematic(firstAirPosition,npc,secondAirPosition,destination);

        Cinematic cinematic = new Cinematic(script,player);

        cameraManager.startCinematic(cinematic);

    }

    private void endTeleport(Player player){
        teleportInfos.remove(player.getName());
    }




    //region Utils methods
    //
    //endregion
    private CinematicScript getTeleportCinematic(Location p1,NPC npc,Location p2,Location destination){
        CinematicChapter chapterOne = new CinematicChapter(p1,30);
        CinematicChapter chapterTwo = new CinematicChapter(p2,30){
            @Override
            public void onChapterStart(Cinematic cinematic) {
                super.onChapterStart(cinematic);
                npc.moveTo(cinematic.getPlayer(),destination);
            }

            @Override
            public void onChapterEnd(Cinematic cinematic) {
                super.onChapterEnd(cinematic);
                cinematic.getCamera().unlockCamera();
                npc.remove(cinematic.getPlayer());
                cinematic.getPlayer().teleport(destination);
                cinematic.getPlayer().setInvisible(false);
                endTeleport(cinematic.getPlayer());
            }
        };

        CinematicScript script = new CinematicScript();
        script.add(chapterOne);
        script.add(chapterTwo);

        return script;
    }


    private BukkitRunnable getCountDownTask(Player player,Location destination){

        final int[] countdown = {waitTime};
        BukkitRunnable countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(countdown[0] > 0){

                    player.sendTitle("Téleportation dans : "+countdown[0]+"s.","",0,20,0);

                    countdown[0]--;
                } else {
                    TeleportPlayer(player,destination);
                    this.cancel();
                }
            }
        };
        return countdownTask;
    }

    public TeleportationInfo getTeleportationInfo(Player player){
        return teleportInfos.get(player.getName());
    }

    public boolean isPlayerInTeleportation(Player player){
        TeleportationInfo tpInfo = getTeleportationInfo(player);
        return tpInfo != null;
    }
    public TeleportationInfo.TeleportationState getPlayerTeleportationState(Player player){
        return getTeleportationInfo(player).teleportationState;
    }

    public boolean canPlayerTeleport(Player player) {
        if(!isPlayerInTeleportation(player)){
            return true;
        }else{
            return getPlayerTeleportationState(player)
                    != TeleportationInfo.TeleportationState.IN_ANIMATION;
        }
    }


}
