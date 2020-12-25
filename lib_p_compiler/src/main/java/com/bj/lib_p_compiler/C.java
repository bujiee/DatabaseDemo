package com.bj.lib_p_compiler;

import com.squareup.javapoet.ClassName;

public class C {
    public static final ClassName Context = ClassName.get("android.content", "Context");
    public static final ClassName ContextCompat = ClassName.get("androidx.core.content", "ContextCompat");
    public static final ClassName PackageManager = ClassName.get("android.content.pm", "PackageManager");
    public static final ClassName IPermissionResultListener = ClassName.get("com.bujie.permission.core.permisson", "IPermission");

    public static ClassName createClass(String pkName,String className) {
        return ClassName.get(pkName, className);
    }
}
