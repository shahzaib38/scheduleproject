package sb.app.messageschedular.broadcast_reciever

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import sb.app.messageschedular.database.MessageDatabase
import sb.app.messageschedular.database.SmsDatabase
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.workmanager.SchedulerWorker
import kotlin.concurrent.thread

class SendBroadCast : BroadcastReceiver() {





    @SuppressLint("RestrictedApi")
    override fun onReceive(context: Context?, intent: Intent?) {



        when(resultCode){
            Activity.RESULT_OK ->{
                if(intent!=null && context!=null ) {
                    val messageId = intent.getLongExtra(SmsService.MESSAGE_ID, 0)
                    val userId = intent.getIntExtra(SmsService.USER_ID, 0)

                    Log.i("SchedulerWorker","messageId ${messageId}")
                    Log.i("SchedulerWorker","userId ${userId}")


                    val workManager =  WorkManager.getInstance(context!!)
                    val oneTimeRequest =OneTimeWorkRequest.Builder(SchedulerWorker::class.java)

                    val data = Data.Builder()
                    data.put("messageId",messageId)
                    data.put("userId",userId)
                    data.put("messageStatus",0)


                    oneTimeRequest.setInputData(data.build())

                    workManager.enqueue(oneTimeRequest.build())



                    //if(context!=null) {
//                        update(
//                            messageId =messageId ,
//                            userId = userId,
//                            messageStatus = MessageStatus.SENT,
//                            context = context!!)
//

                        Log.i("SmsSchedulerImpl" ,"Update")
                  //  }
                }
            }

            else ->{

                if(intent!=null && context !=null ){


                    val messageId = intent.getLongExtra(SmsService.MESSAGE_ID, 0)
                    val userId = intent.getIntExtra(SmsService.USER_ID, 0)

                    Log.i("SchedulerWorker","messageId ${messageId}")
                    Log.i("SchedulerWorker","userId ${userId}")


                    val workManager =  WorkManager.getInstance(context!!)
                    val oneTimeRequest =OneTimeWorkRequest.Builder(SchedulerWorker::class.java)

                    val data = Data.Builder()
                    data.put("messageId",messageId)
                    data.put("userId",userId)
                    data.put("messageStatus",1)


                    oneTimeRequest.setInputData(data.build())

                    workManager.enqueue(oneTimeRequest.build())


















                }


            }

        }


    }

    @SuppressLint("RestrictedApi")
    private fun update(messageId :Long,
                       context : Context,
                       userId :Int,
                       messageStatus: MessageStatus
    ){


//        thread(start = true) {
//            Log.i("SmsSchedulerImpl" ,"")
//            println(context)
//
//            MessageDatabase.getInstance(context)
//                .smsDao().update(
//                    messageId = messageId,
//                    userId = userId,
//                    status = messageStatus
//                )
//
//        }


    }


}