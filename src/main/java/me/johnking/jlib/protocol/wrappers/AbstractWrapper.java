package me.johnking.jlib.protocol.wrappers;

/**
 * Created by Marco on 27.06.2014.
 */
public abstract class AbstractWrapper {

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
}
