package me.johnking.jlib.protocol.util;

import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Marco on 29.06.2014.
 */
public class PlayerUtilities {

    private static final Method GET_HANDLE = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftPlayer"), "getHandle");

    public static Object getNMSPlayer(Player player){
        try {
            return GET_HANDLE.invoke(player);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Object> getNMSPlayers(List<Player> player){
        ArrayList<Object> nmsPlayer = new ArrayList();
        for(Player p: player){
            nmsPlayer.add(getNMSPlayer(p));
        }
        return nmsPlayer;
    }
}
