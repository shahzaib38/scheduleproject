package sb.app.messageschedular.viewHolder

import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.databinding.SmsDataBinding
import sb.app.messageschedular.databinding.SmsItemDataBinding
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.ForYou


 class ContactViewHolder(var binding: SmsItemDataBinding ,onClick :(Contact)->Unit) : RecyclerView.ViewHolder(binding.root){

     init {

         binding.root.setOnClickListener {


           val contact =  binding.contact

             if(contact!=null) {
                 onClick.invoke(contact)
             }

         }

     }


     fun bind(contact : Contact){
             binding.contact = contact }



 }