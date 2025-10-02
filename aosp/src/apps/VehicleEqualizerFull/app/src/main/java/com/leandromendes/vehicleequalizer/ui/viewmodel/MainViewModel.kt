package com.leandromendes.vehicleequalizer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.data.repository.UserRepository
import kotlinx.coroutines.launch

// MainViewModel now receives the Repository in the constructor
class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    // LiveData that MainActivity will OBSERVE
    val allProfilesLiveData: LiveData<List<EqualizerProfile>> = userRepository.allEqualizerProfiles

    private val toastText = MutableLiveData<String?>()

    fun addProfile(equalizerProfile: EqualizerProfile) = viewModelScope.launch {
        userRepository.addProfile(equalizerProfile)
    }

    fun updateProfile(equalizerProfile: EqualizerProfile) = viewModelScope.launch {
        userRepository.updateProfile(equalizerProfile)
    }

    fun removeProfile(equalizerProfile: EqualizerProfile) = viewModelScope.launch {
        userRepository.removeProfile(equalizerProfile)
    }

    fun getToastText(): LiveData<String?> {
        return toastText
    }

    fun setToastText(text: String?) {
        toastText.value = text
    }
}