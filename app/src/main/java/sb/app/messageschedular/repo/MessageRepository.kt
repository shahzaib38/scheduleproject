package sb.app.messageschedular.repo


import kotlinx.coroutines.flow.Flow
import sb.app.messageschedular.contactHelper.ContactHelper
import sb.app.messageschedular.database.SmsDatabase
import sb.app.messageschedular.model.Contact
import javax.inject.Inject

class MessageRepository @Inject constructor(private  val contact : ContactHelper ,
                                            private val smsDatabase: SmsDatabase
                                            ) : BaseRepository() {


    suspend  fun getContacts(text:String): Flow<List<Contact>> {
     return   contact.query(text) }

     fun getUserList() = smsDatabase.smsDao().getMessageList()


}