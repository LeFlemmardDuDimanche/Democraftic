package org.democraftic.core.system.teleportation;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportationInfo {
    public final Location destination;
    public final Long baseTime;
    public BukkitRunnable countdownTask;
    public TeleportationState teleportationState;

    public TeleportationInfo(Location destination, Long baseTime, BukkitRunnable countdownTask) {
        this.destination = destination;
        this.baseTime = baseTime;
        this.countdownTask = countdownTask;
        this.teleportationState = TeleportationState.WAITING;
    }
    public TeleportationInfo(Location destination, Long baseTime,TeleportationState teleportationState) {
        this.destination = destination;
        this.baseTime = baseTime;
        this.countdownTask = null;
        this.teleportationState = teleportationState;
    }


    public enum TeleportationState{
        WAITING,
        IN_ANIMATION,
        TELEPORTED;
    }

}