package com.leandromendes.audioequalizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EqualizerActivity extends AppCompatActivity {

    private TextView textViewProfile;
    private TextView textViewBass;
    private TextView textViewMid;
    private TextView textViewTreble;

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

        textViewProfile = findViewById(R.id.profileSelected);
        textViewBass = findViewById(R.id.textViewBass);
        textViewMid = findViewById(R.id.textViewMid);
        textViewTreble = findViewById(R.id.textViewTreble);

        textViewProfile.setText(currentProfile.getName());

        textViewBass.setText("Bass " + currentProfile.getBassEqValue() + "db");
        SeekBar seekBarBass = findViewById(R.id.seekBarBass);
        seekBarBass.setProgress(currentProfile.getBassEqValue());

        textViewMid.setText("Mid " + currentProfile.getMidEqValue() + "db");
        SeekBar seekBarMid = findViewById(R.id.seekBarMid);
        seekBarMid.setProgress(currentProfile.getMidEqValue());

        textViewTreble.setText("Treble " + currentProfile.getHiEqValue() + "db");
        SeekBar seekBarTreble = findViewById(R.id.seekBarTreble);
        seekBarTreble.setProgress(currentProfile.getHiEqValue());

        seekBarBass.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewBass.setText("Bass " + progress + "db");
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
                textViewMid.setText("Mid " + progress + "db");
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
                textViewTreble.setText("Treble " + progress + "db");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}