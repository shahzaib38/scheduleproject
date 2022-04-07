package sb.app.messageschedular.adapter

import android.telephony.SubscriptionInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SmsItemDataBinding
import sb.app.messageschedular.databinding.SubscriptionItemDataBinding
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.viewHolder.ContactViewHolder
import sb.app.messageschedular.viewHolder.SubscriptionInfoViewHolder
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel



class AsyncSubscriptionnDiffCallBack  : DiffUtil.ItemCallback<SubscriptionInfo>(){

    override fun areItemsTheSame(oldItem: SubscriptionInfo, newItem: SubscriptionInfo): Boolean {

        return    oldItem.subscriptionId ==newItem.subscriptionId

    }

    override fun areContentsTheSame(oldItem: SubscriptionInfo, newItem: SubscriptionInfo): Boolean =
        when {
            oldItem.subscriptionId !=newItem.subscriptionId ->{
                false }


            else ->{

                true

            }

        }

}



class SubscriptionInfoAdapter(val messageScheduleViewModel: MessageScheduleViewModel) : ListAdapter<SubscriptionInfo, SubscriptionInfoViewHolder>(AsyncSubscriptionnDiffCallBack()) {

    companion object{
        const val CONTACT =1 }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscriptionInfoViewHolder {


        val inflator =  LayoutInflater.from(parent.context)
        val binding  =   DataBindingUtil.inflate<SubscriptionItemDataBinding>(inflator , R.layout.sim_item ,parent ,false )

        return         SubscriptionInfoViewHolder(binding , onClick =
        {it ->

//            messageScheduleViewModel.addContact(it )

            messageScheduleViewModel.subscriptionInfoDetail(getItem(it))

        }
        )





    }


    override fun onBindViewHolder(holder: SubscriptionInfoViewHolder, position: Int) {
        val channel = getItem(position)
        holder.bind(channel)

    }



}