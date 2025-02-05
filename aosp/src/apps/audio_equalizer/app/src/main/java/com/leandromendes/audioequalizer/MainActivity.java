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
import androidx.lifecycle.ViewModelProvider;

import com.leandromendes.audioequalizer.data.model.EqualizerProfile;
import com.leandromendes.audioequalizer.ui.EqualizerActivity;
import com.leandromendes.audioequalizer.util.Constants;
import com.leandromendes.audioequalizer.util.ProfileListUtils;
import com.leandromendes.audioequalizer.ui.MainViewModel;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;

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

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Define the title of the list of profile names
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(
                format(Locale.getDefault(), getString(R.string.toolbar_name)));

        // Initialize adapter for profile list
        ListView profileList = findViewById(R.id.profileList);
        ProfileListUtils adapter = new ProfileListUtils(this, mainViewModel.GetAllEqualizerProfiles());
        profileList.setAdapter(adapter);

        mainViewModel.getToastText().observe(this, this::ToastShow);

        ActivityResultLauncher<Intent> eqActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intentRet = result.getData();

                        EqualizerProfile currentProfile = Objects
                                .requireNonNull(intentRet)
                                .getParcelableExtra(Constants.define.INTENT_PARCELABLE_NAME);

                        int position = intentRet.getIntExtra(Constants.define.INTENT_INT_POSITION,
                                Constants.define.INTENT_INT_POSITION_DEFAULT);

                        // If the index received is -1, it means that it is a new configuration,
                        // so it saves a new profile
                        if(position == Constants.define.NEW_PROFILE)
                        {
                            mainViewModel.AddProfile(currentProfile);
                        }
                        else {
                            mainViewModel.UpdateProfile(position, currentProfile);
                        }

                        // Update the ListView adapter
                        adapter.notifyDataSetChanged();
                        mainViewModel.SetToastText(format(Locale.getDefault(), "%s", getString(R.string.saved)));
                    }
                }
        );

        // Defines an action for a quick click in the listview
        profileList.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, EqualizerActivity.class);
            intent.putExtra(Constants.define.INTENT_PARCELABLE_NAME, mainViewModel.GetEqualizerProfiles(position));
            intent.putExtra(Constants.define.INTENT_INT_POSITION, position);
            intent.putExtra(Constants.define.INTENT_INT_SIZE, mainViewModel.GetAllEqualizerProfiles().size());

            eqActivity.launch(intent);

            String text = format(Locale.getDefault(),getString(R.string.current_profile),
                    mainViewModel.GetEqualizerProfiles(position).getName());

            mainViewModel.SetToastText(text);
        });

        // Defines an action for a long click on a profile list
        profileList.setOnItemLongClickListener((parent, view, position, id) -> {
            String NameProfileSelected = mainViewModel.GetEqualizerProfiles(position).getName();
            // If the selected item is different from the default profile, delete the profile from the list
            if(!NameProfileSelected.equals(Constants.define.PROFILE_DEFAULT_NAME)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(format(Locale.getDefault(), getString(R.string.exclusion)));
                builder.setMessage(format(Locale.getDefault(),getString(R.string.confirmation_question_delete),
                        mainViewModel.GetEqualizerProfiles(position).getName()));
                builder.setPositiveButton(
                        format(Locale.getDefault(), getString(R.string.positive_button_name)), (dialog, which) -> {

                            mainViewModel.RemoveProfile(position);
                            adapter.notifyDataSetChanged();
                        });
                builder.setNegativeButton(
                        format(Locale.getDefault(), getString(R.string.negative_button_name)), null);
                builder.show();
            } else {
                String text = format(Locale.getDefault(), getString(R.string.profile_cannot_deleted), NameProfileSelected);
                ToastShow(text);
            }
            return true;
        });
    }

    public void ToastShow(String Text)
    {
        Toast.makeText(this, format(Locale.getDefault(), Text), Toast.LENGTH_SHORT).show();
    }

}

