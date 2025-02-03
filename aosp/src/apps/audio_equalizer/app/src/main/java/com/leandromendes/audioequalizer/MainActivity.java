package com.leandromendes.audioequalizer;

import static java.lang.String.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final List<EqualizerProfile> equalizerProfiles = new ArrayList<>();

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
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                format(Locale.getDefault(), getString(R.string.toolbar_name)));

        // Initializes default profile
        EqualizerProfile defaultEqProfile = new EqualizerProfile(this.getApplicationContext());
        equalizerProfiles.add(defaultEqProfile);

        ListView profileList = findViewById(R.id.profileList);
        // Initialize adapter for profile list
        ProfileList adapter = new ProfileList(this, equalizerProfiles);
        profileList.setAdapter(adapter);

        ActivityResultLauncher<Intent> eqActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intentRet = result.getData();
                        EqualizerProfile currentProfile = Objects.requireNonNull(intentRet)
                                .getParcelableExtra(Constants.define.INTENT_PARCELABLE_NAME);
                        int index = intentRet.getIntExtra(Constants.define.INTENT_INT_POSITION,
                                Constants.define.INTENT_INT_POSITION_DEFAULT);

                        // If the index received is -1, it means that it is a new configuration,
                        // so it saves a new profile
                        if(index > 0)
                        {
                            equalizerProfiles.set(index, currentProfile);
                        }
                        else {
                            equalizerProfiles.add(currentProfile);
                        }
                        // Update the ListView adapter
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, format(Locale.getDefault(), "%s",
                                getString(R.string.saved)), Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // Defines an action for a quick click in the listview
        profileList.setOnItemClickListener((parent, view, position, id) -> {
            EqualizerProfile profileSelected = equalizerProfiles.get(position);
            Toast.makeText(this, format(Locale.getDefault(),getString(R.string.current_profile),
                    profileSelected.getName()), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EqualizerActivity.class);
            intent.putExtra(Constants.define.INTENT_PARCELABLE_NAME, profileSelected);
            intent.putExtra(Constants.define.INTENT_INT_POSITION, position);
            intent.putExtra(Constants.define.INTENT_INT_SIZE, equalizerProfiles.size());

            eqActivity.launch(intent);
        });

        // Defines an action for a long click on a profile list
        profileList.setOnItemLongClickListener((parent, view, position, id) -> {
            String profileSelected = equalizerProfiles.get(position).getName();
            // If the selected item is different from the default profile, delete the profile from the list
            if(!profileSelected.equals(defaultEqProfile.getName())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(format(Locale.getDefault(), getString(R.string.exclusion)));
                builder.setMessage(format(Locale.getDefault(),getString(R.string.confirmation_question_delete),
                        equalizerProfiles.get(position).getName()));
                builder.setPositiveButton(
                        format(Locale.getDefault(), getString(R.string.positive_button_name)), (dialog, which) -> {
                    equalizerProfiles.remove(position);
                    adapter.notifyDataSetChanged();
                });
                builder.setNegativeButton(
                        format(Locale.getDefault(), getString(R.string.negative_button_name)), null);
                builder.show();
            } else {
                Toast.makeText(this, format(Locale.getDefault(),
                        getString(R.string.profile_cannot_deleted), profileSelected), Toast.LENGTH_SHORT).show();
            }
            return true;
        });

    }

}

