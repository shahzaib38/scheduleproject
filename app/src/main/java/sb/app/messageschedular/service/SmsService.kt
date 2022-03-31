package sb.app.messageschedular.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.*
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.RoomSQLiteQuery.acquire
import kotlinx.coroutines.*
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.exceptions.SmsEnQueueException
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.sms_schedulers.SmsScdeduler
import sb.app.messageschedular.util.DateUtils


class SmsService  : Service() {
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var smsScheduler :SmsScdeduler
    private var isServiceStarted  = false
    private   var    notificationBuilder :NotificationCompat.Builder?=null
    private var serviceScope = CoroutineScope(Dispatchers.Main)
    private var  notificationManager :NotificationManager?=null


    fun isServiceRunning():Boolean{
        return isServiceStarted }

    companion object{
       const  val MESSAGE_ID: String ="messageId"
        const val USER_ID:String ="userId"
       // const val DELIVERED: String ="sb.app.messageschedular.sms_schedulers.DELIVERYKEY"
        const val ADD_SERVICE = "sb.spp.message_scheduler.add"

        const val REBOOT ="sb.app.message_scheduler.reboot"
        const val DELETE_SERVICE ="sb.spp.message_scheduler.delete"
        const val Add_kEY ="ADD_KEY"
     //   const val SENT ="sb.app.messageschedular.sms_schedulers.SENT"
        private  const val channelId = "default_notification_channel_id"
        private const val NOTIFICATION_ID =1995
    }


    override fun onCreate() {
        super.onCreate()


        smsScheduler =  SmsScdeduler.getInstance(this )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder  = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(sb.app.messageschedular.R.mipmap.ic_launcher)
            .setContentTitle("Message Scheduled")
            //  .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        Log.i("Boot","intent is ${intent?.action}")

        showNotification()

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        println("intent ${intent }")
        isServiceStarted =true

      if(intent!=null) {
          val action = intent?.action


          when(action){

              SmsService.ADD_SERVICE ->{
                  val sms = intent.getParcelableExtra<Sms>(Add_kEY)
                  schedule(sms!!)

              }

              else ->{

                  Log.i("Boot","Service boot ")
                  serviceScope.launch(Dispatchers.IO) {
                      smsScheduler.reSchedule()
                  }

              }


          }



      }else{





          serviceScope.launch(Dispatchers.IO) {
              smsScheduler.reSchedule()
          }


      }

        return START_STICKY }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {

         notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "test1", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "test otificaiton"
            notificationManager?.createNotificationChannel(channel)
        }

        this.startForeground(NOTIFICATION_ID ,notificationBuilder!!.build())


    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder() }



    inner class LocalBinder : Binder(){
        fun getService():SmsService = this@SmsService
    }



    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted =false
       if( serviceScope.isActive){
           println("Service Scope is cancelled ")
           serviceScope.cancel() }

    }


    fun schedule(sms: Sms) {
        println("Schedule Service ")


        serviceScope.launch(Dispatchers.IO) {

            supervisorScope {

                    try {

                        println("IsServiceRUnngin "+isServiceStarted)


                        smsScheduler.schedule(sms)

                    } catch (smsEnqueueException : SmsEnQueueException) {

                            smsScheduler.failedInsert(sms)

                    }catch (e:Exception){
                        smsScheduler.failedInsert(sms)

                        println("Exception ") }



            }

        }



    }

    fun finish() {
        smsScheduler.log("service finished ")

        smsScheduler.log("Stop  self")

        smsScheduler.log("service state  false ")
        isServiceStarted =false

        smsScheduler.log("Stop foreground service")
        stopForeground(true )

        stopSelf()
    }

    fun notify(sms : Sms) {
        val time =  sms.messages.time
        val formatTime =  DateUtils.constructTime(time)

         val formatDate  =   DateUtils.convertDateIntoFormat(sms.messages.date)
        val calenderFormat =    String.format("Message will be sent at %s  %s" ,formatTime ,formatDate )


        notificationBuilder?.setContentText(calenderFormat)

        notificationManager?.notify(NOTIFICATION_ID,notificationBuilder?.build())
    }

    fun delete(message: Message) {

        println("Delete is called ")
        if(isServiceRunning()) {

            smsScheduler.delete(message)

        }else{


            smsScheduler.removeToDataBase(message)

        }

    }
/****************/

