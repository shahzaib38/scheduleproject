package sb.app.messageschedular.two_way_binding

import android.icu.text.DateFormat
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.flow.update
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.util.DateUtils
import sb.app.messageschedular.util.DateUtils.convertDateIntoFormat
import java.util.*


private fun constructTime(time: Time):String{

 return    if(time.is24Hours){

        String.format("%d:%d",time.hours ,time.minutes)

    }else{

        DateUtils.twelveHourFormat(hours = time.hours, time.minutes) }

}


@BindingAdapter("app:update")
fun updateDate(textView: TextView , date :Long?){



    if(date!=null) {
        textView.text = convertDateIntoFormat(date)
    }
}



@BindingAdapter("app:update")
fun updateTime(textView: TextView , time :Time?){

    if(time!=null ){
        val timeFormat = constructTime(time)
        textView.text=timeFormat
    }else{
         val is24Hours = android.text.format.DateFormat.is24HourFormat(textView.context)
        val timeFormat = if(is24Hours) {
            DateUtils.convert24HourFormat(System.currentTimeMillis())
        }else {
            DateUtils.convert12HourFormat(System.currentTimeMillis()) }

        textView.text =timeFormat
    } }