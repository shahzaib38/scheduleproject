package sb.app.messageschedular.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sb.app.messageschedular.database.converters.EnumTypeConverter
import sb.app.messageschedular.model.Message

@Database(entities = [Message::class], version = 1, exportSchema = false)
@TypeConverters(EnumTypeConverter::class)
abstract class MessageDatabase : RoomDatabase(){


    abstract fun smsDao(): MessageDao


    companion object {

      private   const val DATABASE_NAME ="sms_database"
        @Volatile private var instance: MessageDatabase? = null

        fun getInstance(context: Context): MessageDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it } } }

        private fun buildDatabase(context: Context): MessageDatabase {
            return Room.databaseBuilder(context, MessageDatabase::class.java, DATABASE_NAME)
                .build()
        } }

}