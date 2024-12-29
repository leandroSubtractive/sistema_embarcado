package com.leandromendes.audioequalizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProfileList extends ArrayAdapter<EqualizerProfile> {
    private Context context;
    private List<EqualizerProfile> profiles;

    public ProfileList(Context context, List<EqualizerProfile> profiles) {
        super(context, android.R.layout.simple_list_item_1, profiles);
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_profile, parent, false);

        TextView txtNome = rowView.findViewById(R.id.profileName);
        EqualizerProfile profile = profiles.get(position);
        txtNome.setText(profile.getName());

        return rowView;
    }

}
