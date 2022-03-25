package sb.app.messageschedular.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

object Permissions {

    const val READ_CONTACT_CODE =1213
    val cameraPermissions = arrayListOf(Manifest.permission.READ_CONTACTS)


    fun AppCompatActivity.customCheckSelfPermission(permission :String):Int = ActivityCompat.checkSelfPermission(this, permission)

    fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission:String )=
        ActivityCompat.shouldShowRequestPermissionRationale(this,permission)

    fun AppCompatActivity.requestPermissionCompat(permission:Array<String> , requestCode :Int )=
         ActivityCompat.requestPermissions(this ,permission ,requestCode)

}