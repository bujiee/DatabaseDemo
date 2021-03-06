package com.bujie.permission.core;

import android.app.Activity;

import com.bujie.permission.core.permisson.IPermission;
import com.bujie.permission.core.permisson.PermissionUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class Tai {
    static final Map<Class<?>, Constructor<? extends IPermission>> BINDINGS = new LinkedHashMap<>();
    private Activity activity;
    private IPermission permission;
    private int code = -1;
    private String[] defaultPermission = null;

    private Tai(Activity activity) {
        this.activity = activity;
        permission = apply();
        if (code != -1 && defaultPermission != null && defaultPermission.length > 0) {
            requestPermission(code, defaultPermission);
        }
    }

    public static Tai get(Activity activity) {
        return new Tai(activity);
    }

    public void requestPermission(int code, String[] permissions) {
        //校验权限
        String[] defined = PermissionUtil.checkAllPermissions(activity, permissions);
        if (defined.length == 0 && permission != null) {
            permission.onPermissionGranted(code);
            return;
        }
        //申请权限
        PermissionUtil.requestPermissions(activity, code, permissions, permission);
    }


    @SuppressWarnings("unchecked")
    public IPermission apply() {
        Class<?> act = activity.getClass();
        Constructor<? extends IPermission> constructor;
        Class<?> bindingClass = null;
        if ((constructor = BINDINGS.get(act)) == null) {
            String clsName = act.getName();
            try {
                bindingClass = activity.getClassLoader().loadClass(clsName + "_PermissionBinding");
                constructor = (Constructor<? extends IPermission>) bindingClass.getConstructor(act);
                BINDINGS.put(act, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (constructor == null) {
            return null;
        }

        try {
            IPermission iPermission = constructor.newInstance(activity);
            request(bindingClass, iPermission);
            return iPermission;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            throw new RuntimeException("Unable to create IPermission instance.", cause);
        }
    }

    private void request(Class<?> bindingClass, Object object) {
        if (bindingClass != null) {
            Field requestCode = null;
            try {
                requestCode = bindingClass.getDeclaredField("requestCode");
                code = requestCode.getInt(object);

                Field per = bindingClass.getField("permissions");
                defaultPermission = (String[]) per.get(object);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        try {
            if (activity == null) {
                return;
            }
//            String simpleName = activity.getComponentName().getClassName();
//
//            Class<? extends IPermission> mClass = (Class<? extends IPermission>) Class.forName(simpleName + "_PermissionBinding");
            PermissionUtil.onRequestPermissionsResult(activity, requestCode,
                    permissions, permission);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
