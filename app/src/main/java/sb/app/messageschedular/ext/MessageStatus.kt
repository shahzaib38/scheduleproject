package sb.app.messageschedular.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.enums.MessageStatus

@BindingAdapter("app:updateStatus")
fun messageStatus(textView : TextView ,messageStatus : MessageStatus?){


    if(messageStatus!=null) {
        val resource = textView.context.resources

        when (messageStatus) {
            MessageStatus.PENDING -> {
                textView.text = resource.getString(R.string.pending)
                textView.setTextColor(resource.getColor(R.color.pending_color, null))
            }

            MessageStatus.SENT -> {
                textView.text = resource.getString(R.string.sent)
                textView.setTextColor(resource.getColor(R.color.sent_color, null))
            }

            MessageStatus.FAILED -> {
                textView.text = resource.getString(R.string.failed)
                textView.setTextColor(resource.getColor(R.color.failed_color, null))
            }
        }
    }

}