package xyz.miyayu.android.weatherapp.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import xyz.miyayu.android.weatherapp.model.entity.Area

@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(area: Area)

    @Update
    suspend fun update(area: Area)

    @Delete
    suspend fun delete(area: Area)

    @Query("SELECT * from areas ORDER BY id ASC")
    fun getItems(): Flow<List<Area>>
}