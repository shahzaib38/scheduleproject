package sb.app.messageschedular.util

import android.util.Log
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import java.util.*
import kotlin.collections.HashMap

object  CollectionUtils {
     fun removeSpecificIndex(message  : Message, sms : Sms):List<Contact>{

        val  contactList  = sms.userList

        //New ArrayList Size
        val newArraySize = sms.userList.size -1

        // New ArrayList
        val newArray = Array<Contact>(newArraySize){ Contact() }

        //Loop Contact List
        var k = 0
        for(i:Int in contactList.indices step 1){

            val contact  =    contactList[i]
            if(contact.smsId == message.userInfo.userId){
//                Log.i("Delete","smsID ${contact.smsId}    +userId ${message.userInfo.userId}")
                continue }

            newArray[k++] =  contact }

        return newArray.toList() }



    /************ Deleted Smsm *****/
     fun deletedSms(priorityQueue: Queue<Sms>):HashMap<Long,Sms>{
        val deletedMap     : HashMap<Long ,Sms> =HashMap<Long,Sms>()
        priorityQueue.forEach { sms ->
            deletedMap.put(sms.messages.messageId ,sms)}

        return    deletedMap }


}