package com.leandromendes.vehicleequalizer.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.util.Constants.Define
import com.leandromendes.vehicleequalizer.util.Constants.MusicConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EqualizerActivity : AppCompatActivity() {

    private lateinit var textViewBass: TextView
    private lateinit var textViewMid: TextView
    private lateinit var textViewTreble: TextView
    private lateinit var textViewBalance: TextView
    private lateinit var textViewMasterVol: TextView
    private lateinit var saveButton: Button
    private lateinit var resetButton: Button
    private lateinit var newProfileButton: Button
    private lateinit var equalizerSwitch: SwitchMaterial
    private lateinit var profileName: String
    private lateinit var currentProfile: EqualizerProfile
    private var idPosition = 0

    private var _bassValue = 0
    private var bassValue: Int
        get() = _bassValue
        set(value) {
            _bassValue = value
            saveButton.isEnabled = true
            resetButton.isEnabled = true
        }

    private var _midValue = 0
    private var midValue: Int
        get() = _midValue
        set(value) {
            _midValue = value
            saveButton.isEnabled = true
            resetButton.isEnabled = true
        }

    private var _highValue = 0
    private var highValue: Int
        get() = _highValue
        set(value) {
            _highValue = value
            saveButton.isEnabled = true
            resetButton.isEnabled = true
        }

    private var _balanceValue = 0
    private var balanceValue: Int
        get() = _balanceValue
        set(value) {
            _balanceValue = value
            saveButton.isEnabled = true
            resetButton.isEnabled = true
        }

    private var _masterVolValue = 0
    private var masterVolValue: Int
        get() = _masterVolValue
        set(value) {
            _masterVolValue = value
            saveButton.isEnabled = true
            resetButton.isEnabled = true
        }
    private lateinit var bassSeekBar: SeekBar
    private lateinit var midSeekBar: SeekBar
    private lateinit var trebleSeekBar: SeekBar
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var balanceSeekBar: SeekBar

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
        newProfileButton = findViewById<Button>(R.id.newProfile)
        resetButton = findViewById(R.id.reset)
        saveButton = findViewById(R.id.save)
        equalizerSwitch = findViewById(R.id.switchEqualizer)

        // Initialization of all TextViews
        val textViewProfile = findViewById<TextView>(R.id.profileSelected)
        textViewBass = findViewById(R.id.textViewBass)
        textViewMid = findViewById(R.id.textViewMid)
        textViewTreble = findViewById(R.id.textViewTreble)
        textViewBalance = findViewById(R.id.textViewBalance)
        textViewMasterVol = findViewById(R.id.textViewVolume)

        // Sets the name of the current profile on the screen
        profileName = currentProfile.name
        textViewProfile.text = profileName

        // Sets the initial values of the equalization faders
        // Bass frequency
        bassSeekBar = setupSeekBar(
            R.id.seekBarBass,
            MusicConstants.ACTION_SET_BASS,
            currentProfile.bassEqValue,
            textViewBass,
            ::drawTextOnTheBars
        ) { progress -> bassValue = progress }

        // Mid frequency
        midSeekBar = setupSeekBar(
            R.id.seekBarMid,
            MusicConstants.ACTION_SET_MID,
            currentProfile.midEqValue,
            textViewMid,
            ::drawTextOnTheBars
        ) { progress -> midValue = progress }

        // High frequency
       trebleSeekBar = setupSeekBar(
            R.id.seekBarTreble,
            MusicConstants.ACTION_SET_TREBLE,
            currentProfile.hiEqValue,
            textViewTreble,
            ::drawTextOnTheBars
        ) { progress -> highValue = progress }

        // Balance
       balanceSeekBar = setupSeekBar(
            R.id.seekBarBalance,
            MusicConstants.ACTION_SET_BALANCE,
            currentProfile.balanceEqValue,
            textViewBalance,
            ::drawTextOnThePanBar
        ) { progress -> balanceValue = progress }

        // Master Volume
       volumeSeekBar = setupSeekBar(
            R.id.seekBarVolume,
            MusicConstants.ACTION_SET_VOLUME,
            currentProfile.masterVolValue,
            textViewMasterVol,
            ::drawTextOnTheVolBar
        ) { progress -> masterVolValue = progress }


        // 🔹 Switch → ativa/desativa equalizer em tempo real
        equalizerSwitch.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent(this, AudioService::class.java).apply {
                action = MusicConstants.ACTION_ENABLE_EQUALIZER
                putExtra(MusicConstants.EXTRA_ENABLED, isChecked)
            }
            ContextCompat.startForegroundService(this, intent)
            // 🔑 Habilita/desabilita controles
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
            bassValue = currentProfile.bassEqValue
            (findViewById<SeekBar>(R.id.seekBarBass)).setProgress(bassValue, true)
            drawTextOnTheBars(textViewBass, bassValue)

            midValue = currentProfile.midEqValue
            (findViewById<SeekBar>(R.id.seekBarMid)).setProgress(midValue, true)
            drawTextOnTheBars(textViewMid, midValue)

            highValue = currentProfile.hiEqValue
            (findViewById<SeekBar>(R.id.seekBarTreble)).setProgress(highValue, true)
            drawTextOnTheBars(textViewTreble, highValue)

            balanceValue = currentProfile.balanceEqValue
            (findViewById<SeekBar>(R.id.seekBarBalance)).setProgress(balanceValue, true)
            drawTextOnThePanBar(textViewBalance, balanceValue)

            masterVolValue = currentProfile.masterVolValue
            (findViewById<SeekBar>(R.id.seekBarVolume)).setProgress(masterVolValue, true)
            drawTextOnTheVolBar(textViewMasterVol, masterVolValue)

            // Disables buttons after reset
            resetButton.isEnabled = false
            saveButton.isEnabled = false
        }
    }

    private fun setupSeekBar(
        seekBarId: Int,
        action: String,
        initialValue: Int,
        textView: TextView,
        drawTextFunc: (TextView, Int) -> Unit,
        onProgressUpdate: (Int) -> Unit
    ): SeekBar {
        val seekBar = findViewById<SeekBar>(seekBarId).apply {
            progress = initialValue
            drawTextFunc(textView, initialValue)

            setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    onProgressUpdate(progress)
                    drawTextFunc(textView, progress)
                    sendEqualizerCommand(action, progress) // 🔑 manda pro serviço em tempo real
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        return seekBar
    }

    /**
     * Envia comandos para o AudioService
     */
    private fun sendEqualizerCommand(action: String, level: Int) {
        val intent = Intent(this, AudioService::class.java).apply {
            this.action = action
            putExtra(MusicConstants.EXTRA_LEVEL, level)
        }
        ContextCompat.startForegroundService(this, intent)
    }

    private fun saveNewChangeProfile(profile: EqualizerProfile) {
        val currentDate = Date()
        val format = SimpleDateFormat(Define.DATETIME_FORMAT, Locale.getDefault())
        val dateTime = format.format(currentDate)

        val newProfileHint = String.format(
            Locale.getDefault(),
            "%s %s",
            getString(R.string.new_profile_button),
            dateTime
        )
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

    private fun saveChangeProfile(profile: EqualizerProfile) {
        profile.name = profileName
        profile.bassEqValue = bassValue
        profile.midEqValue = midValue
        profile.hiEqValue = highValue
        profile.balanceEqValue = balanceValue
        profile.masterVolValue = masterVolValue

        val resultIntent = Intent()
        resultIntent.putExtra(INTENT_PARCELABLE_NAME, profile)
        resultIntent.putExtra(INTENT_INT_POSITION, idPosition)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun drawTextOnTheBars(textView: TextView, value: Int) {

        textView.text = when {
            value > 0 -> "+$value${getString(R.string.decibel)}"
            else -> "$value${getString(R.string.decibel)}"
        }
    }

    private fun drawTextOnThePanBar(textView: TextView, value: Int) {
        val offset = 5
        val balance = offset - value

        textView.text = when {
            balance > 0 -> "+$balance"
            balance < 0 -> "+${-balance}"
            else -> getString(R.string.default_pan_value)
        }
    }

    private fun drawTextOnTheVolBar(textView: TextView, value: Int) {
        textView.text = "$value%"
    }

    private fun setControlsEnabled(enabled: Boolean) {
        bassSeekBar.isEnabled = enabled
        midSeekBar.isEnabled = enabled
        trebleSeekBar.isEnabled = enabled
        volumeSeekBar.isEnabled = enabled
        balanceSeekBar.isEnabled = enabled
        saveButton.isEnabled = enabled
        resetButton.isEnabled = enabled
        newProfileButton.isEnabled = enabled
    }

    companion object {
        const val INTENT_PARCELABLE_NAME = Define.INTENT_PARCELABLE_NAME
        const val INTENT_INT_POSITION = Define.INTENT_INT_POSITION

        /**
         * Creates an Intent to launch EqualizerActivity, encapsulating the extras.
         */
        fun newIntent(context: Context, profile: EqualizerProfile, position: Int): Intent {
            return Intent(context, EqualizerActivity::class.java).apply {
                putExtra(INTENT_PARCELABLE_NAME, profile)
                putExtra(INTENT_INT_POSITION, position)
            }
        }

        /**
         * Extracts the result Intent profile used by the calling Activity
         */
        fun getResultProfile(intent: Intent): EqualizerProfile? {
            return intent.getParcelableExtra(INTENT_PARCELABLE_NAME, EqualizerProfile::class.java)
        }

        /**
         * Extracts the position of the result Intent used by the calling Activity
         */
        fun getResultPosition(intent: Intent): Int {
            return intent.getIntExtra(INTENT_INT_POSITION, Define.INTENT_INT_POSITION_DEFAULT)
        }
    }
}