package com.leandromendes.vehicleequalizer.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.util.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EqualizerActivity : AppCompatActivity() {
    private lateinit var textViewBass: TextView
    private lateinit var textViewMid: TextView
    private lateinit var textViewTreble: TextView
    private lateinit var textViewBalance: TextView
    private var bassValue = 0
    private var midValue = 0
    private var highValue = 0
    private var balanceValue = 0
    private var masterVolValue = 0
    private lateinit var profileName: String
    private lateinit var saveButton: Button
    private var idPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_equalizer)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View?, insets: WindowInsetsCompat? ->
            val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
            v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the intent
        val intent = getIntent()

        // Gets all the parameters from the previous activity
        val currentProfile: EqualizerProfile? = intent.getParcelableExtra(
        Constants.define.INTENT_PARCELABLE_NAME,
        EqualizerProfile::class.java
        )
        idPosition = intent.getIntExtra(
            Constants.define.INTENT_INT_POSITION,
            Constants.define.INTENT_INT_POSITION_DEFAULT
        )

        // Get button layout
        val newProfileButton = findViewById<Button>(R.id.newProfile)
        val resetAllSettings = findViewById<Button>(R.id.reset)
        saveButton = findViewById(R.id.save)

        // Sets the name of the current profile on the screen
        profileName = currentProfile!!.name
        // Local variables
        val textViewProfile = findViewById<TextView>(R.id.profileSelected)
        textViewProfile.text = profileName

        // Sets the initial values of the equalization faders
        // Bass frequency
        bassValue = currentProfile.bassEqValue
        textViewBass = findViewById(R.id.textViewBass)
        drawTextOnTheBars(textViewBass, bassValue)
        val seekBarBass = findViewById<SeekBar>(R.id.seekBarBass)
        seekBarBass.progress = bassValue

        // Mid frequency
        midValue = currentProfile.midEqValue
        textViewMid = findViewById(R.id.textViewMid)
        drawTextOnTheBars(textViewMid, midValue)
        val seekBarMid = findViewById<SeekBar>(R.id.seekBarMid)
        seekBarMid.progress = midValue

        // High frequency
        highValue = currentProfile.hiEqValue
        textViewTreble = findViewById(R.id.textViewTreble)
        drawTextOnTheBars(textViewTreble, highValue)
        val seekBarTreble = findViewById<SeekBar>(R.id.seekBarTreble)
        seekBarTreble.progress = highValue

        // Balance
        balanceValue = currentProfile.balanceEqValue
        textViewBalance = findViewById(R.id.textViewBalance)
        drawTextOnThePanBar(textViewBalance, balanceValue)
        val seekBarBalance = findViewById<SeekBar>(R.id.seekBarBalance)
        seekBarBalance.progress = balanceValue

        // Master Volume
        masterVolValue = currentProfile.masterVolValue
        val textViewMasterVol = findViewById<TextView>(R.id.textViewVolume)
        drawTextOnTheVolBar(textViewMasterVol, masterVolValue)
        val seekBarMasterVol = findViewById<SeekBar>(R.id.seekBarVolume)
        seekBarMasterVol.progress = masterVolValue


        // Treatment for bass bar movement
        seekBarBass.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bassValue = progress
                drawTextOnTheBars(textViewBass, bassValue)
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true)
                resetAllSettings.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })

        // Treatment for mid bar movement
        seekBarMid.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                midValue = progress
                drawTextOnTheBars(textViewMid, midValue)
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true)
                resetAllSettings.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })

        // Treatment for high bar movement
        seekBarTreble.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                highValue = progress
                drawTextOnTheBars(textViewTreble, highValue)
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true)
                resetAllSettings.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })

        seekBarBalance.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                balanceValue = progress
                drawTextOnThePanBar(textViewBalance, balanceValue)
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true)
                resetAllSettings.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })

        seekBarMasterVol.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                masterVolValue = progress
                drawTextOnTheVolBar(textViewMasterVol, masterVolValue)
                // Only enable the save button if data has changed.
                saveButton.setEnabled(true)
                resetAllSettings.setEnabled(true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO
            }
        })

        // Create new profile button action
        newProfileButton.setOnClickListener { v: View? ->
            saveNewChangeProfile(
                currentProfile
            )
        }

        // Save profile settings button action
        saveButton.setOnClickListener { v: View? ->
            // The Default profile cannot be overwritten, if you press on the default profile,
            // it creates a new one.
            if (profileName == getString(R.string.default_profile)) {
                saveNewChangeProfile(currentProfile)
            } else {
                saveChangeProfile(currentProfile)
            }
        }

        // Reset all settings
        resetAllSettings.setOnClickListener { v: View? ->
            bassValue = currentProfile.bassEqValue
            drawTextOnTheBars(textViewBass, bassValue)
            seekBarBass.setProgress(bassValue, true)

            midValue = currentProfile.midEqValue
            drawTextOnTheBars(textViewMid, midValue)
            seekBarMid.setProgress(midValue, true)

            highValue = currentProfile.hiEqValue
            drawTextOnTheBars(textViewTreble, highValue)
            seekBarTreble.setProgress(highValue, true)

            balanceValue = currentProfile.balanceEqValue
            drawTextOnThePanBar(textViewBalance, balanceValue)
            seekBarBalance.setProgress(balanceValue, true)

            masterVolValue = currentProfile.masterVolValue
            drawTextOnTheVolBar(textViewMasterVol, masterVolValue)
            seekBarMasterVol.setProgress(masterVolValue, true)

            // Disable button
            resetAllSettings.setEnabled(false)
            saveButton.setEnabled(false)
        }
    }

    private fun saveNewChangeProfile(profile: EqualizerProfile) {
        val currentDate = Date()
        val format = SimpleDateFormat(Constants.define.DATETIME_FORMAT, Locale.getDefault())
        val dateTime = format.format(currentDate)

        profileName = String.format(
            Locale.getDefault(),
            "%s %s",
            getString(R.string.new_profile_button),
            dateTime
        )
        val builder = AlertDialog.Builder(this@EqualizerActivity)
        builder.setTitle(getString(R.string.dialogue_title))

        val view =
            LayoutInflater.from(this@EqualizerActivity).inflate(R.layout.dialog_text_input, null)
        val input = view.findViewById<EditText>(R.id.edit_text)
        input.setHint(profileName)
        builder.setView(view)

        builder.setPositiveButton(
            getString(R.string.agree_button_name)
        ) { dialog: DialogInterface?, which: Int ->
            val text = input.getText().toString()
            if (!text.isEmpty()) {
                profileName = text
            }
            idPosition = Constants.define.INTENT_INT_POSITION_DEFAULT
            saveChangeProfile(profile)
        }

        builder.setNegativeButton(
            getString(R.string.cancel_button_name)
        ) { dialog: DialogInterface?, which: Int -> dialog!!.cancel() }
        builder.show()
    }

    private fun saveChangeProfile(profile: EqualizerProfile) {
        profile.name = profileName
        profile.bassEqValue = bassValue
        profile.midEqValue = midValue
        profile.hiEqValue = highValue
        profile.balanceEqValue = balanceValue
        profile.masterVolValue = masterVolValue
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.define.INTENT_PARCELABLE_NAME, profile)
        resultIntent.putExtra(Constants.define.INTENT_INT_POSITION, idPosition)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun drawTextOnTheBars(textView: TextView, value: Int) {
        textView.text = String.format(
            Locale.getDefault(), "%s%s",
            (if (value > 0) ("+$value") else value), getString(R.string.decibel)
        )
    }

    @SuppressLint("SetTextI18n")
    private fun drawTextOnThePanBar(textView: TextView, value: Int) {
        val offset = 5
        val balance = (offset - value)
        textView.text = String.format(
            Locale.getDefault(), "%s",
            if (balance > 0) "+$balance" else if (balance < 0) "+" + (-balance) else getString(
                R.string.default_pan_value
            )
        )
    }

    private fun drawTextOnTheVolBar(textView: TextView, value: Int) {
        textView.text = String.format(Locale.getDefault(), "%s%s", value, getString(R.string.spl))
    }
}