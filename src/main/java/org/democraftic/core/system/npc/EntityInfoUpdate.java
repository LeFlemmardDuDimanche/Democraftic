package org.democraftic.core.system.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/*
 * Code copié sur : https://www.spigotmc.org/threads/1-19-4-npc-with-protocollib.638115/
 *
 */
public class EntityInfoUpdate {

    private final ProtocolManager protocolManager;
    private final UUID uuid;
    private final String name;
    public final WrappedSignedProperty textureProperty;

    public EntityInfoUpdate(
            ProtocolManager protocolManager,
            UUID uuid, String name, WrappedSignedProperty textureProperty, int id) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
        this.name = name;
        this.textureProperty = textureProperty;
    }

    public void playerInfoUpdate(Player player) {

        PacketContainer npc = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO); // make player info packet
        Set<EnumWrappers.PlayerInfoAction> playerInfoActionSet = new HashSet<>(); //The set collection that contains Player Info Action, very important

        WrappedGameProfile wrappedGameProfile = new WrappedGameProfile(uuid, name);


        wrappedGameProfile.getProperties().clear();
        wrappedGameProfile.getProperties()
                .put("textures", textureProperty);


        PlayerInfoData playerInfoData = new PlayerInfoData(
                wrappedGameProfile,
                0,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText("name"));


        List<PlayerInfoData> playerInfoDataList = Arrays.asList(playerInfoData); //Add player's info data in list like here.

        playerInfoActionSet.add(EnumWrappers.PlayerInfoAction.ADD_PLAYER); //Adds player actions that the server must perform, in our case this is adding a player

        npc.getPlayerInfoActions()
                .write(0, playerInfoActionSet); //Add a player's action to our packet

        npc.getPlayerInfoDataLists().write(1, playerInfoDataList); //Add a list of player's info data in out packet

        protocolManager.sendServerPacket(player, npc);
    }

    public void PlayerInfoRemove(Player player){
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);

        //packet.getIntegers().write(0,1);
        packet.getUUIDLists().write(0, Collections.singletonList(uuid));

        protocolManager.sendServerPacket(player,packet);
    }

}