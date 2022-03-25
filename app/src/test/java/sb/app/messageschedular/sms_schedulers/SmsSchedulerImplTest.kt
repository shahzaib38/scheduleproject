package sb.app.messageschedular.sms_schedulers

import android.text.format.DateFormat
import com.google.common.truth.Truth
import io.mockk.every

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.exceptions.SmsDeQueueException
import sb.app.messageschedular.model.*
import sb.app.messageschedular.util.DateUtils
import java.util.*
import kotlin.collections.ArrayList


@RunWith(MockitoJUnitRunner::class)
class SmsSchedulerImplTest {




    lateinit var smsSchedulerImpl: SmsScdeduler


    @Before
    fun setUp() {
     smsSchedulerImpl  = SmsScdeduler.getInstance()



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
//
//        val message =Messages(messageId =121 , message = "hello how are you",time= Time(hours=12, minutes = 14, meridiem = Meridiem.AM ,true ))
//
//        val userList =ArrayList<Contact>()
//
//        val contact1= Contact(contactId =1 , name ="shahzaib" , phone = "03122189474",0)
//        val contact2= Contact(contactId = 2, name ="shahzaib" , phone = "03122189474",1)
//
//        userList.add(contact1)
//        userList.add(contact2)
//
//        val sms =Sms(messages = message,userList =userList )
//
//        smsSchedulerImpl.enqueue(sms)
//
//
//    val isNOtNutll =    smsSchedulerImpl.
//
//        Truth.assertThat(isNOtNutll).isNotNull()

    }
    object Demo{



            fun hello(value :String )="shahzaibb"

    }

    @Test
    fun demo(){

 io.mockk.mockkObject(DateUtils)
        io.mockk.every {  DateUtils.convertDateIntoFormat(123132) } returns "23"

    }


    @Test
    fun Test_Order_Of_Priority_Queue_Sms(){

           //DO

           val currentDate = System.currentTimeMillis()
           val message =Messages(date = currentDate , messageId =121 , message = "hello how are you",time= Time(hours=12, minutes = 18, meridiem = Meridiem.AM ,true ))

           val userList =ArrayList<Contact>()

           val contact1= Contact(contactId =1 , name ="shahzaib" , phone = "03122189474",0)

           userList.add(contact1)

           val sms1 =Sms(messages = message,userList =userList )

           val message2 =Messages(date = currentDate , messageId =122 , message = "hello how are you",time= Time(hours=12, minutes = 16, meridiem = Meridiem.AM ,true ))

           val userList2 =ArrayList<Contact>()

           val contact21= Contact(contactId =1 , name ="ahsan" , phone = "03122189474",0)

           userList2.add(contact21)

           val sms2 =Sms(messages = message2,userList =userList2 )

           smsSchedulerImpl.enqueue(sms1)
           smsSchedulerImpl.enqueue(sms2)

//        val sms =   smsSchedulerImpl.deque()
//
//           //When
//
//
//        DateUtils.formatSmsDate(sms)
//
//        Truth.assertThat(sms.messages.messageId).isEqualTo(121)



    }





    @After
    fun tearDown() {
    }



}