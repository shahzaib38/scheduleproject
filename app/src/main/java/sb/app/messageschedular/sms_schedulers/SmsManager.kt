package sb.app.messageschedular.sms_schedulers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.service.SmsService

class SmsManager(private val applicationContext: Context) {


//  private  val smsManager  :SmsManager =  SmsManager.getDefault()

//
//   fun  sendSms(sms : Sms){
//
//        val sentIntent = Intent(SmsService.SENT)
//        val deliveryIntent = Intent(SmsService.DELIVERED)
//        val sentPI = PendingIntent.getBroadcast(applicationContext ,0,  sentIntent,0)
//        val deliveryPI = PendingIntent.getBroadcast(applicationContext ,0,  deliveryIntent,0)
//
//        sms.userList.forEach {
//            smsManager.sendTextMessage(it.phone ,
//                null ,
//                sms.messages.message ,
//                sentPI ,
//                deliveryPI ) }
//   }

}