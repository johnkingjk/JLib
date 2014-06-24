package me.johnking.jlib.disguise;

import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.PacketEvent;
import me.johnking.jlib.protocol.event.PacketListener;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Marco
 * @version 1.0.0
 */

public class PacketHandlerListener implements PacketListener{
	
	private DisguiseManager disguiseManager;
	
	public PacketHandlerListener(DisguiseManager disguiseManager){
		this.disguiseManager = disguiseManager;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onPacket(PacketEvent event) {
		try{
			if(event.getPacket().getPacketType().equals(PacketType.Server.PLAYER_INFO)){
				String name = event.getPacket().getString(0);
				if(event.getPlayer().getName().endsWith(name)){
					return;
				}
				for(PlayerDisguise disguise: disguiseManager.getDisguises()){
					if(disguise.getPlayer().getName().equals(name)){
						event.getPacket().setString(0, disguise.getNewName());
					}
				}
			} else if(event.getPacket().getPacketType().equals(PacketType.Server.SCOREBOARD_TEAM)){
				ArrayList players = (ArrayList) event.getPacket().getObject(Collection.class, 0);
				for(PlayerDisguise disguise: disguiseManager.getDisguises()){
					for(int i = players.size(); i > 0; i--){
						if(disguise.getPlayer().getName().equals(players.get(i - 1))){
							players.set(i - 1, disguise.getNewName());
						}
					}
				}
			} else if (event.getPacket().getPacketType().equals(PacketType.Server.SCOREBOARD_SCORE)){
				String name = event.getPacket().getString(0);
				for(PlayerDisguise disguise: disguiseManager.getDisguises()){
					if(disguise.getPlayer().getName().equals(name)){
						event.getPacket().setString(0, disguise.getNewName());
						return;
					}
				}
			} else if (event.getPacket().getPacketType().equals(PacketType.Server.TAB_COMPLETE)){
				String[] tabs = event.getPacket().getObject(String[].class, 0);
				for(PlayerDisguise disguise: disguiseManager.getDisguises()){
					for(int i = 0; i < tabs.length; i++){
						if(tabs[i].equals(disguise.getPlayer().getName())){
							tabs[i] = disguise.getNewName();
						}
					}
				}
			} else if(event.getPacket().getPacketType().equals(PacketType.Server.NAMED_ENTITY_SPAWN)){
				int entityId = event.getPacket().getInt(0);
				for(PlayerDisguise disguise: disguiseManager.getDisguises()){
					if(entityId == disguise.getEntityId()){
						GameProfile profile = new GameProfile(event.getPacket().getObject(GameProfile.class, 0).getId(), disguise.getNewName());
						
						event.getPacket().setObject(GameProfile.class, 0, profile);
					}
				}
			}
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
	}
}
