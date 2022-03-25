package sb.app.messageschedular.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SelectedItemDataBinding
import sb.app.messageschedular.model.Contact


class SelectedViewHolder(var context :Context ,var binding: SelectedItemDataBinding ,
                         onClick :(Contact)->Unit) : RecyclerView.ViewHolder(binding.root){


    val animation = android.view
        .animation
        .AnimationUtils
        .loadAnimation(context , R.anim.horizontal_slide_in)

    init {
        binding.closeId.setOnClickListener {
            val contact =  binding.contact
            if(contact!=null) {
                onClick.invoke(contact) } }
    }


    fun bind(contact : Contact){
    //    mBinding.root.startAnimation( animation)

        binding.contact = contact


    }



}