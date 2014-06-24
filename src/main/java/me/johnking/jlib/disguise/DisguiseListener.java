package me.johnking.jlib.disguise;

import me.johnking.jlib.disguise.event.PlayerUndisguiseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Marco
 * @version 1.0.0
 */

public class DisguiseListener implements Listener{
	
	private DisguiseManager disguiseManager;
	
	public DisguiseListener(DisguiseManager disguiseManager){
		this.disguiseManager = disguiseManager;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		for(int i = disguiseManager.getDisguises().size(); i > 0; i--){
			if(disguiseManager.getDisguises().get(i - 1).getNewName().equalsIgnoreCase(e.getPlayer().getName())){
				Player player = disguiseManager.getDisguises().get(i - 1).getPlayer();
				disguiseManager.unDisguisePlayer(player, PlayerUndisguiseEvent.Reason.PLAYER_JOIN);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		for(int i = disguiseManager.getDisguises().size(); i > 0; i--){
			if(disguiseManager.getDisguises().get(i - 1).getPlayer().equals(e.getPlayer())){
				disguiseManager.getDisguises().remove(i - 1);
			}
		}
	}
}
