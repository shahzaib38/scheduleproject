package sb.app.messageschedular.viewmodel


import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import sb.app.messageschedular.array.ArrayUtils
import sb.app.messageschedular.model.Todo
import sb.app.messageschedular.navigator.TodoNavigator
import sb.app.messageschedular.repo.MessageRepository
import javax.inject.Inject

@HiltViewModel
class TodoViewModel
@Inject constructor(private val messageRepository: MessageRepository)

    : BaseViewModel<TodoNavigator>(messageRepository){



    private val _ITemText : MutableStateFlow<String> =MutableStateFlow("")
    val itemText =_ITemText


    private val _TodoList :MutableStateFlow<List<Todo>> = MutableStateFlow(value = emptyList<Todo>())

     val todoList =_TodoList.asLiveData()


   fun  addItem(value :String ){
       if(value.trim().isEmpty()){
           getNavigator().checkStatus()
           println("empty value ")
           return }

       _ITemText.update {""}
       getNavigator().checkStatus()
       _TodoList.update {
           val   list =ArrayList<Todo>()
           list.addAll(it )
           list.add(Todo(value.trim() ) )
          list }

   }


    fun removeTodo(it: Int){
        _TodoList.update {list->
            val newList =ArrayUtils.removeItem(it,list )
            newList } }



    fun done(){
        getNavigator().done() }

}