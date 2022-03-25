package sb.app.messageschedular.contactHelper

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sb.app.messageschedular.model.Contact
import javax.inject.Inject


class ContactHelper @Inject constructor( @ApplicationContext val context: Context){


    companion object{

        private val CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        private val CONTACT_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        private val PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER

        private val PROJECTION: Array<out String> = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            else
                ContactsContract.Contacts.DISPLAY_NAME,
            PHONE_NUMBER

            )

        private val SELECTION: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"
            else
                "${ContactsContract.Contacts.DISPLAY_NAME} LIKE ?"


    }

//
//
//    sealed class Result<out T>(){
//
//        data class Success<out T>(val t :T ):  Result<T>()
//        data  class Error(val data  : Exception ):Result<Nothing>()
//
//    }

   @OptIn(ExperimentalCoroutinesApi::class)
   suspend  fun query(query :String ): Flow<List<Contact>> = flow {

       val  arrayList  = ArrayList<Contact>()
        val cursor1 = context.contentResolver.query(
            CONTACT_URI, PROJECTION, SELECTION,
            arrayOf("%$query%"), null)

       try {
               val cursor : Cursor =   cursor1 ?: throw NullPointerException("Cursoe must not be null")
               while (cursor.moveToNext()) {


                   val name = cursor.getColumnIndexOrThrow(CONTACT_NAME)
                   val number = cursor.getColumnIndexOrThrow(PHONE_NUMBER)
                   val displayName = cursor.getString(name)
                    val displayNumber = cursor.getString(number)
                   
                   val contact = createContact(displayName,displayNumber)
                   arrayList.add(contact) }

           this.emit(arrayList)

       } finally{
            cursor1?.close()

       }


   }

    private fun createContact(name :String ,phone :String ): Contact =Contact(contactId = 0,name ,phone, smsId = 0 )


}