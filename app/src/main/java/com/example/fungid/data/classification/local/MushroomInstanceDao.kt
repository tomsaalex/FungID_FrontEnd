package com.example.fungid.data.classification.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fungid.data.classification.MushroomInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface MushroomInstanceDao {
    @Query("SELECT * FROM mushroom_instances ORDER BY sampleTakenAt DESC")
    fun getAll(): Flow<List<MushroomInstance>>

    @Query("SELECT * FROM mushroom_instances WHERE id = :id LIMIT 1")
    suspend fun getMushroomInstance(id: String): MushroomInstance

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mushroomInstance: MushroomInstance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mushroomInstances: List<MushroomInstance>)

    @Update
    suspend fun update(mushroomInstance: MushroomInstance): Int

    @Query("UPDATE mushroom_instances SET localImagePath = :localImagePath WHERE id = :id")
    suspend fun updateImagePath(id: String, localImagePath: String): Int

    @Query("DELETE FROM mushroom_instances WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM mushroom_instances")
    suspend fun deleteAll()
}