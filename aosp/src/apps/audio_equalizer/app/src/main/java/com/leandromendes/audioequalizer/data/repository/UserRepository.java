package com.leandromendes.audioequalizer.data.repository;

import com.leandromendes.audioequalizer.data.model.EqualizerProfile;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final List<EqualizerProfile> equalizerProfiles = new ArrayList<>();

    public UserRepository() {
        // Initializes default profile
        equalizerProfiles.add(new EqualizerProfile());
        // TODO: Read values from the database
    }

    public void AddProfile(EqualizerProfile equalizerProfile){
        equalizerProfiles.add(equalizerProfile);
    }

    public void RemoveProfile(int index){
        equalizerProfiles.remove(index);
    }

    public void UpdateProfile(int index, EqualizerProfile equalizerProfile){
        equalizerProfiles.set(index, equalizerProfile);
    }

    public List<EqualizerProfile> getAllEqualizerProfiles() {
        return equalizerProfiles;
    }

    public EqualizerProfile getEqualizerProfile(int position){
        return equalizerProfiles.get(position);
    }

}
