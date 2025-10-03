package com.leandromendes.vehicleequalizer

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.modules.playback.PlaybackModule
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.ui.EqualizerActivity
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModel
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModelFactory
import com.leandromendes.vehicleequalizer.util.Constants
import com.leandromendes.vehicleequalizer.util.ProfileRecyclerViewAdapter



class MainActivity : AppCompatActivity() {
    
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ProfileRecyclerViewAdapter
    private val logTAG = "VehicleEqualizerApp"

    private lateinit var playbackModule: PlaybackModule
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button

    private lateinit var seekBar: SeekBar
    private lateinit var timeText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var isUserSeeking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View?, insets: WindowInsetsCompat? ->
            val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
            v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtain the Application, Database, and Repository to inject into the ViewModel
        val application = application as ProfileApplication // Casting for the new Application class
        val factory = MainViewModelFactory(application.repository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java] // Use this Factory

        /**
         * Registers a callback to start an Activity
         * and handle its result (the returned data) asynchronously.
         *
         * The variable ‘eqActivity’ is the launcher that will be used to start the Activity.
         */
        val eqActivity = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult? ->
            // Checks if the Activity result was successful
            if (result!!.resultCode == RESULT_OK && result.data != null) {
                // Get the return Intent
                val intentRet = result.data!!
                // Extract the EqualizerProfile object
                val currentProfile = EqualizerActivity.getResultProfile(intentRet)
                // Extracts the position of the profile in the list.
                val position = EqualizerActivity.getResultPosition(intentRet)

                // Verify that the profile was successfully extracted
                if (currentProfile == null) {
                    Log.e(logTAG, "Error retrieving EqualizerProfile from result Intent")
                    return@registerForActivityResult
                }

                // If the index received is -1, it means that it is a new configuration,
                // so it saves a new profile
                if (position == Constants.Define.NEW_PROFILE) {
                    mainViewModel.addProfile(currentProfile)

                    Log.d(logTAG, "Saving new profile")
                } else {
                    // Update uses the object ID.
                    // The returned ‘currentProfile’ object already has the database ID.
                    mainViewModel.updateProfile(currentProfile)

                    Log.d(logTAG, "Updating current profile")
                }

                mainViewModel.setToastText(getString(R.string.saved))
            }
        }

        // Initialize adapter for profile list
        val recyclerView = findViewById<RecyclerView>(R.id.profileList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Variable to store the current list of profiles.
        // Initialized with an empty list. It will be filled by LiveData.
        var currentProfileList: List<EqualizerProfile> = emptyList()

        // Start observing the LiveData containing the Toast text in the ViewModel
        mainViewModel.getToastText()
            .observe(this, Observer { text: String? -> this.toastShow(text!!) })

        adapter = ProfileRecyclerViewAdapter(
            profiles = currentProfileList.toMutableList(), // Pass an empty/copyable list
            // onQuickClick (Item Click)
            onItemClick = { profile: EqualizerProfile, position: Int ->
                // Use the Intent Factory from EqualizerActivity
                val intent = EqualizerActivity.newIntent(this, profile, position)
                eqActivity.launch(intent)

                val text = getString(R.string.current_profile, profile.name)
                mainViewModel.setToastText(text)
            },
            // onLongClick (Item Long Click)
            onItemLongClick = { profile: EqualizerProfile, position: Int ->
                val nameProfileSelected = profile.name
                // If the selected item is different from the default profile, delete the profile from the list
                if (nameProfileSelected != Constants.Define.PROFILE_DEFAULT_NAME) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.exclusion))
                    builder.setMessage(getString(R.string.confirmation_question_delete, profile.name))

                    builder.setPositiveButton(getString(R.string.positive_button_name)) { _: DialogInterface?, _: Int ->
                        mainViewModel.removeProfile(profile)
                        adapter.notifyItemRemoved(position)
                    }
                    builder.setNegativeButton(getString(R.string.negative_button_name), null)
                    builder.show()
                } else {
                    val text = getString(R.string.profile_cannot_deleted, nameProfileSelected)
                    mainViewModel.setToastText(text)
                }
                true
            }
        )

        // Define the Adapter in RecyclerView
        recyclerView.adapter = adapter

        // Observe the Room's LiveData and update the Adapter
        mainViewModel.allProfilesLiveData.observe(this) { profiles ->
            // Updates the list in the Adapter and notifies the change
            currentProfileList = profiles // Updates the reference list
            (recyclerView.adapter as ProfileRecyclerViewAdapter).updateProfiles(profiles)
            Log.d(logTAG, "Live Data profiles updated. Count: ${profiles.size}")
        }

        // Inicializa módulo
        playbackModule = PlaybackModule(this)

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        stopButton = findViewById(R.id.stop_button)
        seekBar = findViewById(R.id.seekBar)
        timeText = findViewById(R.id.timeText)

        // Configurações iniciais
        try {
            playbackModule.setRawDataSource(R.raw.toto_africa)
        } catch (e: Exception) {
            Log.e(logTAG, "Erro ao configurar faixa: ${e.message}")
        }
        // Controles
        playButton.setOnClickListener {
            if (playbackModule.canPlay()) {
                playbackModule.play()
            } else {
                Toast.makeText(this, "Nenhuma faixa carregada", Toast.LENGTH_SHORT).show()
            }
        }

            pauseButton.setOnClickListener {
                playbackModule.pause()
            }

            stopButton.setOnClickListener {
                playbackModule.stop()
            }

// Atualiza posição quando o usuário move a SeekBar
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val duration = playbackModule.getDuration()
                        val newPosition = (duration * progress) / 100
                        timeText.text = formatTime(newPosition) + " / " + formatTime(duration)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    isUserSeeking = true
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    isUserSeeking = false
                    val progress = seekBar?.progress ?: 0
                    val duration = playbackModule.getDuration()
                    val newPosition = (duration * progress) / 100
                    playbackModule.seekTo(newPosition)
                }
            })



        Log.d(logTAG, "All components of the main screen have been initialized")
    }

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if (!isUserSeeking && playbackModule.isPlaying()) {
                val position = playbackModule.getCurrentPosition()
                val duration = playbackModule.getDuration()

                if (duration > 0) {
                    val progress = (100 * position) / duration
                    seekBar.progress = progress
                    timeText.text = formatTime(position) + " / " + formatTime(duration)
                }
            }
            handler.postDelayed(this, 500) // Atualiza a cada 500 ms
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateSeekBarRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBarRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackModule.release()
    }
    fun toastShow(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun formatTime(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}

