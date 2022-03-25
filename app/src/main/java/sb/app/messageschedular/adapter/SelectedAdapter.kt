package sb.app.messageschedular.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SelectedItemDataBinding
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.viewHolder.SelectedViewHolder
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


class AsyncSelectedDiffCallBack  : DiffUtil.ItemCallback<Contact>(){

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




class SelectedAdapter(val messageScheduleViewModel: MessageScheduleViewModel) : ListAdapter<Contact ,SelectedViewHolder>(AsyncSelectedDiffCallBack()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedViewHolder{
        val inflater =  LayoutInflater.from(parent.context)
        val selectedItemDataBinding =   DataBindingUtil.inflate<SelectedItemDataBinding>(inflater , R.layout.selected_item ,parent ,false )



        val viewHolder =    SelectedViewHolder(context  = parent.context ,selectedItemDataBinding){

            messageScheduleViewModel.removeContact(it)
        }

        return viewHolder }


    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        val channel = getItem(position)
        holder.bind(channel) }

}