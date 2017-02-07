package com.chan.eventscheduler.eventscheduler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface VTSubscribe
{
    int eventType() default VTEventTypes.VIEW;

    int methodType() default VTMethodTypes.RECEIVE_RESULT;
}

