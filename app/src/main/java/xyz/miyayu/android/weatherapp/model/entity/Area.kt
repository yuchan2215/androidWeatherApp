package xyz.miyayu.android.weatherapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class Area(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)