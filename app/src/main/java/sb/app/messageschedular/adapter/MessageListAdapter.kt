package sb.app.messageschedular.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.MessageItemDataBinding
import sb.app.messageschedular.enums.ListEnum
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.viewHolder.SmsViewHolder
import sb.app.messageschedular.viewmodel.MessageListViewModel

class AsyncSmsDiffCallBack  : DiffUtil.ItemCallback<Message>(){

    override fun areItemsTheSame(oldItem: Message, newItem : Message): Boolean

    {

        return    oldItem.messages.messageId == newItem.messages.messageId

    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean
      =    when {

        oldItem.messages.messageId  !=newItem.messages.messageId  ->{
                false }

            oldItem.userInfo.userId !=oldItem.userInfo.userId->{
                false }
            oldItem.userInfo.name !=newItem.userInfo.name ->{
                false }

            else ->{

                true

            }

        }

}

class MessageListAdapter constructor(private val messageScheduleViewModel: MessageListViewModel) : ListAdapter<Message , SmsViewHolder>(AsyncSmsDiffCallBack())  {

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private  var messageItemDataBinding :MessageItemDataBinding?=null
    private var isVisible :Boolean =false
    private var checkMap = HashMap<Int ,Message>()
    private var listStatus = ListEnum.SELECTALL


    fun getStatus():ListEnum = listStatus


    fun deleteAll(){
        if(checkMap.isEmpty()){
            selectAllStatus()
            return }



        val stringFormat =String.format("Are you sure you want to delete %d message?" ,  checkMap.size)
        materialAlertDialogBuilder.setMessage(stringFormat)

        materialAlertDialogBuilder.show()
    }


   private fun materialAlertDialog(context : Context , resources:Resources = context.resources):MaterialAlertDialogBuilder{

       val alertDialogBuilder =    MaterialAlertDialogBuilder(context)

           .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
           }
           .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->

               checkMap.forEach {
                       messageScheduleViewModel.delete(it.value) }
               checkMap.clear()
               selectAllStatus()
           }

       return    alertDialogBuilder }

    fun selectAll(){

        if(itemCount<=0)return

        unselectStatus()
      //  notifyDataSetChanged()

        notifyItemRangeChanged(0,itemCount)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
         messageItemDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.sms_items,
            parent,
            false)

     materialAlertDialogBuilder =     materialAlertDialog(parent.context)

      val viewHolder =  SmsViewHolder(context = parent.context ,
          messageItemDataBinding!! ,messageScheduleViewModel ,
      onShowDialog = {

          messageScheduleViewModel.showDialog(getItem(it ))
      }
      , onChecked = { itemPosition ,isChecked ,compoundButton  ->

                  checkStatus(isChecked,itemPosition)



          }    )




        return viewHolder
    }

    private fun checkStatus(isChecked: Boolean ,itemPosition : Int ) {

        if(isChecked){
            checkMap.put(itemPosition ,getItem(itemPosition)!!)
        }else{
            checkMap.remove(itemPosition) }


        if(isChecked) {
            if (checkMap.size >= 0) {
                deleteStatus() }
                 }else{

            if (checkMap.size > 0) {

               deleteStatus()
            }else{

                selectAllStatus()
            }

        }

    }

    /******* Delete Status ******/
    private fun deleteStatus() {
        listStatus =ListEnum.DELETE
        isVisible = true
        messageScheduleViewModel.updateVisibililty(ListEnum.DELETE) }

    /******* UNSelect Status ******/
    private fun unselectStatus(){
        listStatus = ListEnum.UNSELECT
        isVisible = true
        messageScheduleViewModel.updateVisibililty(ListEnum.UNSELECT) }

    /******* Select All Status ******/
    private fun selectAllStatus(){
        listStatus =ListEnum.SELECTALL
        isVisible = false
        messageScheduleViewModel.updateVisibililty(ListEnum.SELECTALL)
       // notifyDataSetChanged()
        notifyItemRangeChanged(0,itemCount)

    }


    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val message = getItem(position)
        if(message!=null ) {
            holder.bind(message ,isVisible)
            messageItemDataBinding?.executePendingBindings() } }

    fun unselect() {
        selectAllStatus() }


}