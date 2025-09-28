package com.leandromendes.vehicleequalizer.data.repository

import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class UserRepository {
   private val _allEqualizerProfiles: MutableList<EqualizerProfile> = mutableListOf()

    val allEqualizerProfiles: MutableList<EqualizerProfile>
        get() = _allEqualizerProfiles

    init {
        // Initializes default profile
        _allEqualizerProfiles.add(EqualizerProfile())
        // TODO: Read values from the database
    }

    fun addProfile(equalizerProfile: EqualizerProfile) {
        _allEqualizerProfiles.add(equalizerProfile)
    }

    fun removeProfile(index: Int) {
        if (index >= 0 && index < _allEqualizerProfiles.size) {
            _allEqualizerProfiles.removeAt(index)
        }
    }

    fun updateProfile(index: Int, equalizerProfile: EqualizerProfile) {
        if (index >= 0 && index < _allEqualizerProfiles.size) {
            _allEqualizerProfiles[index] = equalizerProfile
        }
    }

    fun getEqualizerProfile(position: Int): EqualizerProfile {
        return _allEqualizerProfiles[position]
    }
}
