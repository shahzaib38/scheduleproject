package sb.app.messageschedular.navigator

interface TodoNavigator : Navigator {

   fun  addItem(value : String )
     fun done()
     fun checkStatus()
     fun validate()


}