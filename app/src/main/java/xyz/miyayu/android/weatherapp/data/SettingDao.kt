package xyz.miyayu.android.weatherapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)

    @Update
    fun update(setting: Setting)

    @Delete
    fun delete(setting: Setting)

    @Query("SELECT * from setting WHERE name = :apiKey")
    fun getItem(apiKey: String = API_KEY_COL): Flow<Setting>
}