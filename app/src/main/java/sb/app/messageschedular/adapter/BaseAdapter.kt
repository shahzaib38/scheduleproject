package sb.app.messageschedular.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.model.Contact



open class BaseViewHolder<out VDB : ViewDataBinding>( binding : VDB) : RecyclerView.ViewHolder(binding.root){

    val  mBinding :VDB =binding
}


//
//
//abstract class BaseAdapter<T: Contact, VH :RecyclerView.ViewHolder >(difUtils :AsyncDiffCallBack<T> =AsyncDiffCallBack<T>())  : ListAdapter< T,VH>(difUtils) {
//
//
//}