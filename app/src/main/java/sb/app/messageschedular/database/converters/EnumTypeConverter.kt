package sb.app.messageschedular.database.converters

import androidx.room.TypeConverter
import sb.app.messageschedular.enums.MessageStatus

class EnumTypeConverter {

    @TypeConverter
    fun enumToInt(messageStatus: MessageStatus):String{
        return messageStatus.name }

    @TypeConverter
    fun intToEnum(name:String  ):MessageStatus{
        return enumValueOf<MessageStatus>(name) }






}