package sb.app.messageschedular.ext

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.R
import sb.app.messageschedular.adapter.TodoAdapter
import sb.app.messageschedular.model.Todo
import sb.app.messageschedular.viewmodel.TodoViewModel


@BindingAdapter("app:todoViewModel","app:updateTodo")
fun updateTodo(recyclerView: RecyclerView ,todoViewModel: TodoViewModel ,list :List<Todo>?){

    val todoAdapter = TodoAdapter(todoViewModel)

    val mLayoutManager = LinearLayoutManager(
        recyclerView.context,
        LinearLayoutManager.VERTICAL,
        false)

    val itemDecorator = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)

    recyclerView.addItemDecoration(itemDecorator)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.adapter = todoAdapter


    if(list!=null && list.isNotEmpty()){


        todoAdapter.submitList(list )

    }else{



    }





}