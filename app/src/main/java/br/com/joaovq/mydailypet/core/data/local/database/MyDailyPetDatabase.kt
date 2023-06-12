package br.com.joaovq.mydailypet.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.joaovq.mydailypet.core.data.local.converters.SexTypeConverter
import br.com.joaovq.mydailypet.core.data.local.dao.PetDao
import br.com.joaovq.mydailypet.core.data.local.dto.PetDto

@Database(
    entities = [
        PetDto::class,
    ],
    version = 2,
)
@TypeConverters(
    value = [
        SexTypeConverter::class,
    ],
)
abstract class MyDailyPetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var instance: MyDailyPetDatabase? = null
        fun getInstance(context: Context): MyDailyPetDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        MyDailyPetDatabase::class.java,
                        "my_daily_pet_db",
                    ).addMigrations(DatabaseMigrations.Migration1To2).build()
                }
                return instance as MyDailyPetDatabase
            }
        }
    }

    sealed interface DatabaseMigrations {
        object Migration1To2 : Migration(1, 2), DatabaseMigrations {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}
