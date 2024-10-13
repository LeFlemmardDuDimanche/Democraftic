package org.democraftic.core.system.camera;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.democraftic.core.Democraftic;


public class CameraBody {
    private ArmorStand entityArmature;
    private Player player;

    public CameraBody(ArmorStand entityArmature,Player player) {
        this.entityArmature = entityArmature;
        this.player = player;

        this.entityArmature.setInvisible(true);
        this.entityArmature.setInvulnerable(true);
        this.entityArmature.setGravity(false);

        PacketContainer packet = Democraftic.protocolManager.createPacket(PacketType.Play.Server.CAMERA);

        packet.getIntegers().write(0,this.entityArmature.getEntityId());

        Democraftic.protocolManager.sendServerPacket(player,packet);

        player.setInvisible(true);

    }


    public void moveTo(Location location){
        PacketContainer packet = Democraftic.protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        packet.getIntegers().write(0,entityArmature.getEntityId());

        packet.getDoubles().write(0,location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        Democraftic.protocolManager.sendServerPacket(player,packet);

        PacketContainer rotateHead = Democraftic.protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        PacketContainer rotateBody = Democraftic.protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);

        rotateHead.getIntegers()
                .write(0, entityArmature.getEntityId());
        rotateBody.getIntegers()
                .write(0, entityArmature.getEntityId());

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        rotateHead.getBytes()
                .write(0, (byte) ((yaw % 360) * 256 / 360)); //set head rotation
        rotateBody.getBytes()
                .write(0, (byte) ((yaw % 360) * 256 / 360)) //set head rotation
                .write(1, (byte) ((pitch % 360) * 256 / 360)); //set body rotation

        Democraftic.protocolManager.sendServerPacket(player, rotateHead);
        Democraftic.protocolManager.sendServerPacket(player, rotateBody);
    }

    public void unlockCamera(){
        PacketContainer packet = Democraftic.protocolManager.createPacket(PacketType.Play.Server.CAMERA);

        packet.getIntegers().write(0,player.getEntityId());

        Democraftic.protocolManager.sendServerPacket(player,packet);

        entityArmature.remove();
    }

}
