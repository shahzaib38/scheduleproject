package sb.app.messageschedular.navigator

import sb.app.messageschedular.model.Message

interface ListNavigator : Navigator {

    fun delete(message : Message)
   fun changeAdapter()
   fun  showDialog(message : Message)
   fun Test()
}