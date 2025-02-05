package com.leandromendes.audioequalizer.ui;


import static java.lang.String.*;

import android.annotation.SuppressLint;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.leandromendes.audioequalizer.data.model.EqualizerProfile;
import com.leandromendes.audioequalizer.R;
import com.leandromendes.audioequalizer.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EqualizerActivity extends AppCompatActivity {
    private TextView textViewBass;
    private TextView textViewMid;
    private TextView textViewTreble;
    private TextView textViewBalance;
    private int bassValue;
    private int midValue;
    private int highValue;
    private int balanceValue;
    private int masterVolValue;
    private String profileName;
    private EqualizerProfile currentProfile;
    private Button saveButton;
    private int idPosition;

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
        Intent intent = getIntent();

        // Gets all the parameters from the previous activity
        currentProfile = intent.getParcelableExtra(Constants.define.INTENT_PARCELABLE_NAME);
        idPosition = intent.getIntExtra(Constants.define.INTENT_INT_POSITION,
                Constants.define.INTENT_INT_POSITION_DEFAULT);

        // Get button layout
        Button newProfileButton = findViewById(R.id.newProfile);
        Button resetAllSettings = findViewById(R.id.reset);
        saveButton = findViewById(R.id.save);

        // Sets the name of the current profile on the screen
        profileName = currentProfile.getName();
        // Local variables
        TextView textViewProfile = findViewById(R.id.profileSelected);
        textViewProfile.setText(profileName);

        // Sets the initial values of the equalization faders
        // Bass frequency
        bassValue = currentProfile.getBassEqValue();
        textViewBass = findViewById(R.id.textViewBass);
        DrawTextOnTheBars(textViewBass, bassValue);
        SeekBar seekBarBass = findViewById(R.id.seekBarBass);
        seekBarBass.setProgress(bassValue);

        // Mid frequency
        midValue = currentProfile.getMidEqValue();
        textViewMid = findViewById(R.id.textViewMid);
        DrawTextOnTheBars(textViewMid, midValue);
        SeekBar seekBarMid = findViewById(R.id.seekBarMid);
        seekBarMid.setProgress(midValue);

        // High frequency
        highValue = currentProfile.getHiEqValue();
        textViewTreble = findViewById(R.id.textViewTreble);
        DrawTextOnTheBars(textViewTreble, highValue);
        SeekBar seekBarTreble = findViewById(R.id.seekBarTreble);
        seekBarTreble.setProgress(highValue);

        // Balance
        balanceValue = currentProfile.getBalanceEqValue();
        textViewBalance = findViewById(R.id.textViewBalance);
        DrawTextOnThePanBar(textViewBalance, balanceValue);
        SeekBar seekBarBalance = findViewById(R.id.seekBarBalance);
        seekBarBalance.setProgress(balanceValue);

        // Master Volume
        masterVolValue = currentProfile.getMasterVolValue();
        TextView textViewMasterVol = findViewById(R.id.textViewVolume);
        DrawTextOnTheVolBar(textViewMasterVol, masterVolValue);
        SeekBar seekBarMasterVol = findViewById(R.id.seekBarVolume);
        seekBarMasterVol.setProgress(masterVolValue);


        // Treatment for bass bar movement
        seekBarBass.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassValue = progress;
                DrawTextOnTheBars(textViewBass, bassValue);
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
                resetAllSettings.setEnabled(true);
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
                DrawTextOnTheBars(textViewMid, midValue);
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
                resetAllSettings.setEnabled(true);
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
                DrawTextOnTheBars(textViewTreble, highValue);
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
                resetAllSettings.setEnabled(true);
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

        seekBarBalance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                balanceValue = progress;
                DrawTextOnThePanBar(textViewBalance, balanceValue);
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
                resetAllSettings.setEnabled(true);
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

        seekBarMasterVol.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                masterVolValue = progress;
                DrawTextOnTheVolBar(textViewMasterVol, masterVolValue);
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true);
                resetAllSettings.setEnabled(true);
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
        newProfileButton.setOnClickListener(v -> saveNewChangeProfile(currentProfile));

        // Save profile settings button action
        saveButton.setOnClickListener(v -> {
            // The Default profile cannot be overwritten, if you press on the default profile,
            // it creates a new one.
            if (profileName.equals(getString(R.string.default_profile))) {
                saveNewChangeProfile(currentProfile);
            } else {
                saveChangeProfile(currentProfile);
            }
        });

        // Reset all settings
        resetAllSettings.setOnClickListener(v -> {

            bassValue = currentProfile.getBassEqValue();
            DrawTextOnTheBars(textViewBass, bassValue);
            seekBarBass.setProgress(bassValue, true);

            midValue = currentProfile.getMidEqValue();
            DrawTextOnTheBars(textViewMid, midValue);
            seekBarMid.setProgress(midValue, true);

            highValue = currentProfile.getHiEqValue();
            DrawTextOnTheBars(textViewTreble, highValue);
            seekBarTreble.setProgress(highValue, true);

            balanceValue = currentProfile.getBalanceEqValue();
            DrawTextOnThePanBar(textViewBalance, balanceValue);
            seekBarBalance.setProgress(balanceValue, true);

            masterVolValue = currentProfile.getMasterVolValue();
            DrawTextOnTheVolBar(textViewMasterVol, masterVolValue);
            seekBarMasterVol.setProgress(masterVolValue, true);

            // Disable button
            resetAllSettings.setEnabled(false);
            saveButton.setEnabled(false);

        });
    }

    private void saveNewChangeProfile(EqualizerProfile profile) {

        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat(Constants.define.DATETIME_FORMAT, Locale.getDefault());
        String dateTime = format.format(currentDate);

        profileName = format(Locale.getDefault(),"%s %s", getString(R.string.new_profile_button), dateTime);
        AlertDialog.Builder builder = new AlertDialog.Builder(EqualizerActivity.this);
        builder.setTitle(getString(R.string.dialogue_title));

        View view = LayoutInflater.from(EqualizerActivity.this).inflate(R.layout.dialog_text_input, null);
        final EditText input = view.findViewById(R.id.edit_text);
        input.setHint(profileName);
        builder.setView(view);

        builder.setPositiveButton(getString(R.string.agree_button_name), (dialog, which) -> {
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                profileName = text;
            }
            idPosition = Constants.define.INTENT_INT_POSITION_DEFAULT;
            saveChangeProfile(profile);
        });

        builder.setNegativeButton(getString(R.string.cancel_button_name), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void saveChangeProfile(@NonNull EqualizerProfile profile) {
        profile.setName(profileName);
        profile.setBassEqValue(bassValue);
        profile.setMidEqValue(midValue);
        profile.setHiEqValue(highValue);
        profile.setBalanceEqValue(balanceValue);
        profile.setMasterVolValue(masterVolValue);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.define.INTENT_PARCELABLE_NAME, profile);
        resultIntent.putExtra(Constants.define.INTENT_INT_POSITION, idPosition);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void DrawTextOnTheBars(@NonNull TextView textView, int value) {
        textView.setText(format(Locale.getDefault(),"%s%s",
                ((value > 0) ? ("+" + value) : value), getString(R.string.decibel)));
    }

    @SuppressLint("SetTextI18n")
    private void DrawTextOnThePanBar(@NonNull TextView textView, int value) {
        int offset = 5;
        int balance = (offset - value);
        textView.setText(format(Locale.getDefault(),"%s",
                (balance > 0) ? "+" + balance:
                (balance < 0) ? "+" + (-balance) : getString(R.string.default_pan_value)));
    }

    private void DrawTextOnTheVolBar(@NonNull TextView textView, int value){
        textView.setText(format(Locale.getDefault(),"%s%s", value, getString(R.string.spl)));
    }

}