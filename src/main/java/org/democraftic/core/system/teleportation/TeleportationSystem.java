package org.democraftic.core.system.teleportation;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.democraftic.core.Democraftic;
import org.democraftic.core.system.camera.Camera;
import org.democraftic.core.system.npc.NPC;

import java.util.HashMap;
import java.util.UUID;

public final class TeleportationSystem {
    public final Plugin plugin;

    public static final int waitTime = 3;

    public HashMap<String,TeleportationInfo> teleportInfos = new HashMap<>();


    public TeleportationSystem(Plugin plugin){
        this.plugin = plugin;
    }

    public void CancelTeleport(Player player){
        TeleportationInfo tpInfo = getTeleportationInfo(player);
        if(tpInfo != null){
            tpInfo.countdownTask.cancel();
            teleportInfos.remove(player.getName());
        }

    }



    public void WaitTeleportPlayer(Player player,Location destination){
        if(isPlayerInTeleportation(player) && canPlayerTeleport(player)){
            CancelTeleport(player);
        }

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
        TeleportationInfo tpInfo = new TeleportationInfo(destination,System.currentTimeMillis(),countdownTask);

        teleportInfos.put(player.getName(),tpInfo);
        countdownTask.runTaskTimer(plugin,0,20);


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

        Camera camera = new Camera(Democraftic.protocolManager);

        Location firstAirPosition = new Location(playerPosition.getWorld(),playerPosition.getX(),
                playerPosition.getY()+20,playerPosition.getZ(),0,90);

        Location secondAirPosition = new Location(destination.getWorld(),destination.getX(),
                destination.getY()+20,destination.getZ(),0,90);


        ArmorStand armorStand = camera.lockCameraToArmorStand(player,firstAirPosition);
        npc.spawn(player, playerPosition);


        BukkitRunnable firstDelay = new BukkitRunnable() {
            @Override
            public void run() {
                //camera.unlockCamera(player);

                camera.moveArmorStand(player,armorStand,secondAirPosition);

                npc.moveTo(player,destination);

                BukkitRunnable secondDelay = new BukkitRunnable() {
                    @Override
                    public void run() {
                        camera.unlockCamera(player);
                        armorStand.remove();
                        npc.remove(player);
                        player.teleport(destination);
                        player.setInvisible(false);


                        endTeleport(player);
                    }
                };

                secondDelay.runTaskLater(plugin,30);

            }
        };

        firstDelay.runTaskLater(plugin,30);



    }

    private void endTeleport(Player player){
        teleportInfos.remove(player.getName());
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
