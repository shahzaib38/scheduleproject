package sb.app.messageschedular.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Contact(

    @PrimaryKey(autoGenerate = true)
    var contactId :Int =0 ,
    var  name :String="",
    var phone :String="",

    var messageId:Long =0
    ,
    var smsId : Int =0):Parcelable

