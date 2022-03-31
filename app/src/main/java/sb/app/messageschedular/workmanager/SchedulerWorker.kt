package sb.app.messageschedular.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import sb.app.messageschedular.database.MessageDao
import sb.app.messageschedular.database.MessageDatabase
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Error
import sb.app.messageschedular.model.Messages
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.sms_schedulers.SmsScdeduler

class SchedulerWorker(context: Context ,workerParameters: WorkerParameters) :  CoroutineWorker(context ,workerParameters){

  val messageDatabase =  MessageDatabase.getInstance(context = applicationContext)



    override suspend fun doWork(): Result= withContext(Dispatchers.IO) {


        println("Scedule Worker ")

     val messageId =   inputData.getLong("messageId" ,1L)
     val userId =   inputData.getInt("userId",0)
     val  messageStatus =  inputData.getInt("messageStatus",0)

        Log.i("SchedulerWorker","messageId ${messageId}")
        Log.i("SchedulerWorker","userId ${userId}")
        Log.i("SchedulerWorker","messageStatus ${messageStatus}")






        val status =when(messageStatus){
            0 ->MessageStatus.SENT
            else ->MessageStatus.FAILED}

        messageDatabase.smsDao().update(
                messageId = messageId,
                userId = userId,
                status = status
            )



        Result.success() } }