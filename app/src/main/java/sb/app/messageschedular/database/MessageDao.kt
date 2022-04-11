package sb.app.messageschedular.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSms(message : Message)


    @Transaction
    @Query("SELECT * FROM message ORDER BY Id DESC")
    fun getMessageList(): Flow<List<Message>>

    @Delete
   fun deleteMessage(message: Message)

    @Query("DELETE FROM message where messageId=:messageId")
    fun deleteMessage(messageId:Long )


    @Query("update message SET messageStatus =:status where messageId = :messageId ")
    fun update(status : MessageStatus , messageId : Long )
  //  and userId =:userId
    //   ,userId :Int

}