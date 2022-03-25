package sb.app.messageschedular.viewHolder

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.databinding.MessageItemDataBinding
import sb.app.messageschedular.model.Message

import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.viewmodel.MessageListViewModel
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


class SmsViewHolder(val context : Context
                , val binding: MessageItemDataBinding ,
                    val messageScheduleViewModel: MessageListViewModel,
                    val onChecked :(Int  ,Boolean ,CompoundButton)->Unit,
                    onShowDialog:(Int)->Unit


                    ) : RecyclerView.ViewHolder(binding.root){

    init {


        binding.viewModel = messageScheduleViewModel

        binding.selectId.setOnCheckedChangeListener { buttonView, isChecked ->

            onChecked.invoke(this.bindingAdapterPosition, isChecked, buttonView)


        }

        binding.root.setOnClickListener {

            onShowDialog(this.bindingAdapterPosition)
        }


    }



    fun bind(sms : Message,isVisible :Boolean ){
        binding.message =  sms
        binding.selectId.visibility = if(isVisible)View.VISIBLE else  View.GONE


    }




}