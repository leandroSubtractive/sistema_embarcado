package com.leandromendes.vehicleequalizer.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.util.Constants.Define
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EqualizerActivity : AppCompatActivity() {
    private val logTAG = "EqualizerActivity"
    private lateinit var currentProfile: EqualizerProfile
    private lateinit var newProfileButton: Button
    private lateinit var saveButton: Button
    private lateinit var resetButton: Button
    private lateinit var equalizerSwitch: SwitchMaterial
    private lateinit var profileName: String
    private var band0Value = 0
    private var band1Value = 0
    private var band2Value = 0
    private var band3Value = 0
    private var band4Value = 0
    private var masterVolValue = 0
    private var idPosition = 0
    private lateinit var textViewMasterVol: TextView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var seekBarsBand0: VerticalSeekBar
    private lateinit var seekBarsBand1: VerticalSeekBar
    private lateinit var seekBarsBand2: VerticalSeekBar
    private lateinit var seekBarsBand3: VerticalSeekBar
    private lateinit var seekBarsBand4: VerticalSeekBar

    /**
     * Update ui receiver
     */
    private val updateUIReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MusicConstants.ACTION_UPDATE_UI) {
                val isEnabled =
                    intent.getBooleanExtra(MusicConstants.EXTRA_EQUALIZER_ENABLED, false)
                Log.d(logTAG, "Broadcast received - Equalizer enabled? $isEnabled")

                // Updates the switch without infinite loop
                if (equalizerSwitch.isChecked != isEnabled) {
                    equalizerSwitch.isChecked = isEnabled
                }

                setControlsEnabled(isEnabled)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_equalizer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Gets all the parameters from the previous activity
        currentProfile = requireNotNull(
            intent.getParcelableExtra(
                INTENT_PARCELABLE_NAME, // Companion object key
                EqualizerProfile::class.java
            )
        )
        idPosition = intent.getIntExtra(
            INTENT_INT_POSITION, // Companion object key
            Define.INTENT_INT_POSITION_DEFAULT
        )

        // Initialize Views
        newProfileButton = findViewById(R.id.newProfile)
        resetButton = findViewById(R.id.reset)
        saveButton = findViewById(R.id.save)
        equalizerSwitch = findViewById(R.id.switchEqualizer)

        // Initialization of all TextViews
        val textViewProfile = findViewById<TextView>(R.id.profileSelected)
        textViewMasterVol = findViewById(R.id.textViewVolume)

        // Sets the name of the current profile on the screen
        profileName = currentProfile.name
        textViewProfile.text = profileName

        // Sets the initial values of the equalization faders
        seekBarsBand0 = findViewById(R.id.seekBarBand1)
        band0Value = currentProfile.band0
        setupSeekBar(
            seekBarsBand0,
            MusicConstants.ACTION_SET_BAND_LEVEL,
            0,
            band0Value,
            findViewById(R.id.GainBand1),
            ::drawTextOnTheBars
        ) { progress -> band0Value = progress }

        seekBarsBand1 = findViewById(R.id.seekBarBand2)
        band1Value = currentProfile.band1
        setupSeekBar(
            seekBarsBand1,
            MusicConstants.ACTION_SET_BAND_LEVEL,
            1,
            currentProfile.band1,
            findViewById(R.id.GainBand2),
            ::drawTextOnTheBars
        ) { progress -> band1Value = progress }

        seekBarsBand2 = findViewById(R.id.seekBarBand3)
        band2Value = currentProfile.band2
        setupSeekBar(
            seekBarsBand2,
            MusicConstants.ACTION_SET_BAND_LEVEL,
            2,
            band2Value,
            findViewById(R.id.GainBand3),
            ::drawTextOnTheBars
        ) { progress -> band2Value = progress }

        seekBarsBand3 = findViewById(R.id.seekBarBand4)
        band3Value = currentProfile.band3
        setupSeekBar(
            seekBarsBand3,
            MusicConstants.ACTION_SET_BAND_LEVEL,
            3,
            band3Value,
            findViewById(R.id.GainBand4),
            ::drawTextOnTheBars
        ) { progress -> band3Value = progress }

        seekBarsBand4 = findViewById(R.id.seekBarBand5)
        band4Value = currentProfile.band4
        setupSeekBar(
            seekBarsBand4,
            MusicConstants.ACTION_SET_BAND_LEVEL,
            4,
            band4Value,
            findViewById(R.id.GainBand5),
            ::drawTextOnTheBars
        ) { progress -> band4Value = progress }

        // Master Volume
        volumeSeekBar = findViewById(R.id.seekBarVolume)
        masterVolValue = currentProfile.masterVolValue
        setupSeekBar(
            volumeSeekBar,
            MusicConstants.ACTION_SET_VOLUME,
            0,
            masterVolValue,
            textViewMasterVol,
            ::drawTextOnTheVolBar
        ) { progress -> masterVolValue = progress }


        // Enable/Disable equalizer
        equalizerSwitch.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent(this, AudioService::class.java).apply {
                action = MusicConstants.ACTION_ENABLE_EQUALIZER
                putExtra(MusicConstants.EXTRA_ENABLED, isChecked)
            }
            ContextCompat.startForegroundService(this, intent)
            // Enables/disables controls
            setControlsEnabled(isChecked)
        }


        // Create new profile button action
        newProfileButton.setOnClickListener {
            saveNewChangeProfile(currentProfile)
        }

        // Save profile settings button action
        saveButton.setOnClickListener {
            if (profileName == getString(R.string.default_profile)) {
                saveNewChangeProfile(currentProfile)
            } else {
                saveChangeProfile(currentProfile)
            }
        }

        // Reset all settings to the saved value
        resetButton.setOnClickListener {

            band0Value = currentProfile.band0
            resetBand(0, band0Value, seekBarsBand0, findViewById(R.id.GainBand1))

            band1Value = currentProfile.band1
            resetBand(1, band1Value, seekBarsBand1, findViewById(R.id.GainBand2))

            band2Value = currentProfile.band2
            resetBand(2, band2Value, seekBarsBand2, findViewById(R.id.GainBand3))

            band3Value = currentProfile.band3
            resetBand(3, band3Value, seekBarsBand3, findViewById(R.id.GainBand4))

            band4Value = currentProfile.band4
            resetBand(4, band4Value, seekBarsBand4, findViewById(R.id.GainBand5))

            masterVolValue = currentProfile.masterVolValue
            volumeSeekBar.setProgress(masterVolValue, true)
            drawTextOnTheVolBar(textViewMasterVol, masterVolValue)
            sendEqualizerCommand(MusicConstants.ACTION_SET_VOLUME, 0, masterVolValue)

            // Disables buttons after reset
            resetButton.isEnabled = false
            saveButton.isEnabled = false
        }

        sendEqualizerCommand(MusicConstants.ACTION_EQUALIZER_STATUS, 0, 0)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(MusicConstants.ACTION_UPDATE_UI)
        registerReceiver(updateUIReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(updateUIReceiver)
    }

    /**
     * Setup seek bar
     *
     * @param seekBar SeekBar to be configured
     * @param action Action command that will be sent to the audio service when the seekBar is changed
     * @param band Frequency band ID
     * @param initialValue Initial value of SeekBar
     * @param textView SeekBar TextView
     * @param drawTextFunc Auxiliary function for drawing the parameter on the screen
     * @param onProgressUpdate Variable that stores the current value
     * @receiver
     * @receiver
     */
    private fun setupSeekBar(
        seekBar: SeekBar,
        action: String,
        band: Int,
        initialValue: Int,
        textView: TextView,
        drawTextFunc: (TextView, Int) -> Unit,
        onProgressUpdate: (Int) -> Unit
    ) {
        seekBar.progress = initialValue
        drawTextFunc(textView, initialValue)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Updates the current value of the corresponding seekbar position
                onProgressUpdate(progress)
                // Draw the SeekBar value on the screen with the appropriate formatting
                drawTextFunc(textView, progress)

                /* Sends a change to the audio service that is responsible
                for changing the value in the equalizer */
                sendEqualizerCommand(action, band, progress)

                /* Before activating the save and reset buttons, check
                if they are already activated. This check occurs when a seekbar is changed */
                if (!saveButton.isEnabled) saveButton.isEnabled = true
                if (!resetButton.isEnabled) resetButton.isEnabled = true
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    /**
     * Send equalizer command
     *
     * @param action Action command that will be sent to the audio service
     * @param band Frequency band [0 - 4]
     * @param level Gain
     */
    private fun sendEqualizerCommand(action: String, band: Int, level: Int) {
        val intent = Intent(this, AudioService::class.java).apply {
            this.action = action
            putExtra(MusicConstants.EXTRA_BAND, band) // Band ID
            putExtra(MusicConstants.EXTRA_LEVEL, level)// Gain in dB
        }
        // Send action to service
        ContextCompat.startForegroundService(this, intent)
    }

    /**
     * Save new change profile
     *
     * @param profile Profile to be saved
     */
    private fun saveNewChangeProfile(profile: EqualizerProfile) {
        val currentDate = Date()
        val format = SimpleDateFormat(Define.DATETIME_FORMAT, Locale.getDefault())
        val dateTime = format.format(currentDate)

        // Creates default name with date and time
        val newProfileHint = String.format(
            Locale.getDefault(),
            "%s %s",
            getString(R.string.new_profile_button),
            dateTime
        )

        /* Creates a dialog box for the user to enter a name for the profile;
        otherwise, uses the default name. */
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialogue_title))

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_text_input, null)
        val input = view.findViewById<EditText>(R.id.edit_text)
        input.setHint(newProfileHint)
        builder.setView(view)

        builder.setPositiveButton(getString(R.string.agree_button_name)) { dialog: DialogInterface?, which: Int ->
            val text = input.text.toString()
            profileName = text.ifEmpty {
                newProfileHint
            }
            idPosition = Define.INTENT_INT_POSITION_DEFAULT
            saveChangeProfile(profile)
        }

        builder.setNegativeButton(getString(R.string.cancel_button_name)) { dialog: DialogInterface?, which: Int ->
            dialog!!.cancel()
        }
        builder.show()
    }

    /**
     * Save change profile
     *
     * @param profile Profile to be saved
     */
    private fun saveChangeProfile(profile: EqualizerProfile) {
        profile.name = profileName
        profile.band0 = band0Value
        profile.band1 = band1Value
        profile.band2 = band2Value
        profile.band3 = band3Value
        profile.band4 = band4Value
        profile.masterVolValue = masterVolValue

        val resultIntent = Intent()
        resultIntent.putExtra(INTENT_PARCELABLE_NAME, profile)
        resultIntent.putExtra(INTENT_INT_POSITION, idPosition)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    /**
     * Draw text on the bars
     *
     * @param textView Corresponding TextView
     * @param value Gain value
     */
    private fun drawTextOnTheBars(textView: TextView, value: Int) {

        textView.text = when {
            value > 0 -> "+$value${getString(R.string.decibel)}"
            else -> "$value${getString(R.string.decibel)}"
        }
    }

    /**
     * Draw text on the vol bar
     *
     * @param textView Corresponding TextView
     * @param value Volume value
     */
    private fun drawTextOnTheVolBar(textView: TextView, value: Int) {
        textView.text = getString(R.string.percentage_value, value)
    }

    /**
     * Set controls enabled
     *
     * @param enabled Command to activate or deactivate
     */
    private fun setControlsEnabled(enabled: Boolean) {
        seekBarsBand0.isEnabled = enabled
        seekBarsBand2.isEnabled = enabled
        seekBarsBand3.isEnabled = enabled
        seekBarsBand4.isEnabled = enabled
        seekBarsBand1.isEnabled = enabled
        volumeSeekBar.isEnabled = enabled
        newProfileButton.isEnabled = enabled

        /* Enabling or disabling the equalizer only disables these buttons;
        the only thing that is enabled is user input */
        saveButton.isEnabled = if (!enabled) false else saveButton.isEnabled
        resetButton.isEnabled = if (!enabled) false else resetButton.isEnabled
    }

    /**
     * Reset band
     *  Restores the gain values to the profile value before editing
     *  in each frequency band of the equalizer
     *
     * @param bandIndex Frequency band ID: [0 - 4]
     * @param savedValue Amount before editing
     * @param seekBar Corresponding SeekBar
     * @param gainTextView Corresponding TextView
     */
    private fun resetBand(
        bandIndex: Int,
        savedValue: Int,
        seekBar: SeekBar,
        gainTextView: TextView
    ) {
        seekBar.setProgress(savedValue, true)
        drawTextOnTheBars(gainTextView, savedValue)
        sendEqualizerCommand(MusicConstants.ACTION_SET_BAND_LEVEL, bandIndex, savedValue)
    }

    companion object {
        const val INTENT_PARCELABLE_NAME = Define.INTENT_PARCELABLE_NAME
        const val INTENT_INT_POSITION = Define.INTENT_INT_POSITION

        /**
         * New intent
         * Creates an Intent to launch EqualizerActivity, encapsulating the extras.
         *
         * @param context Current context
         * @param profile Equalization profile
         * @param position Position of the profile ID in the list
         * @return Returns a new intent
         */
        fun newIntent(context: Context, profile: EqualizerProfile, position: Int): Intent {
            return Intent(context, EqualizerActivity::class.java).apply {
                putExtra(INTENT_PARCELABLE_NAME, profile)
                putExtra(INTENT_INT_POSITION, position)
            }
        }

        /**
         * Get result profile
         * Extracts the result Intent profile used by the calling Activity
         *
         * @param intent Intention to extract the profile
         * @return Return extracted profile
         */
        fun getResultProfile(intent: Intent): EqualizerProfile? {
            return intent.getParcelableExtra(INTENT_PARCELABLE_NAME, EqualizerProfile::class.java)
        }

        /**
         * Get result position
         * Extracts the position of the result Intent used by the calling Activity
         *
         * @param intent Intention to extract the ID
         * @return Returns the extracted ID
         */
        fun getResultPosition(intent: Intent): Int {
            return intent.getIntExtra(INTENT_INT_POSITION, Define.INTENT_INT_POSITION_DEFAULT)
        }
    }
}