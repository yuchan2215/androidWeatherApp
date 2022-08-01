package xyz.miyayu.android.weatherapp

import android.app.Application
import xyz.miyayu.android.weatherapp.model.room.SettingRoomDatabase

class WeatherApplication : Application() {
    companion object {
        lateinit var instance: Application private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val database: SettingRoomDatabase by lazy {
        SettingRoomDatabase.getDatabase(this)
    }
}