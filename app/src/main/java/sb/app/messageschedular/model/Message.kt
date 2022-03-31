package sb.app.messageschedular.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import sb.app.messageschedular.enums.MessageStatus
import sb.app.messageschedular.util.SearchDisplay


interface  Messenger{

}


enum class SmsStatus{
    FAIL,
    PASS,
    PENDING

}




@Parcelize
@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true )
    var Id :Long =0,
    @Embedded
    var messages: Messages,
    val smsId : Long ,

    @Embedded
    var userInfo : UserInfo,

    var messageStatus: MessageStatus = MessageStatus.PENDING

    ) : Parcelable


@Parcelize
data class UserInfo(

    var contactId :Int,
    var  name  : String,
    var phone  : String,
    var userId : Int) : Parcelable








@Parcelize
data class Sms(

    @Embedded
    var messages: Messages,

    @Relation(
        parentColumn = "messageId",
        entityColumn = "messageId")
    var userList  :  List<Contact> = emptyList() ) : Parcelable ,Messenger








@Parcelize
@Entity
data class Messages(

    @PrimaryKey(autoGenerate = false)
     var messageId :Long =0,
    var message : String ="",

    @Embedded
    var time : Time =Time(),
    var date: Long =0 ,

    @Embedded
    var error : Error =Error()) : Parcelable{
//
//    fun getDisplaySearch(): SearchDisplay = when {
//        !isFocused && searchInput.isEmpty() -> SearchDisplay.Nothing
//        isFocused && searchInput.isEmpty() -> SearchDisplay.Nothing
//
//        else -> SearchDisplay.Results }

    }


@Parcelize
data class Error( var reason : String ="" ):Parcelable








//var smsStatus :SmsStatus?=null