package sb.app.messageschedular.sms_schedulers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.accompanist.insets.rememberImeNestedScrollConnection
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import sb.app.messageschedular.broadcast_reciever.PendingBroadCast
import sb.app.messageschedular.broadcast_reciever.SendBroadCast
import sb.app.messageschedular.database.MessageDatabase
import sb.app.messageschedular.database.SmsDatabase
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.exceptions.SmsEnQueueException
import sb.app.messageschedular.model.*
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.sms_schedulers.SmsScdeduler.Companion.ONE_SECOND
import sb.app.messageschedular.sms_schedulers.SmsScdeduler.Companion.TAG
import sb.app.messageschedular.ui.actiivities.MainActivity
import sb.app.messageschedular.util.CollectionUtils
import sb.app.messageschedular.util.DateUtils
import sb.app.messageschedular.workmanager.SmsWorkManager
import java.util.*
import kotlin.NullPointerException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

interface SmsScdeduler {

   // fun add(sms: Sms):Boolean
    suspend  fun schedule(sms: Sms)
    fun update(messageId : Long,userId :Int ,status : MessageStatus)
    fun onUnScheduled()
    fun getSentMessages():Map<Long ,Set<Int>>
    suspend  fun startCountDown(sms: Sms)
    fun delete(message: Message)
    fun removeToDataBase(message: Message)
    fun enqueue(sms :Sms):Boolean
    fun sendSms(sms:Sms)
    fun failedInsert(sms :Sms )
    suspend  fun reSchedule()
    fun log(value :String)
    fun resetCounter(sms:Sms )
   fun  checkDatabase()
    suspend fun initializeScheduler(sms: Sms, continuation: Continuation<Unit>)
    fun getCountDownState() :Boolean
    fun cancelCountDown()


    companion object{
        const val ONE_SECOND =1000L
         var  TAG = SmsSchedulerImpl::class.java.name

        @Volatile
        private var INSTANCE : SmsScdeduler?=null

     //   smsDatabase: SmsDatabase

      //  ,smsWorkManager: SmsWorkManager
       // smsService: SmsService
        fun getInstance(context:Context ,smsService: SmsService ) :SmsScdeduler = INSTANCE?: synchronized(this ){
            INSTANCE?:SmsSchedulerImpl(context,smsService).also { INSTANCE =it  } }

    //    smsService
    }







}


// private val   smsService : SmsService ,
    //private val smsDatabase: SmsDatabase
