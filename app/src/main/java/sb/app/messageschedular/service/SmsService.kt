package sb.app.messageschedular.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.exceptions.SmsEnQueueException
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.sms_schedulers.SmsScdeduler
import sb.app.messageschedular.util.DateUtils


class SmsService  : Service() {

    private lateinit var smsScheduler :SmsScdeduler
    private var isServiceStarted  =false
    private   var    notificationBuilder :NotificationCompat.Builder?=null
    private var serviceScope = CoroutineScope(Dispatchers.Main)
    private var  notificationManager :NotificationManager?=null


    fun isServiceRunning():Boolean{
        return isServiceStarted }

    companion object{
       const  val MESSAGE_ID: String ="messageId"
        const val USER_ID:String ="userId"
        const val DELIVERED: String ="sb.app.messageschedular.sms_schedulers.DELIVERYKEY"
        const val ADD_SERVICE = "sb.spp.message_scheduler.add"
        const val DELETE_SERVICE ="sb.spp.message_scheduler.delete"
        const val Add_kEY ="ADD_KEY"
        const val SENT ="sb.app.messageschedular.sms_schedulers.SENT"
        private  const val channelId = "default_notification_channel_id"

    }



    /************* Sent BroaddCast **************/
    private val sentBroadcast =object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            println("Send Broadcast")




                    when(resultCode){
                        Activity.RESULT_OK ->{
                            if(intent!=null ) {
                                val messageId = intent.getLongExtra(MESSAGE_ID, 0)
                                val userId = intent.getIntExtra(USER_ID, 0)

                                val scheduleMessage = smsScheduler.getSentMessages()
                                println("Sucessfull message "  + messageId +"userId"+userId)


                                smsScheduler.update(

                                    messageId =messageId ,
                                    userId = userId,
                                    status = MessageStatus.SENT

                                )

                            }
                        }

                        else ->{

                            if(intent!=null){

                                val messageId = intent.getLongExtra(MESSAGE_ID, 0)
                                val userId = intent.getIntExtra(USER_ID, 0)


                                println("Failed")
                                smsScheduler.update(

                                    messageId =messageId ,
                                    userId = userId,
                                    status = MessageStatus.FAILED

                                )


                            }


                        }

                    }






            smsResponseCode(resultCode) } }


    /***** Sent BroadCast End *************/


    /************* Delivery BroaddCast **************/
    private val deliveryBroadcast =object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

            println("Delivery Broadcast")


            if(intent!=null ){
                val userContact =    intent.getParcelableExtra<Sms>("test")
                println("user contacct "+userContact!!.userList[0]) }

            smsResponseCode(resultCode) } }


    /************* Delivery BroaddCast ENd  **************/



    override fun onCreate() {
        super.onCreate()

        smsScheduler =  SmsScdeduler.getInstance(this )

        applicationContext.registerReceiver(sentBroadcast , IntentFilter(SENT))
        applicationContext.registerReceiver(deliveryBroadcast , IntentFilter(DELIVERED))





        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder  = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(sb.app.messageschedular.R.mipmap.ic_launcher)
            .setContentTitle("Message Scheduled")
            //  .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        isServiceStarted =true
        
     val action =   intent?.action

        when(action){
            ADD_SERVICE ->{

               val sms = intent.getParcelableExtra<Sms>(Add_kEY)
                if(sms!=null) {
                    schedule(sms) } }

            DELETE_SERVICE ->{  }
        }

        showNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {

         notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "test1", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "test otificaiton"
            notificationManager?.createNotificationChannel(channel)
        }

        this.startForeground(111 ,notificationBuilder!!.build())


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

        applicationContext.unregisterReceiver(sentBroadcast)
        applicationContext.unregisterReceiver(deliveryBroadcast)

    }


    fun schedule(sms: Sms) {
        println("Schedule Service ")


        serviceScope.launch {

            supervisorScope {

                    try {

                        println("IsServiceRUnngin "+isServiceStarted)


                        smsScheduler.schedule(sms)

                    } catch (smsEnqueueException : SmsEnQueueException) {

                            smsScheduler.failedInsert(sms)



                    }catch (e:Exception){
                        smsScheduler.failedInsert(sms)

                        println("Exception ")

                    }



            }

        }



    }

    fun finish() {
        println("service finished ")

        println("Stop  self")

        println("Stop foreground service")
        stopForeground(true )

        println("service state  false ")
        isServiceStarted =false
        stopSelf()
    }

    fun notify(time: Time? , date : Long? ) {

        println("notification time ${time} "+   "date"+   date)
        if(time != null && date !=null ){

         val formatDate  =   DateUtils.convertDateIntoFormat(date)
        val timeFormat =    String.format("Message will be sent at %d:%d  %s" ,time.hours ,time.minutes,formatDate )
            notificationBuilder?.setContentText(timeFormat)
            notificationManager?.notify(111 ,notificationBuilder?.build())
            //Notification Area

         }


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









}