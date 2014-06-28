package me.johnking.jlib.protocol.wrappers;

import me.johnking.jlib.reflection.ClassAccessor;
import me.johnking.jlib.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;

/**
 * Created by Marco on 27.06.2014.
 */
public class WrappedWatchableObject extends AbstractWrapper{

    private static final Class<?> WATCHABLE_OBJECT_CLASS = ReflectionUtil.getMinecraftClass("WatchableObject");
    private static final ClassAccessor WATCHABLE_OBJECT_ACCESSOR = new ClassAccessor(WATCHABLE_OBJECT_CLASS);
    private static final Constructor<?> WATCHABLE_OBJECT_CONSTRUCTOR = ReflectionUtil.getConstructor(WATCHABLE_OBJECT_CLASS, Integer.TYPE, Integer.TYPE, Object.class);

    public WrappedWatchableObject(Object handle){
        super(WATCHABLE_OBJECT_CLASS);
        setHandle(handle);
    }

    public WrappedWatchableObject(int index, Object value){
        super(WATCHABLE_OBJECT_CLASS);

        setHandle(newHandle(index, value));
    }

    public int getIndex(){
        return WATCHABLE_OBJECT_ACCESSOR.getValue(Integer.TYPE, 1, this.handle);
    }

    public Object getValue(){
        return WATCHABLE_OBJECT_ACCESSOR.getValue(Object.class, 0, this.handle);
    }

    public void setValue(Object object){
        WATCHABLE_OBJECT_ACCESSOR.setValue(Object.class, 0, this.handle, object);
    }

    public static Object newHandle(int index, Object value){
        try {
            return WATCHABLE_OBJECT_CONSTRUCTOR.newInstance(WrappedDataWatcher.getTypeId(value.getClass()), index, value);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