private fun smsResponseCode(responseCode :Int ){

    when(responseCode){
        Activity.RESULT_OK ->{

            println("Sucessfull sent ")
        }

        SmsManager.RESULT_ERROR_GENERIC_FAILURE ->{
            println("RESULT_ERROR_GENERIC_FAILURE") }
        SmsManager.RESULT_ERROR_RADIO_OFF ->{
            println("RESULT_ERROR_RADIO_OFF")

        }
        SmsManager.RESULT_ERROR_NULL_PDU ->{
            println("RESULT_ERROR_NULL_PDU")

        }
        SmsManager.RESULT_ERROR_NO_SERVICE ->{
            println("RESULT_ERROR_NO_SERVICE")

        }
        SmsManager.RESULT_ERROR_LIMIT_EXCEEDED ->{
            println("RESULT_ERROR_LIMIT_EXCEEDED") }

        SmsManager.RESULT_ERROR_FDN_CHECK_FAILURE ->{
            println("RESULT_ERROR_FDN_CHECK_FAILURE")
        }
        SmsManager.RESULT_ERROR_SHORT_CODE_NOT_ALLOWED ->{
            println("RESULT_ERROR_SHORT_CODE_NOT_ALLOWED")


        }
        SmsManager.RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED ->{
            println("RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED")

        }
        SmsManager.RESULT_RADIO_NOT_AVAILABLE ->{
            println("RESULT_RADIO_NOT_AVAILABLE") }

        SmsManager.RESULT_NETWORK_REJECT ->{
            println("RESULT_NETWORK_REJECT")
        }
        SmsManager.RESULT_INVALID_ARGUMENTS ->{
            println("RESULT_INVALID_ARGUMENTS")


        }
        SmsManager.RESULT_INVALID_STATE ->{
            println("RESULT_INVALID_STATE")


        }
        SmsManager.RESULT_NO_MEMORY ->{
            println("RESULT_NO_MEMORY")

        }
        SmsManager.RESULT_INVALID_SMS_FORMAT ->{
            println("RESULT_INVALID_SMS_FORMAT") }


        SmsManager.RESULT_SYSTEM_ERROR ->{
            println("RESULT_SYSTEM_ERROR")
        }
        SmsManager.RESULT_MODEM_ERROR ->{

            println("RESULT_MODEM_ERROR")

        }
        SmsManager.RESULT_NETWORK_ERROR ->{

            println("RESULT_NETWORK_ERROR")

        }
        SmsManager.RESULT_ENCODING_ERROR ->{
            println("RESULT_ENCODING_ERROR")


        }
        SmsManager.RESULT_INVALID_SMSC_ADDRESS ->{
            println("RESULT_INVALID_SMSC_ADDRESS")


        }
        SmsManager.RESULT_OPERATION_NOT_ALLOWED ->{
            println("RESULT_OPERATION_NOT_ALLOWED")


        }
        SmsManager.RESULT_INTERNAL_ERROR ->{
            println("RESULT_INTERNAL_ERROR")

        }
        SmsManager.RESULT_NO_RESOURCES ->{
            println("RESULT_NO_RESOURCES")

        }
        SmsManager.RESULT_CANCELLED ->{
            println("RESULT_CANCELLED")

        }
        SmsManager.RESULT_REQUEST_NOT_SUPPORTED ->{}
        SmsManager.RESULT_NO_BLUETOOTH_SERVICE ->{}
        SmsManager.RESULT_INVALID_BLUETOOTH_ADDRESS ->{}
        SmsManager.RESULT_BLUETOOTH_DISCONNECTED ->{}
        SmsManager.RESULT_UNEXPECTED_EVENT_STOP_SENDING ->{}
        SmsManager.RESULT_SMS_BLOCKED_DURING_EMERGENCY ->{}
        SmsManager.RESULT_SMS_SEND_RETRY_FAILED ->{}
        SmsManager.RESULT_REMOTE_EXCEPTION ->{}
        SmsManager.RESULT_NO_DEFAULT_SMS_APP ->{}

        else->{

            println("unKnown")
        }

    }

}


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        println("root Intent ")
//        val restartServiceIntent = Intent(applicationContext, SmsService::class.java).also {
//            it.setPackage(packageName)
//        };
//        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        applicationContext.getSystemService(Context.ALARM_SERVICE);
//        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
//        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
//

    }



}