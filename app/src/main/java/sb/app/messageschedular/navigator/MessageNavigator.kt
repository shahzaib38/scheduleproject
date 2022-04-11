package sb.app.messageschedular.navigator

import android.Manifest
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Messenger
import sb.app.messageschedular.model.Sms

interface MessageNavigator : Navigator {

    fun changeTime()
    fun changeDate()
    fun scheduleService(sms : Sms)
      fun openTypeMessageDialog()
    //  fun requestSmsPermission(permission :String = Manifest.permission.SEND_SMS )
      fun changeSim()
   fun  todo()


}