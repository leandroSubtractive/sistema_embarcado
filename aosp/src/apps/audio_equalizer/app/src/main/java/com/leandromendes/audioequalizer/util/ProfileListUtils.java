package com.leandromendes.audioequalizer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leandromendes.audioequalizer.R;
import com.leandromendes.audioequalizer.data.model.EqualizerProfile;

import java.util.List;

public class ProfileListUtils extends ArrayAdapter<EqualizerProfile> {
    private final Context context;
    private List<EqualizerProfile> profiles;

    public ProfileListUtils(Context context, List<EqualizerProfile> profiles) {
        super(context, android.R.layout.simple_list_item_1, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_profile, parent, false);

        TextView txtNome = rowView.findViewById(R.id.profileName);
        EqualizerProfile profile = profiles.get(position);
        txtNome.setText(profile.getName());

        return rowView;
    }
}
