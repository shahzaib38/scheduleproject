package sb.app.messageschedular.util

import com.google.gson.Gson
import sb.app.messageschedular.model.Sms

object GsonUtils {

    fun serializeSms(sms: Sms):String {
        val gson : Gson =   Gson()
        return   gson.toJson(sms) }

    fun deSerializeSms(value:String ):Sms {
        val gson = Gson()
        return gson.fromJson<Sms>(value ,Sms::class.java) }
}