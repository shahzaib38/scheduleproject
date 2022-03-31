package sb.app.messageschedular.util

import android.text.format.DateFormat
import android.text.format.DateUtils
import sb.app.messageschedular.enums.Meridiem
import sb.app.messageschedular.model.Messages
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.model.Time
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

object DateUtils {

    const val MIDDAY =12
    const val TIME_FORMAT ="%02d:%02d %s"
    const val AM ="AM"
    const val PM ="PM"
    const val DATE_FORMAT="dd/MM/yyyy"
    const val VALID_TIME= 60000

    fun formatSmsDate(smsUiState: Messages):java.util.Date{
          val time =     smsUiState.time!!
          val date =java.util.Date(smsUiState.date!!)

        val day =   DateFormat.format("dd",date )
        val month  =   DateFormat.format("MM",date )
        val year =   DateFormat.format("yyyy",date )

        println("hours"+time?.hours)
        println("minutes"+time?.minutes)
        println("day"+day)
        println("month "+month)
        println("year"+year)


        val calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_MONTH ,day.toString().trim().toInt() )
        calender.set(Calendar.MONTH,month.toString().trim().toInt()-1)
        calender.set(Calendar.YEAR ,year.toString().trim().toInt())
        calender.set(Calendar.HOUR_OF_DAY ,time.hours)
        calender.set(Calendar.MINUTE ,time.minutes)

        return calender.time }


   fun  isAmPm(hoursOfDay:Int )
   : Meridiem =  if(hoursOfDay < MIDDAY){ Meridiem.AM
       } else
       { Meridiem.PM }

    fun twelveHourTime(hoursOfDay: Int):Int  = hoursOfDay - MIDDAY

    fun twelveHourFormat(hours :Int ,minutes :Int ) : String{
    return if(isAmPm(hoursOfDay = hours) == Meridiem.AM){
                 String.format(TIME_FORMAT,hours ,minutes , AM) }
                else { String.format(TIME_FORMAT,twelveHourTime(hours) ,minutes , PM) } }



    fun convert12HourFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat("hh:mm a")
        val format =    timeFormatter.format(date)
        return format }

    fun convert24HourFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat("HH:mm")
        val format =    timeFormatter.format(date)
        return format }



    fun convertDateIntoFormat(long  :Long ):String{
        val date =  java.util.Date(long)
        val timeFormatter = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val format =    timeFormatter.format(date)
        return format}


    fun countDownTime(sms : Messages): Long {

        val date =   formatSmsDate(sms)

        return date.time - System.currentTimeMillis()}


    fun countDownTime(sms : Sms): Long {

        val date =   formatSmsDate(sms)

        return date.time - System.currentTimeMillis()
    }

    fun isValidTime(currentTime :Long ):Boolean = currentTime > VALID_TIME
    fun generateId() :Long {

        return System.currentTimeMillis() }



    fun formatSmsDate(sms: Sms):java.util.Date{
        val time =     sms.messages.time
        val date =java.util.Date(sms.messages.date)

        val day =   DateFormat.format("dd",date )
        val month  =   DateFormat.format("MM",date )
        val year =   DateFormat.format("yyyy",date )


        val calender = Calendar.getInstance()
        calender.set(Calendar.DAY_OF_MONTH ,day.toString().trim().toInt() )
        calender.set(Calendar.MONTH,month.toString().trim().toInt()-1)
        calender.set(Calendar.YEAR ,year.toString().trim().toInt())
        calender.set(Calendar.HOUR_OF_DAY ,time.hours)
        calender.set(Calendar.MINUTE ,time.minutes)

        return calender.time }




     fun constructTime(time: Time):String{

        return    if(time.is24Hours){

            String.format("%02d:%02d",time.hours ,time.minutes)

        }else{

            sb.app.messageschedular.util.DateUtils.twelveHourFormat(hours = time.hours, time.minutes) }

    }




}


