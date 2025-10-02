package com.leandromendes.vehicleequalizer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

@Dao
interface ProfileDao {

    // Returns LiveData so that the UI can observe changes in real time
    @Query("SELECT * FROM equalizer_profiles ORDER BY id ASC")
    fun getAllProfiles(): LiveData<List<EqualizerProfile>>

    // Inserts a new profile. If there is a conflict, it replaces it
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(profile: EqualizerProfile): Long // Returns the ID of the new item

    // Updates an existing profile
    @Update
    suspend fun update(profile: EqualizerProfile)

    // Remove a profile
    @Delete
    suspend fun delete(profile: EqualizerProfile)

    // Remove a profile by ID
    @Query("DELETE FROM equalizer_profiles WHERE id = :profileId")
    suspend fun deleteById(profileId: Int)
}