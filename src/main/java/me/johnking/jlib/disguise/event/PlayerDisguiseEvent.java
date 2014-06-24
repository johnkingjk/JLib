package me.johnking.jlib.disguise.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Marco
 * @version 1.0.0
 */

public class PlayerDisguiseEvent extends PlayerEvent implements Cancellable{

	private static HandlerList handlers = new HandlerList();
	
	private String name;
	private boolean cancel;
	
	public PlayerDisguiseEvent(Player who, String name) {
		super(who);
		this.name = name;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public String getNewName(){
		return name;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
