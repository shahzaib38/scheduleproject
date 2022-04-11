package sb.app.messageschedular.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.TodoItemDataBinding
import sb.app.messageschedular.model.Todo
import sb.app.messageschedular.viewHolder.TodoViewHolder
import sb.app.messageschedular.viewmodel.TodoViewModel

class AsyncTodoDiffCallBack  : DiffUtil.ItemCallback<Todo>(){

    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return    oldItem.item ==newItem.item }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean =
        when {
            oldItem.item !=newItem.item ->{
                false }
            else ->{
                true } } }

class TodoAdapter(private val todoViewModel: TodoViewModel) :   ListAdapter<Todo, TodoViewHolder>(AsyncTodoDiffCallBack())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater =  LayoutInflater.from(parent.context)
        val todoItemDataBinding =   DataBindingUtil.inflate<TodoItemDataBinding>(inflater , R.layout.todo_item ,parent ,false )
        val viewHolder =    TodoViewHolder(context  = parent.context ,todoItemDataBinding){
            todoViewModel.removeTodo(it) }
        return viewHolder }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val channel = getItem(position)
        holder.bind(channel,position) }


}