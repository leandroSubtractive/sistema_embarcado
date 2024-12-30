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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EqualizerActivity extends AppCompatActivity {

    private TextView textViewProfile;
    private TextView textViewBass;
    private TextView textViewMid;
    private TextView textViewTreble;
    private int bassValue;
    private int midValue;
    private int highValue;
    private int id_position;

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

        Intent intent = getIntent();
        EqualizerProfile currentProfile = intent.getParcelableExtra("profile");
        id_position = intent.getIntExtra("position",-1);

        Button saveButton = findViewById(R.id.save);
        textViewProfile = findViewById(R.id.profileSelected);
        textViewBass = findViewById(R.id.textViewBass);
        textViewMid = findViewById(R.id.textViewMid);
        textViewTreble = findViewById(R.id.textViewTreble);

        textViewProfile.setText(currentProfile.getName());

        bassValue = currentProfile.getBassEqValue();
        textViewBass.setText("Bass " + bassValue + "db");
        SeekBar seekBarBass = findViewById(R.id.seekBarBass);
        seekBarBass.setProgress(currentProfile.getBassEqValue());

        midValue = currentProfile.getMidEqValue();
        textViewMid.setText("Mid " + midValue + "db");
        SeekBar seekBarMid = findViewById(R.id.seekBarMid);
        seekBarMid.setProgress(currentProfile.getMidEqValue());

        highValue = currentProfile.getHiEqValue();
        textViewTreble.setText("Treble " + highValue + "db");
        SeekBar seekBarTreble = findViewById(R.id.seekBarTreble);
        seekBarTreble.setProgress(currentProfile.getHiEqValue());

        seekBarBass.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bassValue = progress;
                textViewBass.setText("Bass " + bassValue + "db");
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMid.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                midValue = progress;
                textViewMid.setText("Mid " + midValue + "db");
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTreble.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                highValue = progress;
                textViewTreble.setText("Treble " + highValue + "db");
                saveButton.setEnabled(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    currentProfile.setName("Profile"+(id_position+1));
                    currentProfile.setBassEqValue(bassValue);
                    currentProfile.setMidEqValue(midValue);
                    currentProfile.setHiEqValue(highValue);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("profile", currentProfile);
                    resultIntent.putExtra("position", id_position);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });

    }
}