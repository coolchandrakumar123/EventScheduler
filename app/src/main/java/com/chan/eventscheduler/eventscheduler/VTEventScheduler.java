package com.chan.eventscheduler.eventscheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import android.os.Bundle;

/**
 * Created by chandra-1765 on 2/6/17.
 */

public class VTEventScheduler {
    //volatile

    public static String TAG = "DUMMY_TAG";

    private static VTEventScheduler instance;

    private final Map<String, CopyOnWriteArrayList<VTSubscriberInfo>> subscriberInfoByTag;

    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC;

    private VTEventScheduler() {
        subscriberInfoByTag = new HashMap<>();
    }

    public static synchronized VTEventScheduler getInstance() {
        if (instance == null) {
            instance = new VTEventScheduler();
        }
        return instance;
    }

    public void register(Object object, String tag) {

        synchronized (this) {
            CopyOnWriteArrayList<VTSubscriberInfo> subscriberInfoList = subscriberInfoByTag.get(tag);
            if (subscriberInfoList == null) {
                subscriberInfoList = new CopyOnWriteArrayList<VTSubscriberInfo>();
            }
            subscriberInfoList.add(findSubscriberMethods(object));
            subscriberInfoByTag.put(tag, subscriberInfoList);
        }
    }

    private VTSubscriberInfo findSubscriberMethods(Object object)
    {
        Class<?> subscriberClass = object.getClass();
        Method[] methods;
        try {
            methods = subscriberClass.getDeclaredMethods();
        } catch (Throwable th) {
            methods = subscriberClass.getMethods();
        }

        List<VTMethodInfo> methodList = new ArrayList<>();
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                /*Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    }*/
                VTSubscribe subscribeAnnotation = method.getAnnotation(VTSubscribe.class);
                if (subscribeAnnotation != null) {
                    VTMethodInfo vtMethodInfo = new VTMethodInfo(object, method);
                    vtMethodInfo.setEventType(subscribeAnnotation.eventType());
                    vtMethodInfo.setMethodType(subscribeAnnotation.methodType());
                    methodList.add(vtMethodInfo);
                }
            }
        }
        return new VTSubscriberInfo(subscriberClass, methodList);
    }

    public void postEvent(String tag, int requestType, int requestAction, Bundle bundle) {
        CopyOnWriteArrayList<VTSubscriberInfo> subscriberInfoList = subscriberInfoByTag.get(tag);
        if (subscriberInfoList != null) {
            for (int i = 0; i < subscriberInfoList.size(); i++) {
                VTSubscriberInfo vtSubscriberInfo = subscriberInfoList.get(i);
                postSingleEvent(vtSubscriberInfo, requestType, new Object[]{requestAction, bundle});
            }
        }
    }

    private void postSingleEvent(VTSubscriberInfo vtSubscriberInfo, int requestType, Object[] params) {
        List<VTMethodInfo> vtMethodInfoList = vtSubscriberInfo.getVtMethodInfoList();
        if (vtMethodInfoList != null) {
            for (int i = 0; i < vtMethodInfoList.size(); i++) {
                VTMethodInfo vtMethodInfo = vtMethodInfoList.get(i);
                postSingleEventToMethod(vtMethodInfo, requestType, params);
            }
        }
    }

    private void postSingleEventToMethod(VTMethodInfo vtMethodInfo, int requestType, Object[] params) {
        if (vtMethodInfo.getEventType() == requestType) {
            try {
                vtMethodInfo.getMethod().invoke(vtMethodInfo.subscriberObj, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegister(String tag) {
        synchronized (this) {
            CopyOnWriteArrayList<VTSubscriberInfo> subscriberInfoList = subscriberInfoByTag.get(tag);
            if (subscriberInfoList != null) {
                for (int i = 0; i < subscriberInfoList.size(); i++) {
                    //Do Iterate
                    VTSubscriberInfo vtSubscriberInfo = subscriberInfoList.get(i);
                    vtSubscriberInfo.subscriberClass = null;
                }
            }
        }
    }
}
