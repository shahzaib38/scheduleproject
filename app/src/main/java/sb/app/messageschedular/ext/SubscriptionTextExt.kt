package sb.app.messageschedular.ext

import android.telephony.SubscriptionInfo
import android.widget.TextView
import androidx.databinding.BindingAdapter
import sb.app.messageschedular.R




@BindingAdapter("app:subscription")
fun simSubscriptionText(textView : TextView, subscriptionInfo: SubscriptionInfo?){

    println("Sucriptioa asdadadsas"+subscriptionInfo)
    if(subscriptionInfo!=null){

        textView.text =subscriptionInfo.displayName.toString()

    }else{

   val no_sim =     textView.context.resources.getString(R.string.no_sim)
        textView.text =no_sim

    }

}








@BindingAdapter("app:change_sim")
fun simChangeText(textView : TextView, subscriptionInfo: SubscriptionInfo?){

    if(subscriptionInfo!=null){

        val no_sim =     textView.context.resources.getString(R.string.change_sim)
        textView.text =no_sim
    }else{

        val no_sim =     textView.context.resources.getString(R.string.select_sim)
        textView.text =no_sim

    }

}