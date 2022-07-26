package xyz.miyayu.android.weatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Setting::class], version = 1, exportSchema = false)
abstract class SettingRoomDatabase : RoomDatabase() {
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: SettingRoomDatabase? = null
        fun getDatabase(context: Context): SettingRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SettingRoomDatabase::class.java,
                    "setting_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}