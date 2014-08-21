package me.johnking.jlib.protocol.wrappers;

import me.johnking.jlib.protocol.util.EntityUtilities;
import me.johnking.jlib.reflection.ClassAccessor;
import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * Created by Marco on 20.08.2014.
 */
public class WrappedGameProfile extends AbstractWrapper {

    private static final Class<?> GAME_PROFILE_CLASS = ReflectionUtil.getClass("net.minecraft.util.com.mojang.authlib.GameProfile");
    private static final ClassAccessor GAME_PROFILE_ACCESSOR = new ClassAccessor(GAME_PROFILE_CLASS);
    private static final Constructor<?> GAME_PROFILE_CONSTRUCTOR = ReflectionUtil.getConstructor(GAME_PROFILE_CLASS, UUID.class, String.class);
    private static final Class<?> ENTITY_HUMAN_CLASS = ReflectionUtil.getMinecraftClass("EntityHuman");
    private static final ClassAccessor ENTITY_HUMAN_ACCESSOR = new ClassAccessor(ENTITY_HUMAN_CLASS);

    public WrappedGameProfile(UUID uuid, String name) {
        super(GAME_PROFILE_CLASS);
        try {
            setHandle(GAME_PROFILE_CONSTRUCTOR.newInstance(uuid, name));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public WrappedGameProfile(Player player) {
        super(GAME_PROFILE_CLASS);
        setHandle(ENTITY_HUMAN_ACCESSOR.getValue(GAME_PROFILE_CLASS, 0, EntityUtilities.getHandle(player)));
    }

    public String getName() {
        return GAME_PROFILE_ACCESSOR.getValue(String.class, 0, this.handle);
    }

    public void setName(String name) {
        GAME_PROFILE_ACCESSOR.setValue(String.class, 0, this.handle, name);
    }

    public UUID getUUID() {
        return GAME_PROFILE_ACCESSOR.getValue(UUID.class, 0, this.handle);
    }

    public void setUUID(UUID uuid) {
        GAME_PROFILE_ACCESSOR.setValue(UUID.class, 0, this.handle, uuid);
    }
}
