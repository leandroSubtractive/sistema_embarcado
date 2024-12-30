package com.leandromendes.audioequalizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView profileList;
    private List<EqualizerProfile> equalizerProfiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Define the title of the list of profile names
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Profiles");

        // Initializes default profile
        EqualizerProfile defaultEqProfile = new EqualizerProfile();
        equalizerProfiles.add(defaultEqProfile);

        profileList = findViewById(R.id.profileList);
        // Initialize adapter for profile list
        ProfileList adapter = new ProfileList(this, equalizerProfiles);
        profileList.setAdapter(adapter);

        // Defines an action for a quick click in the listview
        profileList.setOnItemClickListener((parent, view, position, id) -> {
            EqualizerProfile profileSelected = equalizerProfiles.get(position);
            Toast.makeText(this, "Profile " + profileSelected.getName() + " selected", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EqualizerActivity.class);
            intent.putExtra("profile", profileSelected);
            // Start the new Activity
            startActivity(intent);
        });

        // Defines an action for a long click on a profile list
        profileList.setOnItemLongClickListener((parent, view, position, id) -> {
            String profileSelected = equalizerProfiles.get(position).getName();
            // If the selected item is different from the default profile, delete the profile from the list
            if(!profileSelected.equals(defaultEqProfile.getName())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm exclusion");
                builder.setMessage("Do you want to delete profile " + equalizerProfiles.get(position).getName() + "?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    equalizerProfiles.remove(position);
                    adapter.notifyDataSetChanged();
                });
                builder.setNegativeButton("No", null);
                builder.show();
            } else {
                Toast.makeText(this, "Cannot delete " + profileSelected + " profile.", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

    }

}