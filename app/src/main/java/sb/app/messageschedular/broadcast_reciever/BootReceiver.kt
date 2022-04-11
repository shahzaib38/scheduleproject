package sb.app.messageschedular.broadcast_reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import sb.app.messageschedular.workmanager.SmsWorkManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if(context!=null){
//
//            Log.i("Boot","Boor Started ")
//            val rebootIntent = Intent(context , SmsService::class.java)
//
//            intent?.action =SmsService.REBOOT
//            ContextCompat.startForegroundService(context ,rebootIntent)

            val workManager =  WorkManager.getInstance(context !!)
            val oneTimeRequest = OneTimeWorkRequest.Builder(SmsWorkManager::class.java)


            workManager.enqueue(oneTimeRequest.build())


        }


    }


}