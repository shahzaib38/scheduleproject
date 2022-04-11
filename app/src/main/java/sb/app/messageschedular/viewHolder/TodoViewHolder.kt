package sb.app.messageschedular.viewHolder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.databinding.TodoItemDataBinding
import sb.app.messageschedular.model.Todo

class TodoViewHolder(var context : Context, var binding: TodoItemDataBinding,
                     onClick :(Int)->Unit) : RecyclerView.ViewHolder(binding.root){


    init {

        binding.delete.setOnClickListener {
            val position =absoluteAdapterPosition
                onClick.invoke(position) } }


    fun bind(todo : Todo ,position :Int ){
        val todoString = String.format("%d.  %s",position+1 ,todo.item)
        binding.itemId.text = todoString }



}