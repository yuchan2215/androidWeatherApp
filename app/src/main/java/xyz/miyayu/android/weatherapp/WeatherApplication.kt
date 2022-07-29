package xyz.miyayu.android.weatherapp

import android.app.Application
import xyz.miyayu.android.weatherapp.model.room.SettingRoomDatabase

class WeatherApplication : Application() {
    val database: SettingRoomDatabase by lazy {
        SettingRoomDatabase.getDatabase(this)
    }
}