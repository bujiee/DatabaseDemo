package com.bj.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bj.lib_permission_annotation.Defined;
import com.bj.lib_permission_annotation.Granted;
import com.bj.lib_permission_annotation.Permission;
import com.bj.lib_permission_annotation.Rationale;

@Permission
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Defined
    public void definedB(int code, String[] permissions) {
    }

    @Defined
    public void definedC(int ss, String[] permissions) {
    }

    @Granted
    public void grantedB(int code) {

    }

    @Rationale
    public void rationaleB(int code, String[] permission) {
    }
}