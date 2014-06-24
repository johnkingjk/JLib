package me.johnking.jlib.disguise;

import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Marco
 * @version 1.0.0
 */

public class PlayerDisguise{
	
	private static final Method getHandle = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftPlayer"), "getHandle");
	private static final Method getId = ReflectionUtil.getMethod(ReflectionUtil.getMinecraftClass("Entity"), "getId");
	
	private Player player;
	private int id;
	private String name;
	
	public PlayerDisguise(Player player, String name){
		this.player = player;
		this.name = name;
		
		try {
			Object nms_player = getHandle.invoke(player);
			this.id = (int) getId.invoke(nms_player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getEntityId(){
		return id;
	}
	
	public String getNewName(){
		return name;
	}
}
