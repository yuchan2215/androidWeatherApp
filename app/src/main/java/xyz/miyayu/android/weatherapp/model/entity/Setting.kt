package xyz.miyayu.android.weatherapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.miyayu.android.weatherapp.model.dao.SettingDao.Companion.API_KEY_COL

@Entity(tableName = "setting")
data class Setting(
    @PrimaryKey(autoGenerate = false)
    val name: String = API_KEY_COL,
    val value: String?
)