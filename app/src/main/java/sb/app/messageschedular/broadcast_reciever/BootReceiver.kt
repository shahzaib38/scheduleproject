package sb.app.messageschedular.broadcast_reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import sb.app.messageschedular.service.SmsService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(context!=null){

            Log.i("Boot","Boor Started ")
            val rebootIntent = Intent(context , SmsService::class.java)

            intent?.action =SmsService.REBOOT
            ContextCompat.startForegroundService(context ,rebootIntent)

        }


    }


}