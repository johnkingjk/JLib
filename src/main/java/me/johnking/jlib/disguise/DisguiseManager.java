package me.johnking.jlib.disguise;

import me.johnking.jlib.disguise.event.PlayerDisguiseEvent;
import me.johnking.jlib.disguise.event.PlayerUndisguiseEvent;
import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.ProtocolManager;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.johnking.jlib.reflection.ReflectionUtil;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author Marco
 * @version 1.0.0
 */

public class DisguiseManager {

	private static final Method getHandle = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftPlayer"), "getHandle");
	private static final Method getProfile = ReflectionUtil.getMethod(ReflectionUtil.getMinecraftClass("EntityPlayer"), "getProfile");
	private static final Class<?> entityHuman = ReflectionUtil.getMinecraftClass("EntityHuman");

	private ArrayList<PlayerDisguise> disguises;
	private ProtocolManager protocolManager;
	private PacketHandlerListener packetHandler;
	private DisguiseListener listener;

	public DisguiseManager(Plugin plugin, ProtocolManager protocolManager) {
		this.protocolManager = protocolManager;
		this.disguises = new ArrayList<PlayerDisguise>();
		this.packetHandler = new PacketHandlerListener(this);
		this.listener = new DisguiseListener(this);
		Bukkit.getPluginManager().registerEvents(this.listener, plugin);
		this.protocolManager.registerListener(packetHandler, PacketType.Server.TAB_COMPLETE, PacketType.Server.NAMED_ENTITY_SPAWN, PacketType.Server.SCOREBOARD_TEAM, PacketType.Server.SCOREBOARD_SCORE, PacketType.Server.PLAYER_INFO);
	}

	public ArrayList<PlayerDisguise> getDisguises() {
		return disguises;
	}

	public void disguisePlayer(PlayerDisguise disguise, boolean ignoreDouplicate) {
		// check if name as player name already exists
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(disguise.getNewName())) {
				try {
					throw new DisguiseException("This is a name of an online-player!");
				} catch (DisguiseException e) {
					e.printStackTrace();
					return;
				}
			}
		}
		if (!ignoreDouplicate) {
			// check if name as disguise name already exists
			for (PlayerDisguise dis : disguises) {
				if (dis.getNewName().equalsIgnoreCase(disguise.getNewName())) {
					try {
						throw new DisguiseException("This is a name of an disguised-player!");
					} catch (DisguiseException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}

		PlayerDisguiseEvent event = new PlayerDisguiseEvent(disguise.getPlayer(), disguise.getNewName());
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}

		// check if already disguised and remove old disguise on duplicate
		for (int i = disguises.size(); i > 0; i--) {
			if (disguises.get(i - 1).getPlayer().equals(disguise.getPlayer())) {
				disguises.remove(i - 1);
			}
		}
		try {
			Object nms = getHandle.invoke(disguise.getPlayer());

			Object destroyPacket = PacketType.Server.ENTITY_DESTROY.getPacketClass().getConstructors()[1].newInstance(new int[] { disguise.getEntityId() });
			ProtocolPacket spawnPacket = new ProtocolPacket(PacketType.Server.NAMED_ENTITY_SPAWN, PacketType.Server.NAMED_ENTITY_SPAWN.getPacketClass().getConstructor(entityHuman).newInstance(nms));

			GameProfile profile = new GameProfile(((GameProfile) getProfile.invoke(nms)).getId(), disguise.getNewName());

			spawnPacket.setObject(GameProfile.class, 0, profile);

			Player[] players = Bukkit.getOnlinePlayers();
			for (int i = 0; i < players.length; i++) {
				if (players[i].equals(disguise.getPlayer())) {
					players[i] = null;
				}
			}

			protocolManager.sendPacket(new Object[] { destroyPacket, spawnPacket.getHandle() }, players);
			
			updateTabList(disguise.getPlayer().getName(), disguise.getNewName(), players);
			disguises.add(disguise);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void unDisguisePlayer(Player player) {
		unDisguisePlayer(player, PlayerUndisguiseEvent.Reason.CUSTOM);
	}

	public void unDisguisePlayer(Player player, PlayerUndisguiseEvent.Reason reason) {
		PlayerDisguise disguise = null;
		for (int i = disguises.size(); i > 0; i--) {
			if (disguises.get(i - 1).getPlayer().equals(player)) {
				disguise = disguises.get(i - 1);
				disguises.remove(i - 1);
			}
		}
		if (disguise == null) {
			Bukkit.getLogger().log(Level.WARNING, "Failed to undisguise not disguised player!");
			return;
		}

		PlayerUndisguiseEvent event = new PlayerUndisguiseEvent(player, reason);
		Bukkit.getPluginManager().callEvent(event);

		// send update packets
		try {
			Object nms = getHandle.invoke(player);

			Object destroyPacket = PacketType.Server.ENTITY_DESTROY.getPacketClass().getConstructors()[1].newInstance(new int[] { disguise.getEntityId() });
			Object spawnPacket = PacketType.Server.NAMED_ENTITY_SPAWN.getPacketClass().getConstructor(entityHuman).newInstance(nms);

			Player[] players = Bukkit.getOnlinePlayers();
			for (int i = 0; i < players.length; i++) {
				if (players[i].equals(player)) {
					players[i] = null;
				}
			}

			protocolManager.sendPacket(new Object[] { destroyPacket, spawnPacket }, players);
			
			updateTabList(disguise.getNewName(), disguise.getPlayer().getName(), players);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateTabList(String oldName, String newName, Player[] players) throws Exception{
		Constructor<?> consturctor = PacketType.Server.PLAYER_INFO.getPacketClass().getConstructor(String.class, Boolean.TYPE, Integer.TYPE);
			
		Object oldPacket = consturctor.newInstance(oldName, false, 9999);
		Object newPacket = consturctor.newInstance(newName, true, 9999);		
		
		protocolManager.sendPacket(new Object[]{oldPacket, newPacket}, players);
	}

	public boolean isDisguised(Player player) {
		for (PlayerDisguise disguise : disguises) {
			if (disguise.getPlayer().equals(player)) {
				return true;
			}
		}
		return false;
	}

	public String getDisguisedName(Player player) {
		for (PlayerDisguise disguise : disguises) {
			if (disguise.getPlayer().equals(player)) {
				return disguise.getNewName();
			}
		}
		return player.getName();
	}
}
