package com.chan.eventscheduler.eventscheduler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VTMethodInfo
{
    Object subscriberObj;
    private Method method;
    private int eventType;
    private int methodType;

    public VTMethodInfo(Object subscriberObj, Method method)
    {
        this.subscriberObj = subscriberObj;
        this.method = method;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setEventType(int eventType)
    {
        this.eventType = eventType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public int getEventType() {
        return eventType;
    }

    public int getMethodType() {
        return methodType;
    }
}

