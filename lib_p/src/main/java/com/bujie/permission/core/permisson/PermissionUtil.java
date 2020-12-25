package com.bujie.permission.core.permisson;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


/**
 * Created by Scofield on 2018/10/30.
 */

public class PermissionUtil {

    /**
     * 申请权限
     **/
    public static void requestPermissions(Activity activity, int requestCode, String[] permissions, IPermission listener) {
//        if (ListUtil.isEmpty(permissions)) {
//            throw new IllegalArgumentException("权限列表不能为空");
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 当前SDK版本在6.0及以上才需要动态检测权限
            String[] deniedPermissions = checkAllPermissions(activity, permissions);
            if (deniedPermissions.length > 0) {
                ActivityCompat.requestPermissions(activity, deniedPermissions, requestCode);
                return;
            }
            // 所有权限都被允许
        }
        // 当前SDK版本在6.0以下在安装时赋予所有权限
        if (listener != null) {
            listener.onPermissionGranted(requestCode);
        }
    }

    /**
     * 当申请权限返回时
     **/
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, IPermission listener) {
        String[] deniedPermissions = checkAllPermissions(activity, permissions);
        if (deniedPermissions.length > 0) {
            // 优先判断是否有拒绝且不再提示的权限
            if (shouldShowRequestPermissionRationale(activity, deniedPermissions)) {
                // 用户在已经拒绝授权并且shouldShowRequestPermissionRationale返回false，表示用户勾选了“不再提示”
                if (listener != null) {
                    listener.onPermissionDeniedAndNotHint(requestCode, deniedPermissions);
                }
            } else {
                if (listener != null) {
                    listener.onPermissionDenied(requestCode, deniedPermissions);
                }
            }
        } else {
            // 权限请求成功
            if (listener != null) {
                listener.onPermissionGranted(requestCode);
            }
        }
    }

    private static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测所有权限，返回未允许的权限
     **/
    public static String[] checkAllPermissions(Context context, String[] permissions) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 未授权权限
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

//    public static void showToSettDialog(Activity activity, int requestCode) {
//        boolean isNeedExit = requestCode == Config.REQUEST_CODE_PERMISSION_NECESSARY;
//        String message = null;
//        switch (requestCode) {
//            case Config.REQUEST_CODE_PERMISSION_NECESSARY:
//                message = "为了您更好的使用，<br/>请在设置-应用-查客荣耀-权限中开启电话和存储等相关权限";
//                break;
//            case Config.REQUEST_CODE_PERMISSION_CAMERA:
//                message = "为了您更好的使用查客荣耀，<br/>请在设置-应用-查客荣耀-权限中开启相机权限";
//                break;
//            case Config.REQUEST_CODE_PERMISSION_CAMERA_AND_LOCATION:
//                message = "为了您更好的使用查客荣耀，<br/>请在设置-应用-查客荣耀-权限中开启相机和定位权限";
//                break;
//            case Config.REQUEST_CODE_PERMISSION_FACE_SIGN:
//                message = "为了您更好的使用查客荣耀，<br/>请在设置-应用-查客荣耀-权限中开启相机和麦克风和存储权限";
//                break;
//            case Config.REQUEST_CODE_PERMISSION_LOCATION:
//                message = "打开“定位服务“允许“查客荣耀“确定您的位置，为您提供更快捷的套餐查找服务～";
//                break;
//            default:
//                break;
//        }
//        if (message != null) {
//            new UniversalDialog.Builder(activity)
//                    .setMessageText(message)
//                    .setButtonText("取消")
//                    .setButtonListener(dialog -> {
//                        if (isNeedExit) {
//                            activity.finish();
//                        }
//                    })
//                    .setAnotherButtonText("去设置")
//                    .setAnotherButtonTextColor(Color.parseColor("#FF642E"))
//                    .setAnotherButtonListener(dialog -> turnToSystemSetting(activity, isNeedExit))
//                    .create()
//                    .show();
//        }
//    }

    private static void turnToSystemSetting(Activity activity, boolean isNeedExit) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//        activity.startActivity(intent);
        if (isNeedExit) {
            activity.finish();
        }
    }

}
