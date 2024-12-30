package com.leandromendes.audioequalizer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EqualizerActivity extends AppCompatActivity {
    // Private variables
    private TextView textViewProfile;
    private TextView textViewBass;
    private TextView textViewMid;
    private TextView textViewTreble;
    private int bassValue;
    private int midValue;
    private int highValue;
    private int idPosition;
    private int countName;
    private String profileName;
    private EqualizerProfile currentProfile;
    private  Intent intent;
    private Button saveButton;
    private Button newProfileButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_equalizer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the intent
        intent = getIntent();

        // Gets all the parameters from the previous activity
        currentProfile = intent.getParcelableExtra("profile");
        idPosition = intent.getIntExtra("position",-1);
        countName = intent.getIntExtra("size", 0);

        // Get button layout
        newProfileButton = findViewById(R.id.newProfileb);
        saveButton = findViewById(R.id.save);

        // Gets view of text boxes
        textViewProfile = findViewById(R.id.profileSelected);
        textViewBass = findViewById(R.id.textViewBass);
        textViewMid = findViewById(R.id.textViewMid);
        textViewTreble = findViewById(R.id.textViewTreble);

        // Sets the name of the current profile on the screen
        profileName = currentProfile.getName();
        textViewProfile.setText(profileName);

        // Sets the initial values of the equalization faders
        // Bass frequency
        bassValue = currentProfile.getBassEqValue();
        textViewBass.setText("Bass " + bassValue + "db");
        SeekBar seekBarBass = findViewById(R.id.seekBarBass);
        seekBarBass.setProgress(currentProfile.getBassEqValue());

        // Mid frequency
        midValue = currentProfile.getMidEqValue();
        textViewMid.setText("Mid " + midValue + "db");
        SeekBar seekBarMid = findViewById(R.id.seekBarMid);
        seekBarMid.setProgress(currentProfile.getMidEqValue());

        // High frequency
        highValue = currentProfile.getHiEqValue();
        textViewTreble.setText("Treble " + highValue + "db");
        SeekBar seekBarTreble = findViewById(R.id.seekBarTreble);
        seekBarTreble.setProgress(currentProfile.getHiEqValue());

        // Treatment for bass bar movement
        seekBarBass.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassValue = progress;
                textViewBass.setText("Bass " + bassValue + "db");
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO
            }
        });

        // Treatment for mid bar movement
        seekBarMid.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                midValue = progress;
                textViewMid.setText("Mid " + midValue + "db");
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO
            }
        });

        // Treatment for high bar movement
        seekBarTreble.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                highValue = progress;
                textViewTreble.setText("Treble " + highValue + "db");
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO
            }
        });

        // Create new profile button action
        newProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewChangeProfile();
                }
        });

        // Save profile settings button action
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The Default profile cannot be overwritten, if you press on the default profile,
                // it creates a new one.
                if(profileName.equals("Default"))
                {
                    saveNewChangeProfile();
                }
                else {
                    saveChangeProfile();
                }
            }
        });
    }

    private void saveNewChangeProfile(){
        profileName = "New Profile"+ (countName);
        AlertDialog.Builder builder = new AlertDialog.Builder(EqualizerActivity.this);
        builder.setTitle("Profile Name");

        View view = LayoutInflater.from(EqualizerActivity.this).inflate(R.layout.dialog_text_input, null);
        final EditText input = view.findViewById(R.id.edit_text);
        input.setHint(profileName);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (!text.isEmpty())
                {
                    profileName = text;
                }
                idPosition = -1;
                saveChangeProfile();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void saveChangeProfile(){
        currentProfile.setName(profileName);
        currentProfile.setBassEqValue(bassValue);
        currentProfile.setMidEqValue(midValue);
        currentProfile.setHiEqValue(highValue);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("profile", currentProfile);
        resultIntent.putExtra("position", idPosition);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}