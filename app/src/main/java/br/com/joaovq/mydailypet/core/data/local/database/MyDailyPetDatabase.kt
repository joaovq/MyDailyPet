package br.com.joaovq.mydailypet.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.joaovq.mydailypet.pet.data.converters.DateTypeConverter
import br.com.joaovq.mydailypet.pet.data.converters.SexTypeConverter
import br.com.joaovq.mydailypet.pet.data.dao.PetDao
import br.com.joaovq.mydailypet.pet.data.dto.PetDto

@Database(
    entities = [
        PetDto::class,
    ],
    version = 2,
)
@TypeConverters(
    value = [
        SexTypeConverter::class,
        DateTypeConverter::class,
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
                    ).fallbackToDestructiveMigration()/*addMigrations(
                        DatabaseMigrations.Migration1To2,
                    )*/.build()
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

        object Migration2To3 : Migration(2, 3), DatabaseMigrations {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE pet_tb ADD COLUMN animal TEXT")
            }
        }

        object Migration3To4 : Migration(3, 4), DatabaseMigrations {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                CREATE TABLE new_Pet (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,
                    name TEXT NOT NULL,
                    nickname TEXT NOT NULL,
                    sex TEXT NOT NULL,
                    weight REAL NOT NULL,
                    birth INTEGER,
                    type TEXT NOT NULL,
                    image_url TEXT NOT NULL,
                    animal TEXT
                )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                INSERT INTO new_Pet (name, nickname, sex, weight, birth, type, image_url,animal)
                SELECT name, nickname, sex, weight, birth, type, image_url,animal FROM pet_tb
                    """.trimIndent(),
                )
                database.execSQL("DROP TABLE pet_tb")
                database.execSQL("ALTER TABLE new_Pet RENAME TO pet_tb")
            }
        }
    }
}
