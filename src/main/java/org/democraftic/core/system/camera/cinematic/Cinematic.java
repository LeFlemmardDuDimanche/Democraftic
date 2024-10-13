package org.democraftic.core.system.camera.cinematic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.democraftic.core.Democraftic;
import org.democraftic.core.system.camera.CameraBody;
import org.democraftic.core.system.camera.CameraManager;

import java.util.Locale;

public class Cinematic {
    private final CinematicScript script;
    private final Player player;
    private CameraBody camera;

    public Cinematic(CinematicScript script, Player player) {
        this.script = script;
        this.player = player;
    }

    public Location getFirstLocation(){
        return script.get(0).location;
    }

    public CinematicChapter getNextChapter(){
        return script.getNextChapter();
    }

    public boolean isLastChapter(){
        return script.isLastChapter();
    }

    public void setBodyCam(CameraBody cameraBody){
        this.camera = cameraBody;
    }

    public CameraBody getCamera(){
        return camera;
    }

    public Player getPlayer(){
        return player;
    }
}
