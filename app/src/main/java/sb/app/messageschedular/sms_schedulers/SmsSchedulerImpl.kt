package sb.app.messageschedular.sms_schedulers

import android.app.PendingIntent
import android.content.Intent
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
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
import sb.app.messageschedular.util.CollectionUtils
import sb.app.messageschedular.util.DateUtils
import java.util.*
import kotlin.NullPointerException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

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
    companion object{
        const val ONE_SECOND =1000L
         var  TAG = SmsSchedulerImpl::class.java.name

        @Volatile
        private var INSTANCE : SmsScdeduler?=null

     //   smsDatabase: SmsDatabase

        fun getInstance(smsService: SmsService) :SmsScdeduler = INSTANCE?: synchronized(this ){
            INSTANCE?:SmsSchedulerImpl(smsService ).also { INSTANCE =it  } }

    }







}


    //private val smsDatabase: SmsDatabase
class SmsSchedulerImpl constructor(
        private val   smsService : SmsService ,
        private  val messageDatabase : MessageDatabase =  MessageDatabase.getInstance(smsService.applicationContext),
        private  val smsDatabase : SmsDatabase =  SmsDatabase.getInstance(smsService.applicationContext),
        private  val smsManager: SmsManager =  android.telephony.SmsManager.getDefault()
    ) : SmsScdeduler {


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
            sms.userList.forEachIndexed { index, contact ->

                println("UserInfo "+index)
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

                messageDatabase.smsDao().insertSms(message)
            } } }


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
                    , userId = userInfo.userId ,status =MessageStatus.FAILED

                    )

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
                smsService.finish() }
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

                        this.insertContact(sms.userList)
                        this.insertMessage(sms.messages)
                    }

                }


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

            thread(start = true) {

                messageDatabase.smsDao().update(
                    messageId = messageId, userId = userId, status = status

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




//
//
//
//    private fun removeSpecificIndex(message  : Message ,sms : Sms ):List<Contact>{
//
//        val  contactList  = sms.userList
//
//        //New ArrayList Size
//        val newArraySize = sms.userList.size -1
//
//        // New ArrayList
//        val newArray = Array<Contact>(newArraySize){Contact()}
//
//        //Loop Contact List
//        var k = 0
//        for(i:Int in contactList.indices step 1){
//
//        val contact  =    contactList[i]
//            if(contact.smsId == message.userInfo.userId){
//              Log.i("Delete","smsID ${contact.smsId}    +userId ${message.userInfo.userId}")
//                continue }
//
//          newArray[k++] =  contact }
//
//        return newArray.toList() }

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

            val smsDeletedList =  CollectionUtils.removeSpecificIndex(message ,deletedSms)

            deleteLog("remove list 1 ${deletedSms.userList.size}")

            if(smsDeletedList.isNotEmpty()) {

                updateHashMapAndPriorityQueue(
                    smsMap = smsMap ,
                    message =message ,
                    deletedSms = deletedSms ,
                    smsDeletedList =smsDeletedList)

                deleteContact(message)

                //Print Contact
                printContact(deletedSms)

                // Remove Message to Sms Database
                removeToDataBase(message)

            } else{
                dequeue(deletedSms)
                deleteSaeSmsInDb(deletedSms)
                removeToDataBase(message) }
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

            sms.userList.forEachIndexed { index, contact ->

                val sentIntent = Intent(smsService.applicationContext ,SendBroadCast::class.java)

                sentIntent.putExtra(SmsService.MESSAGE_ID, sms.messages.messageId)
                sentIntent.putExtra(SmsService.USER_ID, contact.smsId)


                val deliveryIntent = Intent(smsService.applicationContext ,PendingBroadCast::class.java)


                val sentPI = PendingIntent.getBroadcast(
                    smsService.applicationContext,
                    index,
                    sentIntent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                val deliveryPI =
                    PendingIntent.getBroadcast(
                        smsService.applicationContext,
                        0,
                        deliveryIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )



                try {

                    val messageList = smsManager.divideMessage(sms.messages.message)

                    messageList.forEach { it ->

                        smsManager.sendTextMessage(
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

                sendSms(sms)

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






    }



