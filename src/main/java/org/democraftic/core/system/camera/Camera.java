package org.democraftic.core.system.camera;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

public class Camera {

    private final ProtocolManager protocolManager;

    public Camera(ProtocolManager protocolManager){
        this.protocolManager = protocolManager;
    }

    public ArmorStand lockCameraToArmorStand(Player player, Location location){
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);

        armorStand.setHeadPose(new EulerAngle(0,90,0));



        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA);

        packet.getIntegers().write(0,armorStand.getEntityId());

        protocolManager.sendServerPacket(player,packet);

        player.setInvisible(true);

        return armorStand;
    }

    public void moveArmorStand(Player player, ArmorStand armorStand,Location location){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        packet.getIntegers().write(0,armorStand.getEntityId());

        packet.getDoubles().write(0,location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        protocolManager.sendServerPacket(player,packet);

        PacketContainer rotateHead = protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        PacketContainer rotateBody = protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);

        rotateHead.getIntegers()
                .write(0, armorStand.getEntityId()); //The second argument sets the ID of the object that will be used
        rotateBody.getIntegers()
                .write(0, armorStand.getEntityId()); //The second argument sets the ID of the object that will be used

        float yaw = location.getYaw();
        float pitch = location.getPitch();

        rotateHead.getBytes()
                .write(0, (byte) ((yaw % 360) * 256 / 360)); //set head rotation
        rotateBody.getBytes()
                .write(0, (byte) ((yaw % 360) * 256 / 360)) //set head rotation
                .write(1, (byte) ((pitch % 360) * 256 / 360)); //set body rotation

        protocolManager.sendServerPacket(player, rotateHead);
        protocolManager.sendServerPacket(player, rotateBody);
    }

    public void unlockCamera(Player player){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA);

        packet.getIntegers().write(0,player.getEntityId());

        protocolManager.sendServerPacket(player,packet);
    }
}
