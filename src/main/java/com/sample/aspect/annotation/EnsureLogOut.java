package com.sample.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EnsureLogOut {
    // If true, user will be logged out (session invalidated) only in case of any exception which is thrown outside of the method.
    boolean onlyIfException() default false;
}
