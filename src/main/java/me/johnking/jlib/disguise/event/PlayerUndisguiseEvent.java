package me.johnking.jlib.disguise.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Marco
 * @version 1.0.0
 */

public class PlayerUndisguiseEvent extends PlayerEvent{

	private static HandlerList handlers = new HandlerList();
	
	private Reason reason;
	
	public PlayerUndisguiseEvent(Player who, Reason reason) {
		super(who);
		this.reason = reason;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Reason getReason(){
		return reason;
	}

	public enum Reason {
		CUSTOM,
		PLAYER_JOIN;
	}
}