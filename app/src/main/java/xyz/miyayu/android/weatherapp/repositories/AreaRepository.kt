package xyz.miyayu.android.weatherapp.repositories

import xyz.miyayu.android.weatherapp.WeatherApplication
import xyz.miyayu.android.weatherapp.model.entity.Area

object AreaRepository {
    fun getAreaList() =
        WeatherApplication.instance.database.areaDao().getItems()


    suspend fun insertArea(areaName: String, latitude: Double, longitude: Double) {
        val area = Area(
            name = areaName,
            latitude = latitude,
            longitude = longitude
        )
        WeatherApplication.instance.database.areaDao().insert(area)
    }

    suspend fun deleteArea(area: Area) {
        WeatherApplication.instance.database.areaDao().delete(area)
    }
}