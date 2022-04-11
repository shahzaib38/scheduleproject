package sb.app.messageschedular.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.exceptions.SmsEnQueueException
import sb.app.messageschedular.model.Messages
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.sms_schedulers.SmsScdeduler
import sb.app.messageschedular.sms_schedulers.SmsSchedulerImpl
import sb.app.messageschedular.ui.actiivities.MainActivity
import sb.app.messageschedular.util.DateUtils
import sb.app.messageschedular.util.GsonUtils
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class SmsWorkManager(val context : Context, workerParameters:WorkerParameters) : CoroutineWorker(context  ,workerParameters) {




 //  private  val   smsScheduler =  SmsScdeduler.getInstance(context,this)
   private  val  notificationBuilder  = NotificationCompat.Builder(context , channelId)
   private  val   notificationManager  = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
   private var isRunning =false
    private var serviceScope = CoroutineScope(Dispatchers.Main)







    companion object {

        private const val channelId = "default_notification_channel_id"
        private const val NOTIFICATION_ID = 1995
        private const val SCHEDULE_NOTIFICATION = "Schedule"
        private const val SCHEDULE_NOTIFICATION_DESCRIPTION = "notification for scheduling sms"
    }
    override suspend fun getForegroundInfo(): ForegroundInfo {

        val intent = Intent( this.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context , 0, intent, 0)

        notificationBuilder.setOngoing(true)
          .setAutoCancel(false)
            .setSmallIcon(sb.app.messageschedular.R.mipmap.itremider)
            .setContentTitle("Message Scheduled")
            .setContentIntent(pendingIntent)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, SCHEDULE_NOTIFICATION, NotificationManager.IMPORTANCE_MIN)
            channel.description = SCHEDULE_NOTIFICATION_DESCRIPTION
            notificationManager?.createNotificationChannel(channel)
        }

        return ForegroundInfo(NOTIFICATION_ID,notificationBuilder.build())
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.Main){

        setForegroundAsync(getForegroundInfo())
//
//        println("Is RUnnung"+smsScheduler.getCountDownState())
//
//        if(smsScheduler.getCountDownState()){
//
//            smsScheduler.cancelCountDown()
//        }

        val data = inputData.getString("SMS")

        val sms =GsonUtils.deSerializeSms(data!!)

        Log.i("Smsss","${sms}")
        println("Thread "+Thread.currentThread().name)

            if (!isRunning) {

                println("Scheduler 1")



                isRunning= true
                suspendCoroutine<Unit> {continuation ->


                    launch(Dispatchers.IO) {

//                        smsScheduler.initializeScheduler(sms ,continuation)
                    //    smsScheduler.schedule(sms)
                    }

                }
                isRunning =false

//
//                smsScheduler.start()
//                synchronized(smsScheduler){
//
//                    smsScheduler.join()
//
//                }



                println("Scheduler 211111")
                 Result.success()

            } else {
                val messages=Messages(messageId = 122, message = "Shahzaib where are you", time = Time(hours = 16, minutes = 30, meridiem = Meridiem.AM,is24Hours = true)

                    , date = System.currentTimeMillis()
                )
                val sms = Sms(messages = messages)


                isRunning= true

                    launch {

                        //smsScheduler.initializeScheduler(sms ,continuation)
                            //smsScheduler.schedule(sms)
                    }



                 Result.failure()
            }



    }



    fun notify(sms : Sms) {
        val time =  sms.messages.time
        val formatTime =  DateUtils.constructTime(time)

        val formatDate  =   DateUtils.convertDateIntoFormat(sms.messages.date)
        val calenderFormat =    String.format("Message will be sent at %s  %s" ,formatTime ,formatDate )


        notificationBuilder?.setContentText(calenderFormat)

        notificationManager?.notify(NOTIFICATION_ID,notificationBuilder?.build())
    }

    fun schedule(sms: Sms) {
        println("Schedule Service ")



                try {

                 //   println("IsServiceRUnngin "+isServiceStarted)

                    serviceScope.launch {

                    //     smsScheduler.schedule(sms)

                     }
                } catch (smsEnqueueException : SmsEnQueueException) {

                //    smsScheduler.failedInsert(sms)

                }catch (e:Exception){
                   // smsScheduler.failedInsert(sms)

                    println("Exception ") }







    }



}