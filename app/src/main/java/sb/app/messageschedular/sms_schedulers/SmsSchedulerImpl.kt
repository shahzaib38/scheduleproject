package sb.app.messageschedular.sms_schedulers

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.os.CountDownTimer
import android.telephony.SmsManager
import sb.app.messageschedular.database.SmsDatabase
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.exceptions.SmsEnQueueException
import sb.app.messageschedular.model.*
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.sms_schedulers.SmsScdeduler.Companion.ONE_SECOND
import sb.app.messageschedular.util.DateUtils
import java.util.*
import kotlin.NullPointerException
import kotlin.collections.HashMap
import kotlin.concurrent.thread

interface SmsScdeduler {

   // fun add(sms: Sms):Boolean
    fun schedule(sms: Sms)
    fun update(messageId : Long,userId :Int ,status : MessageStatus)
    fun onUnScheduled()
    fun getSentMessages():Map<Long ,Set<Int>>
    fun startCountDown(sms: Sms)
    fun delete(message: Message)
    fun removeToDataBase(message: Message)
    fun enqueue(sms :Sms):Boolean
    fun sendSms(sms:Sms)
    fun failedInsert(sms :Sms )


    companion object{
        const val ONE_SECOND =1000L

        @Volatile
        private var INSTANCE : SmsScdeduler?=null

     //   smsDatabase: SmsDatabase

        fun getInstance(smsService: SmsService) :SmsScdeduler = INSTANCE?: synchronized(this ){
            INSTANCE?:SmsSchedulerImpl(smsService ).also { INSTANCE =it  } }

    }


}


    //private val smsDatabase: SmsDatabase
