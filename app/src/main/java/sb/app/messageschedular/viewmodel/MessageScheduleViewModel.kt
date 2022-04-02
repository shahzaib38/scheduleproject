package sb.app.messageschedular.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import sb.app.messageschedular.model.*
import sb.app.messageschedular.navigator.MessageNavigator
import sb.app.messageschedular.repo.MessageRepository
import sb.app.messageschedular.util.DateUtils
import sb.app.messageschedular.util.SmsUiState
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MessageScheduleViewModel
    @Inject constructor(private val messageRepository: MessageRepository)

    : BaseViewModel<MessageNavigator>(messageRepository){


    companion object{
     const val  CONTACT_RANGE =  "select at least 1 contact"
      const val MESSAGE_EMPTY = "message must not be empty "
        const val VALID_TIME = "choose a valid time"
        const val    TIME_RANGE ="choose valid time at least 5 minutes from now "


    }


   private val _UiSearchState  = MutableStateFlow<SmsUiState>(SmsUiState())
    val uiSearchState =_UiSearchState.asLiveData()

    private val _MessageInput =MutableLiveData<String>()
    val messageInput =_MessageInput

    private val _Message  = MutableStateFlow<Messages>(Messages())
     val message =_Message.asLiveData()

    private var _ProgressLoading =MutableStateFlow<Boolean>(value =false )
     val progress =_ProgressLoading.asLiveData()

    private var  _SmsError = MutableSharedFlow<String>()
    val smsError =_SmsError.asSharedFlow()



    fun sendError(errorMessage:String ){

        viewModelScope.launch {
            _SmsError
                .emit(errorMessage)
        }

    }



    init {
        updateDate(System.currentTimeMillis()) }

     fun updateDate(date: Long){
         _Message.update {
             it.copy(date = date  )}
     }



    /**** Update Time*********/
    fun updateTime(hours : Int ,minutes :Int , is24HourFormat :Boolean ){
        _Message.update {it:Messages ->
        it.copy(time =Time(hours = hours, minutes = minutes, is24Hours = is24HourFormat) ) } }



    /***** Search Focus ********/
    fun onSearchFocusedChange(v: View, hasFocus :Boolean ){

        _UiSearchState.update {
            it.copy(isFocused = hasFocus) }
    }

    /***** Progress ************/
   private  fun showProgress(loading :Boolean ){
        _ProgressLoading.value = loading

   }

    /************ Search from Content Provider ************/
    fun search(s: CharSequence?, start: Int, before: Int, count: Int ){
        if(s==null) return

        onValueChanged(s.toString().trim())
        showProgress(true  )
        viewModelScope.launch {
            messageRepository.getContacts(s.toString().trim())
                .catch {

                     }.flowOn(Dispatchers.IO).collect { result ->

                result.forEach {
                    Log.i("MaoList" , "Result contactId ${it.contactId}  name ${it.name}       ")

                }




                _UiSearchState.update {
                 val searchDisplay =         it.getDisplaySearch()

                   it.processResult(searchDisplay ,result)

                           }

            } }
        showProgress(false  )

    }



    private fun SmsUiState.processResult(searchDisplay:
                              sb.app.messageschedular.util.SearchDisplay,
                                         result :List<Contact>
                              ) :SmsUiState{

        return when(searchDisplay) {
                sb.app.messageschedular.util.SearchDisplay.Results -> {
                    this.copy(contactList = result )

                }
                sb.app.messageschedular.util.SearchDisplay.Nothing -> {
                    this.copy(contactList = emptyList())

                }


            sb.app.messageschedular.util.SearchDisplay.NoResult -> {
                    this.copy(contactList = emptyList())

            } }

    }


    /************ message **************/
    fun messageValueChange(s: CharSequence?, start: Int, before: Int, count: Int) {

        println("messazge"+s )
        if(s==null )return

        _Message.update {
            it.copy(message = s.toString().trim()) } }


    /**************** Remove Contact **************/
            fun removeContact(contact: Contact) {
                _UiSearchState.update {
                    val arrayList =ArrayList<Contact>(it.selectedList)
                    arrayList.remove(contact)
                    it.copy(selectedList = arrayList) } }


    /**************** Add Contact **************/
    fun addContact(contact : Contact){
        _UiSearchState.update {
            val arrayList =ArrayList<Contact>(it.selectedList)
            arrayList.add(contact)
            it.copy(selectedList = arrayList ,
                contactList = emptyList() , searchInput = "" ) } }



            fun onValueChanged(value :String ){
                _UiSearchState.update {
                    it.copy(searchInput = value ) } }



    /**** Change Date *****/
    fun changeDate(){
        getNavigator().changeDate() }

    /*** Change Time ****/
    fun changeTime(){
        getNavigator().changeTime() }


    /**** Schedule Sms *****/
    fun schedule(smsUiState :SmsUiState ,message :Messages){
        println("smsUiState " +smsUiState  +"message "+message)

        val selectedListIsEmpty = smsUiState.selectedList.isEmpty()

        if(selectedListIsEmpty) {
            sendError(CONTACT_RANGE)

            return }

      val messageIsEmpty=  message.message.isEmpty()
        if(messageIsEmpty){
            sendError(MESSAGE_EMPTY)
            return }

        val time  = message.time
        if(time==null ) {
            sendError(VALID_TIME)
            return }

        val date =   DateUtils.countDownTime(message)
        val isValid =   DateUtils.isValidTime(date)

        if(!isValid){
            sendError(TIME_RANGE)
            return }

        val sms = createSms(message , smsUiState)
        getNavigator().scheduleService(sms )
    }



  private fun createSms(message: Messages ,smsUiState: SmsUiState): Sms {
      val newId = DateUtils.generateId()
      val contactList =ArrayList<Contact>()
      smsUiState.selectedList.forEachIndexed { index, contact ->
          contactList.add(Contact(contactId = contact.contactId ,
              name =contact.name,
              phone=contact.phone,
              messageId = newId ,
          smsId = index)) }




      return Sms(messages = message.copy(messageId = newId) ,
          contactList) }




    fun openTypeMessageDialog(){

        getNavigator().openTypeMessageDialog()


    }


   }

