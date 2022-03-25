package sb.app.messageschedular.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sb.app.messageschedular.enums.Meridiem
import java.util.*

@Parcelize
data class Time(var hours :Int=0,
                var  minutes :Int=0,
                val meridiem :Meridiem =Meridiem.AM,
                val is24Hours :Boolean = false ):Parcelable{


    companion object{


        fun createTime(hours :Int
                       , minutes :Int
                       , meridiem: Meridiem =Meridiem.AM
                       , is24Hours :Boolean  ):Time{

            return Time(hours ,minutes ,meridiem ,is24Hours) }


    }

}