class SmsSchedulerImpl constructor(
      private val   smsService : SmsService,
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
                    messageStatus = messageStatus
                )

                smsDatabase.smsDao().insertSms(message)

            }

        }
        }


    /********** Update Mesasge in Database ***?
     *
     */
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

                smsDatabase.smsDao().update(messageId = message.messages.messageId
                    , userId = userInfo.userId ,status =MessageStatus.FAILED

                    )

            }

        }
    }





    override fun schedule(sms: Sms) {


//       sendSms(sms)

          val  enqueued  =  enqueue(sms)

        if(enqueued) {

            val dequeueSms =  priorityQueue.peek()

            insertIntoDatabase(sms ,MessageStatus.PENDING)
           startCountDown(dequeueSms!!)

        }else{

            throw SmsEnQueueException(smsId = sms.messages.messageId,

                smsIndex = sms.messages.messageId
                ,
                message = sms.messages.message)

        }

    }

        override fun update(messageId: Long, userId: Int, status: MessageStatus) {

            thread(start = true) {

                smsDatabase.smsDao().update(
                    messageId = messageId, userId = userId, status = status

                )

            }
        }








        override fun startCountDown(sms :  Sms) {
        if(isCountDownStart){
            countDownTimer?.cancel()

           isCountDownStart = false

        } else {
            isCountDownStart =true }

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


    private fun getIndex(message :Message) : Int {
        var messageIndex  = Integer.MIN_VALUE

        priorityQueue.forEachIndexed { index, sms ->
            if(message.messages.messageId  ==  sms.messages.messageId){
                messageIndex = index } }

        return messageIndex }




    private fun removeSpecificIndex(message  : Message ,sms : Sms){

        println("remove Specific index")
          val index =         message.userInfo.userId

           val smsContactList = sms.userList

        val newArraySize = sms.userList.size -1
        val newArray = Array<Contact>(newArraySize){Contact()}

        var   k = 0

        for(i :Int in smsContactList.indices step 1){

            println("deleled index"+index  +"i"+i )

            if(index == i ){
                println("deleled index"+index)
                continue }

            newArray[k++]=smsContactList[i]
        }


        sms.userList =newArray.toList()

    }

    private fun printContact(sms : Sms){

        println("println Contact ")
        sms.userList.forEach {

            println("contact"+it.name)
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

    private fun processFirstIndex(message: Message ,sms :Sms ){

       val list = sms.userList

        if(list.size > 1 ){

            removeSpecificIndex(message , sms )
            removeToDataBase(message)

        }

        else{

            println("single contact ")

            dequeue(sms )

            removeToDataBase(message)
        }


    }


    override fun delete(message: Message) {

        if(!priorityQueue.isEmpty()){

         val index =   getIndex(message)

            if(index == 0){
                processFirstIndex(message , priorityQueue.first())
                println("is at first index")

            }else if(index == Integer.MIN_VALUE){
                println("not in queue ")
                removeToDataBase(message)

            }else{
                processFirstIndex(message , priorityQueue.elementAt(index))
            }



        }else{

            removeToDataBase(message)


        }




    }

    override fun removeToDataBase(message: Message) {
        thread(start = true) {
            smsDatabase.smsDao().deleteMessage(message)
        }
        }

    override fun sendSms(sms: Sms) {

//
//        val  set  = HashSet<Int>()

        sms.userList.forEachIndexed { index, contact ->
  //          println("Sent code "+index)

//            println("set"+set)

//            set.add(index)

//            sentMessages[sms.messages.messageId] = set






            val sentIntent = Intent(SmsService.SENT)

            sentIntent.putExtra(SmsService.MESSAGE_ID ,sms.messages.messageId)
            sentIntent.putExtra(SmsService.USER_ID , index)


            val deliveryIntent = Intent(SmsService.DELIVERED)


            val sentPI = PendingIntent.getBroadcast(smsService.applicationContext, index, sentIntent,  PendingIntent.FLAG_IMMUTABLE or   PendingIntent.FLAG_UPDATE_CURRENT)
            val deliveryPI =
                PendingIntent.getBroadcast(smsService.applicationContext, 0, deliveryIntent,  PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)



            try {

                smsManager.sendTextMessage(
                    contact.phone,
                    null,
                    sms.messages.message,
                    sentPI,
                    deliveryPI)


            } catch (e: IllegalArgumentException) {

                println("illegal argument exception "+index)


                update(messageId = sms.messages.messageId,

                    userId = index
                   ,status = MessageStatus.FAILED)

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

                    smsDatabase.smsDao().insertSms(message)

                }

            }

        }


        private fun resetCounter(sms: Sms){

            isCountDownStart = true


        smsService.notify(sms.messages.time , sms.messages.date)

        val timeLeft = DateUtils.countDownTime(sms)

        countDownTimer = object : android.os.CountDownTimer(timeLeft, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                val format = DateUtils.convert12HourFormat(millisUntilFinished)
                println("tick" + format) }

            override fun onFinish() {

                val sms =   priorityQueue.poll()


                println("before sms send ")
                /// error may occured here
                sendSms(sms)

                onUnScheduled()
            }

        }.start()
    }


        override fun onUnScheduled(){


            println("after sms send")

            if(!priorityQueue.isEmpty()){
                resetCounter(priorityQueue.peek()!!)
            }else{
                isCountDownStart =false
     //  smsService.finish()
            }

        }















     fun dequeue(sms : Sms ){
        priorityQueue.remove(sms)
        //updateDatabase(sms , MessageStatus.SENT)

        sendSms(sms)

         validateQueue()

     }

    /**********/


    private fun validateQueue(){
        if(!priorityQueue.isEmpty()){
            resetCounter(priorityQueue.peek()!!)
        }else{
            finishScheduling()
        }


    }




        private fun finishScheduling(){
        isCountDownStart =false
        smsService.finish()

        }

    }






/************** Rough ******************/



//
//            val added = priorityQueue.offer(null)
//
//            if (added) {
//                val prioritySms = priorityQueue.peek()
//
//                if (prioritySms != null) {
//                    startCountDown(prioritySms)
//                }
//
//            } else {
//            }
//






//        if(sms is Sms) {
//
//            val isAdded = add(sms)
//
//
//            if (isAdded) {
//
//                thread(start = true) {
//                    smsDatabase.smsDao().insertSms(sms.messages)
//                }
//
//                val topMessage =  getTop()
//                if(topMessage is Sms ) {
//                    startCountDown(topMessage) }
//
//            } else {
//                println("Not added text")
//
//
//            }
//        }






//    /**** Populate Queue with sms items ****/
//    private fun populateQueue(arrayList: List<Sms>) {
//        for (sms in arrayList) {
//            val isAdded = queue.offer(sms)
//            println(isAdded)
//
//        }
//    }


//    /**** sort SmsUiState Array*****/
//    fun List<Sms>.sorted() {
//        val arr: ArrayList<Sms> = this as ArrayList<Sms>
//
//        var i = 0
//        var j = 0
//        while (i < arr.size) {
//            var min = i
//            j = i + 1
//            while (j < arr.size) {
//                val jArray = DateUtils.formatSmsDate(arr[j])
//                val minArray = DateUtils.formatSmsDate(arr[min])
//                if (jArray.time < minArray.time) {
//                    min = j
//                }
//
//
//
//                j++
//            }
//
//            val temp = arr[min]
//            arr[min] = arr[i]
//            arr[i] = temp
//            i++
//        }
//    }

/**** if ine  add single sms in queue
 * else add it to queue then change queue into array to sort
 * clear the queue and populate new values
 */
//    override fun add(sms: Sms):Boolean  {
//
////        return  if (queue.isEmpty()) {
////            queue.offer(sms)
////        } else {
////            val add =     queue.offer(sms)
////            val arrayList :List<Sms> = ArrayList<Sms>(queue)
////            arrayList.sorted()
////            queue.clear()
////            populateQueue(arrayList)
////
////            add
////        }
//
//
//        return false
//    }



//        if(isAdded){
//
//            insertIntoDatabase(sms)
//
//        }
//        priorityQueue.element()

