package sb.app.messageschedular.sms_schedulers

import com.google.common.truth.Truth
import io.mockk.mockk

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.exceptions.SmsDeQueueException
import sb.app.messageschedular.model.*
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.util.CollectionUtils
import java.util.*
import kotlin.collections.ArrayList


@RunWith(MockitoJUnitRunner::class)
class SmsSchedulerImplTest {




    lateinit var smsSchedulerImpl: SmsScdeduler


    @Before
    fun setUp() {

//
//    val smsService =    mockk<SmsService>()
//     smsSchedulerImpl  = SmsScdeduler.getInstance(smsService)
//


    }

    @Test
    fun Enqueue_Sms_Test(){

        val message =Messages(messageId =121 , message = "hello how are you",time= Time(hours=12, minutes = 14, meridiem = Meridiem.AM ,true ))

        val userList =ArrayList<Contact>()

        val contact1= Contact(contactId =1,name ="shahzaib" , phone = "03122189474",0)
        val contact2= Contact(contactId =2,name ="shahzaib" , phone = "03122189474",1)

        userList.add(contact1)
        userList.add(contact2)

        val sms =Sms(messages = message,userList =userList )

        val isAdded =  smsSchedulerImpl.enqueue(sms = sms )


        Truth.assertThat(isAdded).isTrue()

    }

    @Test(expected = SmsDeQueueException::class)
    fun dequeue_Sms_Test(){

    }






    @Test
    fun test_remove_specific_index_from_sms(){

        val USER_NAME_1 ="Shahzaib"
        val USER_NAME_2 ="Abu"

    var message =    Message(
             Id  = 108,
             messages = Messages(messageId = 111 , message="test message",
                 time =Time(hours = 12, minutes = 24, meridiem = Meridiem.AM,is24Hours = true)),
                  smsId =0 ,
                userInfo = UserInfo(contactId = 1,name =USER_NAME_1, phone = "+923122189474" ,userId=0),
                  messageStatus = MessageStatus.PENDING)

        val contact1 =Contact(contactId = 1 ,name =USER_NAME_1,phone="+923122189474" , messageId =111 , smsId =0 )
        val contact2 =Contact(contactId = 1 ,name =USER_NAME_2,phone="+923222527025" , messageId = 112 , smsId =1 )


        val messages =Messages(
            messageId =111,
             message ="This is test",
            time = message.messages.time,
            date =message.messages.date ,
            error=Error())

        val sms =Sms(messages = messages , listOf(contact1 ,contact2))

      val contactList =  CollectionUtils.removeSpecificIndex(message ,sms )

        Truth.assertThat(contactList.size).isEqualTo(1)
    }


    /************** name Of Deleted user *************************/

    @Test
    fun test_name_of_removed_message_from_sms(){

        val USER_NAME_1 ="Shahzaib"
        val USER_NAME_2 ="Abu"

        val message =    Message(
            Id  = 108,
            messages = Messages(messageId = 111 , message="test message",
                time =Time(hours = 12, minutes = 24, meridiem = Meridiem.AM,is24Hours = true)),
            smsId =0 ,
            userInfo = UserInfo(contactId = 1,name =USER_NAME_1, phone = "+923122189474" ,userId=0),
            messageStatus = MessageStatus.PENDING)

        val contact1 =Contact(contactId = 1 ,name =USER_NAME_1,phone="+923122189474" , messageId =111 , smsId =0 )
        val contact2 =Contact(contactId = 1 ,name =USER_NAME_2,phone="+923222527025" , messageId = 112 , smsId =1 )


        val messages =Messages(
            messageId =111,
            message ="This is test",
            time = message.messages.time,
            date =message.messages.date ,
            error=Error())

        val sms =Sms(messages = messages , listOf(contact1 ,contact2))

       val deletedSms =    CollectionUtils.removeSpecificIndex(message,sms )



        Truth.assertThat(deletedSms[0].smsId ).isEqualTo(1)


    }



    @Test
    fun test_HashMap_List_Update_size(){

        //Sms 1

        val USER_NAME_1 ="Shahzaib"
        val USER_NAME_2 ="Abu"
        val USER_NAME_3 ="Home"
        val USER_NAME_4 ="MOM"

        val message =    Message(
            Id  = 108,
            messages = Messages(messageId = 111 , message="test message",
                time =Time(hours = 12, minutes = 24, meridiem = Meridiem.AM,is24Hours = true)),
            smsId =0 ,
            userInfo = UserInfo(contactId = 1,name =USER_NAME_1, phone = "+923122189474" ,userId=0),
            messageStatus = MessageStatus.PENDING)

        val contact1 =Contact(contactId = 1 ,name =USER_NAME_1,phone="+923122189474" , messageId =111 , smsId =0 )
        val contact2 =Contact(contactId = 2 ,name =USER_NAME_2,phone="+923222527025" , messageId = 112 , smsId =1 )


        val messages =Messages(
            messageId =111,
            message ="This is test",
            time = message.messages.time,
            date =message.messages.date ,
            error=Error())

        val sms =Sms(messages = messages , listOf(contact1 ,contact2))


        val messages1 =Messages(
            messageId =112,
            message ="This is test",
            time = message.messages.time,
            date =message.messages.date ,
            error=Error())



        val contact3 =Contact(contactId = 3 ,name =USER_NAME_3,phone="+923122189474" , messageId =111 , smsId =0 )
        val contact4 =Contact(contactId = 4 ,name =USER_NAME_4,phone="+923222527025" , messageId = 112 , smsId =1 )


        val sms2 =Sms(messages = messages1 , listOf(contact3 ,contact4))



        val priorityQueue :Queue<Sms> =     LinkedList<Sms>()

        priorityQueue.offer(sms)
        priorityQueue.offer(sms2)
        val hashMap =    CollectionUtils.deletedSms(priorityQueue)
        hashMap.put(113 ,sms )
        Truth.assertThat(hashMap.size).isEqualTo(2)

    }




    @After
    fun tearDown() {
    }



}