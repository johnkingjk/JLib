package me.johnking.jlib.protocol.wrappers;

import me.johnking.jlib.reflection.ClassAccessor;
import me.johnking.jlib.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by Marco on 27.06.2014.
 */
public class WrappedDataWatcher extends AbstractWrapper{

    private static final Class<?> DATA_WATCHER_CLASS = ReflectionUtil.getMinecraftClass("DataWatcher");
    private static final ClassAccessor DATA_WATCHER_ACCESSOR = new ClassAccessor(DATA_WATCHER_CLASS);
    private static final Constructor<?> DATA_WATCHER_CONSTRUCTOR = ReflectionUtil.getConstructor(DATA_WATCHER_CLASS, ReflectionUtil.getMinecraftClass("Entity"));

    private static final Field TYPE_MAP_FIELD = DATA_WATCHER_ACCESSOR.withTypeAndIndex(Map.class, 0);
    private static final Field VALUE_MAP_FIELD = DATA_WATCHER_ACCESSOR.withTypeAndIndex(Map.class, 1);

    private static final Field READ_WRITE_LOCK_FIELD = DATA_WATCHER_ACCESSOR.withTypeAndIndex(ReadWriteLock.class, 0);

    private static Map<Class<?>, Integer> TYPE_MAP;

    private ReadWriteLock lock;
    private Map<Integer, Object> watchableObjects;

    public WrappedDataWatcher(){
        super(DATA_WATCHER_CLASS);
        setHandle(newHandle());

        try {
            lock = (ReadWriteLock) READ_WRITE_LOCK_FIELD.get(this.handle);
            watchableObjects = (Map<Integer, Object>) VALUE_MAP_FIELD.get(this.handle);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object newHandle(){
        try{
            return DATA_WATCHER_CONSTRUCTOR.newInstance(new Object[]{null});
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getTypeId(Class<?> clazz){
        return TYPE_MAP.get(clazz);
    }

    private Object getWatchableObject(int index){
        lock.readLock().lock();
        Object object = watchableObjects.get(index);
        lock.readLock().unlock();
        return object;
    }

    public Byte getByte(int index){
        return (Byte) getObject(index);
    }

    public Short getShort(int index){
        return (Short) getObject(index);
    }

    public Integer getInteger(int index){
        return (Integer) getObject(index);
    }

    public Float getFloat(int index){
        return (Float) getObject(index);
    }

    public String getString(int index){
        return (String) getObject(index);
    }

    public Object getObject(int index){
        Object object = getWatchableObject(index);
        if(object != null){
            return new WrappedWatchableObject(object).getValue();
        } else {
            return null;
        }
    }

    public void setObject(int index, Object value){
        Object object = getWatchableObject(index);
        if(object != null){
            new WrappedWatchableObject(object).setValue(value);
        } else {
            watchableObjects.put(index, new WrappedWatchableObject(index, value).getHandle());
        }
    }

    static {
        try {
            TYPE_MAP = (Map<Class<?>, Integer>) TYPE_MAP_FIELD.get(null);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
