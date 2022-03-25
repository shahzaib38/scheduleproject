package sb.app.messageschedular

import java.lang.RuntimeException

open class SmsException(open var  smsId :Long  , open val smsIndex : Long  ,override val message:String  ) : RuntimeException(message){


}