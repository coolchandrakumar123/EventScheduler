package com.chan.eventscheduler.eventscheduler;

import java.lang.reflect.Method;
import java.util.List;

public class VTSubscriberInfo
{
    Class<?> subscriberClass;
    private List<VTMethodInfo> vtMethodInfoList;

    public VTSubscriberInfo(Class<?> subscriberClass, List<VTMethodInfo> vtMethodInfoList)
    {
        this.subscriberClass = subscriberClass;
        this.vtMethodInfoList = vtMethodInfoList;
    }

    public Class<?> getSubscriberClass()
    {
        return subscriberClass;
    }

    public List<VTMethodInfo> getVtMethodInfoList() {
        return vtMethodInfoList;
    }
}

