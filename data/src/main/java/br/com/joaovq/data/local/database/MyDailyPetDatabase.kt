package br.com.joaovq.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.joaovq.data.converters.AttachConverter
import br.com.joaovq.data.converters.DateTypeConverter
import br.com.joaovq.data.converters.NotificationAlarmConverter
import br.com.joaovq.data.converters.SexTypeConverter
import br.com.joaovq.data.local.dao.PetDao
import br.com.joaovq.model.PetDto
import br.com.joaovq.reminder_data.dao.ReminderDao
import br.com.joaovq.reminder_data.model.ReminderDto
import br.com.joaovq.tasks_data.dao.TaskDao
import br.com.joaovq.tasks_data.model.TaskDto

const val MY_DAILY_PET_DATABASE_NAME = "my_daily_pet_db"

@Database(
    entities = [
        PetDto::class,
        ReminderDto::class,
        TaskDto::class,
    ],
    version = 1,
)
@TypeConverters(
    value = [
        SexTypeConverter::class,
        NotificationAlarmConverter::class,
        DateTypeConverter::class,
        AttachConverter::class,
    ],
)
abstract class MyDailyPetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun reminderDao(): ReminderDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: MyDailyPetDatabase? = null
        fun getInstance(context: Context): MyDailyPetDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        MyDailyPetDatabase::class.java,
                        MY_DAILY_PET_DATABASE_NAME,
                    ).build()
                }
                return instance as MyDailyPetDatabase
            }
        }
    }
}
