package sb.app.messageschedular.repo


import kotlinx.coroutines.flow.Flow
import sb.app.messageschedular.contactHelper.ContactHelper
import sb.app.messageschedular.database.MessageDatabase
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Message
import javax.inject.Inject

class MessageRepository @Inject constructor(private  val contact : ContactHelper ,
                                            private val messageDatabase: MessageDatabase
                                            ) : BaseRepository() {


    suspend  fun getContacts(text:String): Flow<List<Contact>> {
     return   contact.query(text) }

     fun getUserList():Flow<List<Message>> {

         println("Flow Thread"+Thread.currentThread().name)
      return    messageDatabase.smsDao().getMessageList()
     }

}