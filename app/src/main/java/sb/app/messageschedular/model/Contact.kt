package sb.app.messageschedular.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(

    var contactId :Int =0 ,
    var  name :String="",
    var phone :String="",
    var smsId : Int =0):Parcelable

