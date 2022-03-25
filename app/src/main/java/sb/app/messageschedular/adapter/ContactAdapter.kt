package sb.app.messageschedular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SmsItemDataBinding
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.viewHolder.ContactViewHolder
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel

class AsyncDiffCallBack  : DiffUtil.ItemCallback<Contact>(){

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {

     return    oldItem.contactId ==newItem.contactId

    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        when {
            oldItem.contactId !=newItem.contactId ->{
                false }
            oldItem.name !=newItem.name ->{
                false }
            oldItem.phone !=newItem.phone ->{
                false }
            else ->{

                false

            }

        }

}



class ContactAdapter(val messageScheduleViewModel: MessageScheduleViewModel) : ListAdapter<Contact ,ContactViewHolder>(AsyncDiffCallBack()) {

    companion object{
        const val CONTACT =1 }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder{


               val inflator =  LayoutInflater.from(parent.context)
               val smsItemDataBinding =   DataBindingUtil.inflate<SmsItemDataBinding>(inflator , R.layout.sms_item ,parent ,false )

       return         ContactViewHolder(smsItemDataBinding , onClick =
               {

                  messageScheduleViewModel.addContact(it )


               }
               )





    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val channel = getItem(position)
        holder.bind(channel)

     println("Current list"+   currentList)
    }



}