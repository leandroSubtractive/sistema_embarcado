package com.leandromendes.vehicleequalizer.data.repository

import androidx.lifecycle.LiveData
import com.leandromendes.vehicleequalizer.data.ProfileDao
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class UserRepository(private val profileDao: ProfileDao) {
    // Returns LiveData of profiles. This list will be used by the ViewModel.
    val allEqualizerProfiles: LiveData<List<EqualizerProfile>> = profileDao.getAllProfiles()

    suspend fun addProfile(equalizerProfile: EqualizerProfile) {
        // ID is 0 (autoGenerate) so that Room inserts it as new
        profileDao.insert(equalizerProfile.copy(id = 0))
    }

    suspend fun removeProfile(profile: EqualizerProfile) {
        profileDao.delete(profile)
    }

    suspend fun updateProfile(equalizerProfile: EqualizerProfile) {
        // Assume that the EqualizerProfile object already has the database ID
        profileDao.update(equalizerProfile)
    }
}