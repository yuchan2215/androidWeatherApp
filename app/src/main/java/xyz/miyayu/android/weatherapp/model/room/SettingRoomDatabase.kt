package xyz.miyayu.android.weatherapp.model.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import xyz.miyayu.android.weatherapp.model.dao.AreaDao
import xyz.miyayu.android.weatherapp.model.dao.SettingDao
import xyz.miyayu.android.weatherapp.model.entity.Area
import xyz.miyayu.android.weatherapp.model.entity.Setting

@Database(
    entities = [Setting::class, Area::class], version = 3, exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class SettingRoomDatabase : RoomDatabase() {
    abstract fun settingDao(): SettingDao
    abstract fun areaDao(): AreaDao

    companion object {
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE areas ADD COLUMN longitude REAL")
                database.execSQL("ALTER TABLE areas ADD COLUMN latitude REAL")
            }
        }

        @Volatile
        private var INSTANCE: SettingRoomDatabase? = null
        fun getDatabase(context: Context): SettingRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SettingRoomDatabase::class.java,
                    "setting_database"
                ).fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}