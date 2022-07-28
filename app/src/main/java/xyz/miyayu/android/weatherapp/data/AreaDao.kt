package xyz.miyayu.android.weatherapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(area: Area)

    @Update
    fun update(area: Area)

    @Delete
    fun delete(area: Area)

    @Query("SELECT * from areas ORDER BY id ASC")
    fun getItems(): Flow<List<Area>>
}