package xyz.miyayu.android.weatherapp.repositories

import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.entity.Area

object AreaRepository {
    suspend fun insertArea(areaName: String) {
        val area = Area(name = areaName)
        WeatherApplication.instance.database.areaDao().insert(area)
    }
}