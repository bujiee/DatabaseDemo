package com.bj.databasedemo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.OnClick
import com.bj.databasedemo.db.User
import com.bj.databasedemo.db.UserDatabase
import com.bj.lib_permission_annotation.Defined
import com.bj.lib_permission_annotation.Granted
import com.bj.lib_permission_annotation.Permission
import com.bj.lib_permission_annotation.Rationale
import com.bujie.permission.core.Tai
import kotlinx.coroutines.*

@Permission(
    reqCode = 1, permission = [Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ]
)
class MainActivity : BaseActivity() {

    private val scope by lazy {
        MainScope()
    }

    @BindView(R.id.tv_hello_world)
    lateinit var tv_hello_world: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Tai.get(this).requestPermission(
            10,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
        ButterKnife.bind(this)
    }

    private suspend fun doSo(db: UserDatabase) = coroutineScope {
        //update user data
        val user1 = User(123, "hello world")
        val user2 = User(321, "hello world")
        db.userDao()?.updateUsers(user1, user2)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    @Defined
    fun definedMain(code: Int, permissions: Array<String>) {
        showToast("用户权限被拒绝了")
    }


    @Defined
    fun definedA(ss: Int, permissions: Array<String>) {
    }

    @Granted
    fun granted(code: Int) {
        showToast("用户权限通过了")
    }

    @Rationale
    fun rationale(code: Int, permission: Array<String>) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("用户拒绝且不在询问")
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }


    fun camera(view: View?) {
        Tai.get(this).requestPermission(
            11,
            arrayOf(
                Manifest.permission.CAMERA
            )
        )
    }

    fun readWrite(view: View?) {
        Tai.get(this).requestPermission(
            12,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    fun audio(view: View?) {
        Tai.get(this).requestPermission(
            13,
            arrayOf(
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

}