class SmsSchedulerImpl constructor(

        private  val context: Context,
        private val smsService :SmsService,
        private  val messageDatabase : MessageDatabase =  MessageDatabase.getInstance(context.applicationContext),
        private  val smsDatabase : SmsDatabase =  SmsDatabase.getInstance(context.applicationContext),
        private  val smsManager: SmsManager =  android.telephony.SmsManager.getDefault()
    ) : SmsScdeduler {

    private var cancellableContinuation : Continuation<Unit>?=null

        companion object {
            var comparator = java.util.Comparator<Sms> { sms1O, sms2O ->

                val sms1 = DateUtils.formatSmsDate(sms1O)
                val sms2 = DateUtils.formatSmsDate(sms2O)

                when {
                    sms1.time < sms2.time -> {
                        -1

                    }
                    sms1.time > sms2.time -> {

                        1
                    }
                    else -> {
                        0

                    }
                }


            }
        }




    private var priorityQueue : Queue<Sms> =PriorityQueue<Sms>(comparator)

        private var isCountDownStart = false
        private var countDownTimer : CountDownTimer?=null
        private var sentMessages: HashMap<Long ,Set<Int>> = HashMap()


    override fun getCountDownState() :Boolean {
        return isCountDownStart
    }

    override fun cancelCountDown() {
        countDownTimer?.cancel()

        if(cancellableContinuation!=null){
            Log.i("State","${cancellableContinuation}")
            cancellableContinuation?.resumeWith(Result.success(Unit ))

        }
    }

    override fun getSentMessages() : Map<Long ,Set<Int>>  = sentMessages


     override   fun enqueue(sms :Sms):Boolean {

         sms.userList.forEach {


             Log.i("Enqueue" ,"userId ${it.smsId}")

         }
         log("enqueue")
      return   try {

          priorityQueue.add(sms )

        }catch (c : ClassCastException){

            throw SmsEnQueueException(smsId = sms.messages.messageId ,

                smsIndex = sms.messages.messageId
                     ,
            message = sms.messages.message)

        }catch (c : NullPointerException  ){

            throw SmsEnQueueException(smsId = sms.messages.messageId,

                smsIndex = sms.messages.messageId
                ,
                message = c.message!!)

        }catch (c : IllegalArgumentException  ){

            throw SmsEnQueueException(smsId = sms.messages.messageId ,

                smsIndex = sms.messages.messageId
                ,
                message = c.message!!)

        }catch (illegalStateException  : IllegalStateException  ){

           throw SmsEnQueueException(smsId = sms.messages.messageId ,

              smsIndex = sms.messages.messageId,
              message = "No Such Value found"!!)



        }

        }

















    private fun insertIntoDatabase(sms: Sms ,messageStatus: MessageStatus =MessageStatus.PENDING){

        thread(start = true) {
                val userInfo = UserInfo(
                    contactId =0 ,
                    name = "name",
                    phone = "phone",
                    userId = 1)

                val message = Message(
                    messages = sms.messages,
                    smsId = sms.messages.messageId,
                    userInfo = userInfo,
                    messageStatus = messageStatus)
            messageDatabase.smsDao().insertSms(message)

//            sms.userList.forEachIndexed { index, contact ->
//
//                println("UserInfo "+index)
//                val userInfo = UserInfo(
//                    contactId = contact.contactId,
//                    name = contact.name,
//                    phone = contact.phone,
//                    userId = index)
//
//                val message = Message(
//                    messages = sms.messages,
//                    smsId = sms.messages.messageId,
//                    userInfo = userInfo,
//                    messageStatus = messageStatus)
//
//                messageDatabase.smsDao().insertSms(message)
//            }
//

        }

    }


    /********** Update Mesasge in Database *****/
    private fun updateDatabase(sms: Sms , messageStatus: MessageStatus = MessageStatus.PENDING){

        thread(start = true) {
            sms.userList.forEachIndexed { index, contact ->

                val userInfo = UserInfo(
                    contactId = contact.contactId,
                    name = contact.name,
                    phone = contact.phone,
                    userId = index)


                val message = Message(
                    messages = sms.messages,
                    smsId = sms.messages.messageId,
                    userInfo = userInfo,
                    messageStatus = messageStatus)

                messageDatabase.smsDao().update(messageId = message.messages.messageId
                    ,status =MessageStatus.FAILED

                    )

                //, userId = userInfo.userId
            }

        }
    }


        override fun log(value :String ){
            Log.i(TAG ,value ) }

        override suspend fun  reSchedule(){
            log("Reschedule")

            val list = smsDatabase.smsDao().getSmsList()

                if(priorityQueue.isNotEmpty()){
                    priorityQueue.clear() }

                list.forEach {
                    priorityQueue.offer(it) }


            log("Log Size"+priorityQueue.size)











            priorityQueue.forEach {


                val sms = it
                sms.userList.forEach {
                    Log.i("Reschedule", "${it.name}")
                }

            }














            if(!priorityQueue.isEmpty()) {
                val sms = priorityQueue.peek()
                    schedule(sms)
            }else{
            //    smsService.finish()

            }
        }


        /*************** Delete Sms **************/

        private fun deleteSaeSmsInDb(sms : Sms ){

            log("Sms Deleted")
            log("Delete Sms In Db ")

            thread(start = true) {
                smsDatabase.smsDao().run {

                    sms.userList.forEach {
                    deleteContact(it.messageId ,it.smsId) }

                    this.deleteMessage(sms.messages)
                }


            }
        }

        /** Save Schedule Sms into Db *****/
        private fun saveScheduleSmsIntoDb(sms:Sms){



            log("save schedule sms into db"+sms.userList )

            thread(start = true){

              val  ifSmsExists   =smsDatabase.smsDao().getMessage(sms.messages.messageId)



                if(!ifSmsExists){

                    insertIntoDatabase(sms, MessageStatus.PENDING)
                    smsDatabase.smsDao().run {

                        this.insertContact(listOf())
                        this.insertMessage(sms.messages)
                    }

                }


        }


        }





        /************ Deleted Smsm *****/
        private fun getSmsMap(list: List<Sms>):HashMap<Long,Sms>{
            val deletedMap     : HashMap<Long ,Sms> =HashMap<Long,Sms>()


            list.forEach { sms ->
                deletedMap.put(sms.messages.messageId ,sms) }



            return    deletedMap
        }



    private fun resetSmsDelete(sms:Sms){


            messageDatabase.smsDao().run {
                this.deleteMessage(sms.messages.messageId) }



    }



   suspend  fun resetQueue(sms :Sms ){

       Log.i("listaaa","data aaaaa")

        val smsList =smsDatabase.smsDao().getSmsList()

        Log.i("listaaa","data ${smsList}")
       if(smsList.isNotEmpty()){

          val smsMap = getSmsMap(smsList)
           val containSms =      smsMap.get(sms.messages.messageId)
             if(containSms!=null){
                 deleteSaeSmsInDb(containSms)
                resetSmsDelete(sms )

             }


           if(smsList.isNotEmpty()){

               if (priorityQueue.isNotEmpty())priorityQueue.clear()

               smsList.forEach {
                   priorityQueue.offer(it ) }

               schedule(sms)

           }else{
               schedule(sms ) }
       }else{

           schedule(sms)
       }





    }


    override suspend  fun schedule(sms: Sms){
        log("Schedule")


          val  enqueued  =  enqueue(sms)

        if(enqueued) {

            val dequeueSms =  priorityQueue.peek()

            saveScheduleSmsIntoDb(sms)

           startCountDown(dequeueSms)

        }else{
            throw SmsEnQueueException(smsId = sms.messages.messageId,
                smsIndex = sms.messages.messageId,
                message = sms.messages.message) }


    }

        override fun update(messageId: Long, userId: Int, status: MessageStatus) {

         //   userId = userId,
            thread(start = true) {

                messageDatabase.smsDao().update(
                    messageId = messageId, status = status

                )

            }
        }








        override suspend fun startCountDown(sms :  Sms) =
            withContext(Dispatchers.Main) {


                    log("StartCountDown")


                    counterStatus()
                    resetCounter(sms)





            }


    /***** Delete Algorithm
     * if priority queue is empty
     * delete it from database
     *
     * else
     * val index  get index
     * check if
     * at first index
     *
     *
     *
     * else
     *
     */


    private fun printContact(sms : Sms){

        log("println Contact ")
        sms.userList.forEach {

            Log.i("Contact","contact"+it.name)
        }

    }



    /*** process if message is at first index
     *
     * if list.size >1
     *
     *
     *
     *
     * else
     *
     * **/


    private fun deleteContact(message :Message ){
        log("delete Contact")
        Log.i("deletCOntact" ,"userId ${message.userInfo.userId}")
        thread(start = true) {

            smsDatabase.smsDao().deleteContact(message.messages.messageId
                ,message.userInfo.userId)


        }
    }



        /************ Deleted Smsm *****/
        private fun deletedSms(priorityQueue: Queue<Sms>):HashMap<Long,Sms>{
            val deletedMap     : HashMap<Long ,Sms> =HashMap<Long,Sms>()


            priorityQueue.forEach { sms ->
                deletedMap.put(sms.messages.messageId ,sms) }



         return    deletedMap
        }


        private fun  updateHashMapAndPriorityQueue(smsMap : HashMap<Long,Sms > ,message:Message
        ,deletedSms :Sms,smsDeletedList : List<Contact>
        ){

            smsMap.put(
                message.messages.messageId,
                deletedSms.copy(userList = smsDeletedList))
            deleteLog("remove list 2 ${smsMap.get(message.messages.messageId)}")

            priorityQueue.clear()

            smsMap.forEach {
                priorityQueue.offer(it.value) }
        }


        private fun processDeletedSms(message:Message ,deletedSms :Sms  ,smsMap : HashMap<Long,Sms >){

//            val smsDeletedList =  CollectionUtils.removeSpecificIndex(message ,deletedSms)
//
//            deleteLog("remove list 1 ${deletedSms.userList.size}")
//
//            if(smsDeletedList.isNotEmpty()) {
//
//                updateHashMapAndPriorityQueue(
//                    smsMap = smsMap ,
//                    message =message ,
//                    deletedSms = deletedSms ,
//                    smsDeletedList =smsDeletedList)
//
//                deleteContact(message)
//
//                //Print Contact
//                printContact(deletedSms)
//
//                // Remove Message to Sms Database
//                removeToDataBase(message)
//
//            } else{
                dequeue(deletedSms)
                deleteSaeSmsInDb(deletedSms)
                removeToDataBase(message)

            //}
        }


        private fun deleteLog(value :String){

            Log.i("deleted" ,value )

        }


    override fun delete(message: Message) {

        deleteLog("delete call ")

        if (!priorityQueue.isEmpty()) {
            deleteLog("Queue is not empty ")

            val smsMap  =  deletedSms(priorityQueue)
            val deletedSms = smsMap.get(message.messages.messageId)


            if(deletedSms!=null) {
                deleteLog("deleted sms ${deletedSms}")

                deleteLog("deleted sms list ${deletedSms.userList}")

                processDeletedSms(
                    message = message,
                    deletedSms = deletedSms,
                    smsMap = smsMap)

            }else {

                deleteLog("deleted sms null ")
                removeToDataBase(message)
            }

        }else{

            removeToDataBase(message)


        }

    }

    override fun removeToDataBase(message: Message) {
        thread(start = true) {
            messageDatabase.smsDao().deleteMessage(message)
        }
        }

    override fun sendSms(sms: Sms) {

        log("send Sms ")


        thread(start =true ) {

            sms.userList.forEachIndexed { index, contact :Contact->

                val sentIntent = Intent(context.applicationContext ,SendBroadCast::class.java)

                sentIntent.putExtra(SmsService.MESSAGE_ID, sms.messages.messageId)
                sentIntent.putExtra(SmsService.USER_ID, contact.smsId)


                val deliveryIntent = Intent(context.applicationContext ,PendingBroadCast::class.java)


                val sentPI = PendingIntent.getBroadcast(
                    context.applicationContext,
                    index,
                    sentIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val deliveryPI =
                    PendingIntent.getBroadcast(
                        context.applicationContext,
                        0,
                        deliveryIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )



                try {

                    val messageList = smsManager.divideMessage(sms.messages.message)

                    messageList.forEach { it ->


                      //  smsManager.getSms
                        SmsManager.getSmsManagerForSubscriptionId(contact.subscriptionId).sendTextMessage(
                            contact.phone,
                            null,
                            it,
                            sentPI,
                            deliveryPI
                        )
                    }


                    deleteSaeSmsInDb(sms)


                } catch (e: IllegalArgumentException) {

                    println("illegal argument exception " + index)


                    update(
                        messageId = sms.messages.messageId,

                        userId = index, status = MessageStatus.FAILED
                    )

                }

            }


        }
    }





        override fun failedInsert(sms : Sms ) {
            thread(start = true) {
                sms.userList.forEachIndexed { index, contact ->

                    val userInfo = UserInfo(
                        contactId = contact.contactId,
                        name = contact.name,
                        phone = contact.phone,
                        userId = index)


                    val message = Message(
                        messages = sms.messages,
                        smsId = sms.messages.messageId,
                        userInfo = userInfo,
                        messageStatus = MessageStatus.FAILED)

                    messageDatabase.smsDao().insertSms(message)

                }

            }

        }


     override         fun resetCounter(sms: Sms){

            println("reset Counter")
            isCountDownStart = true


         smsService.notify(sms)


        val timeLeft = DateUtils.countDownTime(sms)

        countDownTimer = object : android.os.CountDownTimer(timeLeft, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                val format = DateUtils.convert12HourFormat(millisUntilFinished)
                println("tick" + format)


            }

            override fun onFinish() {

                log("Finish")
                val sms =   priorityQueue.poll()

                createNotification(sms)

           //     onUnScheduled()
                validateQueue()

            }

        }.start()
    }




        override fun onUnScheduled(){
            log("OnUnSchedule")
        }


        /********* Dequeue ***********/
        private fun dequeue(sms : Sms ){

     val isRemoved =  priorityQueue.remove(sms)

            log("Removed"+isRemoved)
         validateQueue()

     }


        /******** Status **************/
        private fun counterStatus(){
            if(isCountDownStart){
                countDownTimer?.cancel()

                isCountDownStart = false

            } else {
                isCountDownStart =true }
        }




    /*******Validate Qeueue  ***/
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun validateQueue(){
        log("Validate "+priorityQueue.size)

        if(!priorityQueue.isEmpty()){

              counterStatus()
              resetCounter(priorityQueue.peek()!!)

            }else{
            log("FInish scheduling")
            isCountDownStart =false
            countDownTimer?.cancel()
            smsService.finish()
//            cancellableContinuation?.let {
//                it.resumeWith(Result.success(Unit))
//            }


            }



    }

        override  fun checkDatabase(){

            thread(start = true) {
                val remainingSms = smsDatabase.smsDao().getSmsList()

                    if (!remainingSms.isEmpty()) {

                        remainingSms.forEach {
                            updateDatabase(it , messageStatus = MessageStatus.FAILED)

                        }

                        smsDatabase.clearAllTables()
//                        smsDatabase.smsDao().deletaAll()

                    }



            }
        }

    override suspend  fun initializeScheduler(sms: Sms, continuation: Continuation<Unit>) {
        cancellableContinuation =continuation

       // schedule(sms)
        resetQueue(sms)
    }




    /******************/

    private fun createNotification(sms:Sms  ){


        deleteSaeSmsInDb(sms)

        update(
            messageId = sms.messages.messageId,
            userId = 0, status = MessageStatus.SENT)


        val  notificationBuilder  = NotificationCompat.Builder(context , "channelIdd")
        val   notificationManager  = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val intent = Intent( context.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context , 0, intent, 0)

        notificationBuilder
            .setSmallIcon(sb.app.messageschedular.R.mipmap.itremider)
            .setContentTitle(sms.messages.title)
            .setContentIntent(pendingIntent)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("channelIdd", "notify", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Snooze Notify"
            notificationManager?.createNotificationChannel(channel)
        }


        notificationManager.notify(sms.messages.messageId.toInt() ,notificationBuilder.build())

    }


}



