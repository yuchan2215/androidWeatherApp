package xyz.miyayu.android.weatherapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val API_KEY_COL = "apiKey"

@Entity(tableName = "setting")
data class Setting(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val value: String?
)