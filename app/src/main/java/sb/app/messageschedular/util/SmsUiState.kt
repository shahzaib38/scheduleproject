package sb.app.messageschedular.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Time


@Parcelize
data class  SmsUiState(

//    var isLoading :Boolean,
    var searchInput: String ="",
    var contactList :  List<Contact> = emptyList<Contact>(),
    var isFocused :Boolean =false,
//    var errorMessage :String?=null,
    val selectedList :  List<Contact> = emptyList(),
    var messageInput : String ="",
//    var time : Time?=null,
//    var date: Long?=null
) : Parcelable {

    fun getDisplaySearch(): SearchDisplay = when {
        !isFocused && searchInput.isEmpty() -> SearchDisplay.Nothing
        isFocused && searchInput.isEmpty() -> SearchDisplay.Nothing

        else -> SearchDisplay.Results }

}



enum class SearchDisplay{
    Nothing,Results ,NoResult
}