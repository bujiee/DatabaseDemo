package com.bj.lib_permission_annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 申请权限的,用于标注Activity,立即申请权限
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Permission {
    int reqCode() default -1;

    String[] permission() default {};
}
