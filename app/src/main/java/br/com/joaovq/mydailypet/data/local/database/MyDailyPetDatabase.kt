package br.com.joaovq.mydailypet.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.joaovq.mydailypet.data.converters.AttachConverter
import br.com.joaovq.mydailypet.data.converters.DateTypeConverter
import br.com.joaovq.mydailypet.data.converters.NotificationAlarmConverter
import br.com.joaovq.mydailypet.data.converters.SexTypeConverter
import br.com.joaovq.mydailypet.data.converters.TaskConverter
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.model.PetDto
import br.com.joaovq.mydailypet.reminder.data.dao.ReminderDao
import br.com.joaovq.mydailypet.reminder.data.model.ReminderDto

const val MY_DAILY_PET_DATABASE_NAME = "my_daily_pet_db"

@Database(
    entities = [
        PetDto::class,
        ReminderDto::class,
    ],
    version = 2,
)
@TypeConverters(
    value = [
        SexTypeConverter::class,
        NotificationAlarmConverter::class,
        DateTypeConverter::class,
        TaskConverter::class,
        AttachConverter::class,
    ],
)
abstract class MyDailyPetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun reminderDao(): ReminderDao

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
                    ).fallbackToDestructiveMigration()/*addMigrations(
                        DatabaseMigrations.Migration1To2,
                    )*/.build()
                }
                return instance as MyDailyPetDatabase
            }
        }
    }
}
