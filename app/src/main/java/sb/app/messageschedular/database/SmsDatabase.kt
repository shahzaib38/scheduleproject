package sb.app.messageschedular.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sb.app.messageschedular.database.converters.EnumTypeConverter
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Messages

@Database(entities = [Messages::class , Contact::class], version = 1, exportSchema = false)
@TypeConverters(EnumTypeConverter::class)
abstract class SmsDatabase : RoomDatabase(){


    abstract fun smsDao(): SmsDao


    companion object {

        private   const val DATABASE_NAME ="message_database"
        @Volatile private var instance: SmsDatabase? = null

        fun getInstance(context: Context): SmsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it } } }

        private fun buildDatabase(context: Context): SmsDatabase {
            return Room.databaseBuilder(context, SmsDatabase::class.java, DATABASE_NAME)
                .build()
        } }

}