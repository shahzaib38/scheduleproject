package sb.app.messageschedular.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sb.app.messageschedular.enums.ListEnum
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Messages
import sb.app.messageschedular.navigator.ListNavigator
import sb.app.messageschedular.repo.MessageRepository
import javax.inject.Inject


data class AppBarUI(
    var listState  : ListEnum =ListEnum.SELECTALL
)


@HiltViewModel
class MessageListViewModel  @Inject constructor(private val messageRepository: MessageRepository ): BaseViewModel<ListNavigator>(messageRepository) {


    private var _smsList = MutableLiveData<List<Message>>()
    val smsList = _smsList

    private var _AppBarUI = MutableStateFlow<AppBarUI>(AppBarUI())
    val appBarUI = _AppBarUI.asLiveData()

    fun delete(message : Message){
        getNavigator().delete(message )


    }



    fun collectUserMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.getUserList().collect {

                _smsList.postValue( it)

            }
        }
    }


    fun selectAll(){
        getNavigator().changeAdapter()

    }

    fun updateVisibililty(listEnum : ListEnum) {

        println("visibility"+listEnum)
        _AppBarUI.update {
            it.copy(listState = listEnum ) }



    }

    fun showDialog(message : Message) {
        getNavigator().showDialog(message)

    }

}