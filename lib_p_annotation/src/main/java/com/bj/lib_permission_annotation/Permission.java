package com.bj.lib_permission_annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只有被标注的Activity才能申请权限
 * 当有值的时候会立即申请权限
 */
@Documented
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Permission {
    int reqCode() default -1;

    String[] permission() default {};
}
