package com.leandromendes.vehicleequalizer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.data.repository.UserRepository

class MainViewModel : ViewModel() {
    var userRepository: UserRepository = UserRepository()

    private val toastText = MutableLiveData<String?>()

    fun getAllEqualizerProfiles(): MutableList<EqualizerProfile> {
        return userRepository.allEqualizerProfiles
    }

    fun getEqualizerProfiles(position: Int): EqualizerProfile {
        return userRepository.getEqualizerProfile(position)
    }

    fun addProfile(equalizerProfile: EqualizerProfile) {
        userRepository.addProfile(equalizerProfile)
    }

    fun updateProfile(position: Int, equalizerProfile: EqualizerProfile) {
        userRepository.updateProfile(position, equalizerProfile)
    }

    fun removeProfile(position: Int) {
        userRepository.removeProfile(position)
    }

    fun getToastText(): LiveData<String?> {
        return toastText
    }

    fun setToastText(text: String?) {
        toastText.value = text
    }
}