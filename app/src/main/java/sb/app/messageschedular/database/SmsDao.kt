package sb.app.messageschedular.database

import androidx.room.*
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Messages
import sb.app.messageschedular.model.Sms

@Dao
interface SmsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact : List<Contact>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messages : Messages)


    @Delete
    fun deleteMessage(message: Messages)

    @Delete
    fun deleteContacts(contact : List<Contact>)

    //@Delete
  //  fun deleteContact(contact : Contact)


    @Query("DELETE FROM contact WHERE   messageId =:messageId and smsId =:smsId")
    fun deleteContact(messageId:Long , smsId:Int  )

    @Transaction
    @Query("SELECT * FROM messages")
    fun getSmsList(): List<Sms>



    @Query("select exists(select * from messages where messageId = :messageId)")
    fun getMessage(messageId :Long) :Boolean


    //fun deletaAll()

    @Query("DELETE FROM messages")
    fun deletaAll()


}