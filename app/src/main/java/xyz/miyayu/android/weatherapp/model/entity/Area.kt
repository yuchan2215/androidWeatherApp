package xyz.miyayu.android.weatherapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class Area(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    /**
     * 経度。
     * アップデートにて追加されたため、Nullが入っていることがある
     */
    val longitude: Double?,
    /**
     * 緯度。
     * アップデートにて追加されたため、Nullが入っていることがある
     */
    val latitude: Double?
) {
    /**
     * 無効なデータかどうかを確認します。
     * 緯度経度がnullの場合、無効と判断します。
     */
    fun isInvalid() = longitude == null || latitude == null
}