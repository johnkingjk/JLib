package me.johnking.jlib.protocol.event;

/** 
 * @author Marco
 * @version 1.1.0
 */

public abstract interface PacketListener {

	public abstract void onPacket(PacketEvent event);
	
}
