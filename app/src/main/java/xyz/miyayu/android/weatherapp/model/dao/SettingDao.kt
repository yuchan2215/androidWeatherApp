package xyz.miyayu.android.weatherapp.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.miyayu.android.weatherapp.model.entity.Setting


@Dao
interface SettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)

    @Update
    fun update(setting: Setting)

    @Delete
    fun delete(setting: Setting)

    @Query("SELECT * from setting WHERE name = :col")
    fun getItem(col: String = API_KEY_COL): Flow<Setting>

    @Query("SELECT * from setting WHERE name = :col")
    suspend fun getItemOnce(col: String = API_KEY_COL): Setting

    companion object {
        internal const val API_KEY_COL = "apiKey"
    }
}