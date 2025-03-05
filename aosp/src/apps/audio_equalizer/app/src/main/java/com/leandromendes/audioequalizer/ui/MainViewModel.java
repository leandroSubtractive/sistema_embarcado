package com.leandromendes.audioequalizer.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leandromendes.audioequalizer.data.model.EqualizerProfile;
import com.leandromendes.audioequalizer.data.repository.UserRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    UserRepository userRepository;

    private MutableLiveData<String> toastText = new MutableLiveData<>();

    public MainViewModel(){
        userRepository = new UserRepository();
    }

    public List<EqualizerProfile> GetAllEqualizerProfiles(){
        return userRepository.getAllEqualizerProfiles();
    }

    public EqualizerProfile GetEqualizerProfiles(int position){
        return userRepository.getEqualizerProfile(position);
    }

    public void AddProfile(EqualizerProfile equalizerProfile){
        userRepository.AddProfile(equalizerProfile);
    }

    public void UpdateProfile(int position, EqualizerProfile equalizerProfile){
        userRepository.UpdateProfile(position, equalizerProfile);
    }

    public void RemoveProfile(int position){
        userRepository.RemoveProfile(position);
    }

    public LiveData<String> getToastText() {
        return toastText;
    }

    public void SetToastText(String text) {
        toastText.setValue(text);
    }
}
