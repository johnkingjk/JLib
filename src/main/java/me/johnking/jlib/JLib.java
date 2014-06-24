package me.johnking.jlib;

import me.johnking.jlib.disguise.DisguiseManager;
import me.johnking.jlib.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Marco on 24.06.2014.
 */
public class JLib extends JavaPlugin {

    private static JLib instance;
    private ProtocolManager protocolManager;
    private DisguiseManager disguiseManager;

    @Override
    public void onEnable() {
        instance = this;

        protocolManager = new ProtocolManager(this);
        disguiseManager = new DisguiseManager(this, protocolManager);
    }

    public static ProtocolManager getProtocolManager(){
        return instance.protocolManager;
    }

    public static DisguiseManager getDisguiseManager(){
        return instance.disguiseManager;
    }
}
