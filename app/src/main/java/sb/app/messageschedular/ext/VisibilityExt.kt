package sb.app.messageschedular.ext

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.model.Message

@BindingAdapter("app:visibile")
fun selectAllVisibility(textView :TextView ,smsList :List<Message>? ){
    if(smsList!=null && smsList.isNotEmpty()){
        textView.visibility = View.VISIBLE }
    else { textView.visibility = View.GONE }
}