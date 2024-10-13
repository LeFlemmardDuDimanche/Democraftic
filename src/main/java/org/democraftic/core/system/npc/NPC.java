package org.democraftic.core.system.npc;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class NPC {

    private EntityInfoUpdate entityInfoUpdate;
    private EntitySpawn entitySpawn;

    private ProtocolManager protocolManager;
    private UUID uuid;
    private int id;



    public NPC(
            ProtocolManager protocolManager,
            UUID uuid) {
        this.protocolManager = protocolManager;
        this.uuid = uuid;
        this.id = -1;
    }

    public void spawn(Player player, Location location) {

        WrappedSignedProperty property = getSkinByName(player.getName());

        entityInfoUpdate = new EntityInfoUpdate(protocolManager, uuid, player.getName(), property,id);
        entitySpawn = new EntitySpawn(protocolManager, uuid,id);

        entityInfoUpdate.playerInfoUpdate(player);
        entitySpawn.spawnEntity(player, location);
    }

    public void moveTo(Player player, Location location){
        entitySpawn.moveEntity(player,location);
    }

    public void remove(Player player){

        entityInfoUpdate.PlayerInfoRemove(player);
        entitySpawn.removeEntity(player);
    }

    public static WrappedSignedProperty getSkinByName(String name){
        String value = null;
        String signature = null;


        Object obj = JsonParser.parseString(getResponse("https://api.mojang.com/users/profiles/minecraft/" + name));
        JsonObject json = (JsonObject)obj;
        String uuid = json.get("id").toString().replace("\"","");
        Object obj2 = JsonParser.parseString(getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false"));
        JsonObject json2 = (JsonObject)obj2;
        Object props = ((JsonArray)json2.get("properties")).get(0);
        JsonObject propsObj = (JsonObject)props;
        value = propsObj.get("value").toString();
        signature = propsObj.get("signature").toString();

        WrappedSignedProperty property = new WrappedSignedProperty(
                "textures",value,signature);
        return property;
    }
    public static String getResponse(String _url){
        try {
            URL url = new URL(_url);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;

            StringBuilder result = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }



            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}