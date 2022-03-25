package sb.app.messageschedular.navigator

import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Messenger
import sb.app.messageschedular.model.Sms

interface MessageNavigator : Navigator {

    fun changeTime()
    fun changeDate()
    fun scheduleService(sms : Sms)

}