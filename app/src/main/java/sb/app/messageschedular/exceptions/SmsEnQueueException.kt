package sb.app.messageschedular.exceptions

import sb.app.messageschedular.SmsException

class SmsEnQueueException(override var  smsId:Long, override val smsIndex :Long,
                          override val message: String, ) : SmsException(smsId  , smsIndex  , message ){


}