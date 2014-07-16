package me.johnking.jlib.protocol.wrappers;

import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Marco on 27.06.2014.
 */
public abstract class AbstractWrapper {

    private static final Method GET_ENTITY_HANDLE = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftEntity"), "getHandle");
    protected Class<?> handleClass;
    protected Object handle;

    public AbstractWrapper(Class<?> handleClass){
        this.handleClass = handleClass;
    }

    public void setHandle(Object handle){
        if(handle == null){
            throw new IllegalArgumentException("Handle cannot be null!");
        }
        if(!handleClass.isAssignableFrom(handle.getClass())){
            throw new IllegalArgumentException("Wrong Handle");
        }
        this.handle = handle;
    }

    public Object getHandle(){
        return handle;
    }

    public Class<?> getHandleClass(){
        return handleClass;
    }

    public static Object getEntityHandle(Entity entity){
        try {
            return GET_ENTITY_HANDLE.invoke(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
