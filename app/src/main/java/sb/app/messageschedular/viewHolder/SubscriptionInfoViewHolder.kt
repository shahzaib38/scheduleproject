package sb.app.messageschedular.viewHolder

import android.telephony.SubscriptionInfo
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.databinding.SmsItemDataBinding
import sb.app.messageschedular.databinding.SubscriptionItemDataBinding
import sb.app.messageschedular.model.Contact

class SubscriptionInfoViewHolder(var binding: SubscriptionItemDataBinding, onClick :(position :Int )->Unit) : RecyclerView.ViewHolder(binding.root){

    init {

        binding.root.setOnClickListener {


      //      val subscriptionInfo =  binding.subscriptionInfo

      val position    =   absoluteAdapterPosition
   //         if(contact!=null) {
                onClick.invoke(position)
///            }

        }

    }


    fun bind(subscriptionInfo : SubscriptionInfo){
        binding.subscriptionInfo = subscriptionInfo }



}