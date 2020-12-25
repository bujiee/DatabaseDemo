package com.bujie.permission.core.permisson;

public interface IPermission {

    void onPermissionGranted(int requestCode);

    void onPermissionDenied(int requestCode, String[] permission);

    void onPermissionDeniedAndNotHint(int requestCode, String[] permission);

}
