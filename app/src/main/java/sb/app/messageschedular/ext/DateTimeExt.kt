package sb.app.messageschedular.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.model.Time
import sb.app.messageschedular.util.DateUtils


@BindingAdapter("app:updateDate" ,"app:updateTime")
fun dateAndTimeExt(textView : TextView , date :Long  ,time :Time){



    val dateString  = DateUtils.convertDateIntoFormat(date)

    val dateFormat = String.format("%s  %d:%d "  , dateString ,time.hours ,time.minutes)

    textView.text = dateFormat


}


@BindingAdapter("app:messageDate" ,"app:messageTime")
fun messageDateAndTime(textView : TextView , date :Long?  ,time :Time?){

    println("Time "+time )
    println("Date"+date )

    if(time!=null  && date!=null ) {

        val dateString = DateUtils.convertDateIntoFormat(date)

        val dateFormat = String.format("%s  %d:%d ", dateString, time.hours, time.minutes)

        textView.text = dateFormat
    }

}