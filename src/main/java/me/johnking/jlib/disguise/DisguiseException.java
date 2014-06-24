package me.johnking.jlib.disguise;

/**
 * @author Marco
 * @version 1.0.0
 */

public class DisguiseException extends Exception{

	private static final long serialVersionUID = -2885308179100742669L;

	public DisguiseException(){
		
	}
	
	public DisguiseException(String arg0){
		super(arg0);
	}
	
	public DisguiseException(Throwable arg0){
		super(arg0);
	}
	
	public DisguiseException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}
}
