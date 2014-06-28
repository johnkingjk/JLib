package me.johnking.jlib.protocol.wrappers;

import me.johnking.jlib.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by Marco on 28.06.2014.
 */
public class WrappedChatComponent extends AbstractWrapper{

    private static final Class<?> I_CHAT_BASE_COMPONENT = ReflectionUtil.getMinecraftClass("IChatBaseComponent");
    private static final Constructor<?> CHAT_COMPONENT_TEXT_CONSTRUCTOR = ReflectionUtil.getConstructor(ReflectionUtil.getMinecraftClass("ChatComponentText"), String.class);
    private static final Method MESSAGE_FROM_STRING = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("util.CraftChatMessage"), "fromString");

    private WrappedChatComponent(Object handle){
        super(I_CHAT_BASE_COMPONENT);
        setHandle(handle);
    }

    public static WrappedChatComponent fromHandle(Object handle){
        return new WrappedChatComponent(handle);
    }

    public static WrappedChatComponent[] fromChatMessage(String message){
        try{
            Object[] handles = (Object[]) MESSAGE_FROM_STRING.invoke(null, message);
            WrappedChatComponent[] chatComponents = new WrappedChatComponent[handles.length];
            for(int i = 0; i < handles.length; i++){
                chatComponents[i] = fromHandle(handles[i]);
            }
            return chatComponents;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static WrappedChatComponent fromText(String message){
        if(message == null){
            throw new IllegalArgumentException("Message cannot be null");
        }
        try{
            return new WrappedChatComponent(CHAT_COMPONENT_TEXT_CONSTRUCTOR.newInstance(message));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
