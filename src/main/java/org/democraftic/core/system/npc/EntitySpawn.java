package org.democraftic.core.system.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

/*
 * Code copié sur : https://www.spigotmc.org/threads/1-19-4-npc-with-protocollib.638115/
 *
 */

public class EntitySpawn {

    private final ProtocolManager protocolManager;
    private final UUID uuid;
    private final int id;

    public EntitySpawn(
            ProtocolManager protocolManager,
            UUID uuid, int id) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
        this.id = id;
    }

    public void spawnEntity(Player player, Location location) {

        /*
        Create a named entity spawn packet
         */
        PacketContainer npc = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);


        npc.getIntegers()
                .write(0, id) //the second value it's entity id, it must be unique!
                .writeSafely(1, 122); //the second value it's same entity id, but it use for only spawn entity

        npc.getUUIDs()
                .write(0, uuid); //uuid must be like uuid in player info packet!

        npc.getEntityTypeModifier()
                .writeSafely(0, EntityType.PLAYER); //Entity Type, nothing complicated.

        npc.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ()); //Spawn location

        npc.getBytes()
                .write(0, (byte) (0))
                .write(1, (byte) (0))
                .write(2, (byte) (0)); //rotate location




        /*
        In the first method arguments, we need to put player to who that send packet
        In the second method arguments, we need to put packet that sends.
         */
        protocolManager.sendServerPacket(player, npc);

        rotateEntity(player,location);

    }

    private void rotateEntity(Player player, Location location){
        PacketContainer rotateHead = protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        PacketContainer rotateBody = protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);

        rotateHead.getIntegers()
                .write(0, id); //The second argument sets the ID of the object that will be used
        rotateBody.getIntegers()
                .write(0, id); //The second argument sets the ID of the object that will be used

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

    public void moveEntity(Player player, Location location){

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        packet.getIntegers().write(0,id);

        packet.getDoubles().write(0,location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        protocolManager.sendServerPacket(player,packet);
        rotateEntity(player,location);

    }

    public void removeEntity(Player player){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);


        //packet.getIntegers().write(0,1);
        packet.getIntLists().write(0,new ArrayList<Integer>() {{this.add(id);}});

        protocolManager.sendServerPacket(player,packet);

    }

}