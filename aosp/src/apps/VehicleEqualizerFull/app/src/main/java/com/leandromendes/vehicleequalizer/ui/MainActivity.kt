package com.leandromendes.vehicleequalizer.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leandromendes.vehicleequalizer.ProfileApplication
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.service.AudioService
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModel
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModelFactory
import com.leandromendes.vehicleequalizer.util.Constants
import com.leandromendes.vehicleequalizer.util.ProfileRecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    private val logTAG = "VehicleEqualizerApp"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trackTitle: TextView
    private lateinit var timeText: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var playPauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var adapter: ProfileRecyclerViewAdapter
    private var isSeeking = false
    private var duration = 0
    private var isPlaying = false // Flag to signal the play status
    private val stateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Constants.MusicConstants.BROADCAST_MUSIC_STATE) {
                val currentPosition = intent.getIntExtra(Constants.MusicConstants.EXTRA_CURRENT_POSITION, 0)
                duration = intent.getIntExtra(Constants.MusicConstants.EXTRA_DURATION, 0)
                val title = intent.getStringExtra(Constants.MusicConstants.EXTRA_TRACK_TITLE) ?: "Unknown track"
                val state = intent.getStringExtra(Constants.MusicConstants.EXTRA_STATE) ?: Constants.PlaybackStates.STOPPED

                if (!isSeeking && state != Constants.PlaybackStates.STOPPED) {
                    seekBar.max = duration
                    seekBar.progress = currentPosition
                } else if (!isSeeking) {
                    seekBar.max = duration
                    seekBar.progress = 0
                }
                timeText.text = getString(
                    R.string.time_format,
                    formatTime(currentPosition),
                    formatTime(duration)
                )
                trackTitle.text = title

                // Updates the icon if the status has changed outside the button
                if (state == Constants.PlaybackStates.PLAYING && !isPlaying) {
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                    isPlaying = true
                } else if ((state == Constants.PlaybackStates.PAUSED || state == Constants.PlaybackStates.STOPPED) && isPlaying) {
                    playPauseButton.setImageResource(R.drawable.ic_play_arrow)
                    isPlaying = false
                }
            }
        }
    }

    // Notification permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "Notification permission denied. The player may not function correctly.", Toast.LENGTH_LONG).show()
            }
        }

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

        // Request notification permission if necessary, if it has not been given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Obtain the Application, Database, and Repository to inject into the ViewModel
        val application = application as ProfileApplication // Casting for the new Application class
        val factory = MainViewModelFactory(application.repository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java] // Use this Factory


        // Start observing the LiveData containing the Toast text in the ViewModel
        mainViewModel.getToastText().observe(this, Observer { text: String? -> this.toastShow(text!!) })

        /**
         * Registers a callback to start an Activity
         * and handle its result (the returned data) asynchronously.
         *
         * The variable ‘eqActivity’ is the launcher that will be used to start the Activity.
         */
        val eqActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
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

        // Variable to store the current list of profiles.
        // Initialized with an empty list. It will be filled by LiveData.
        var currentProfileList: List<EqualizerProfile> = emptyList()

        // Adapter configuration
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
                    builder.setMessage(
                        getString(
                            R.string.confirmation_question_delete,
                            profile.name
                        )
                    )

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

        // Initialize adapter for profile list
        val recyclerView = findViewById<RecyclerView>(R.id.profileList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Define the Adapter in RecyclerView
        recyclerView.adapter = adapter

        // Observe the Room's LiveData and update the Adapter
        mainViewModel.allProfilesLiveData.observe(this) { profiles ->
            // Updates the list in the Adapter and notifies the change
            currentProfileList = profiles // Updates the reference list
            (recyclerView.adapter as ProfileRecyclerViewAdapter).updateProfiles(profiles)
            Log.d(logTAG, "Live Data profiles updated. Count: ${profiles.size}")
        }

        // Get all the music player interface components
        playPauseButton = findViewById(R.id.playPauseButton)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        seekBar = findViewById(R.id.seekBar)
        trackTitle = findViewById(R.id.trackTitle)
        timeText = findViewById(R.id.timeText)

        // Play/Pause button with animation
        playPauseButton.setOnClickListener {
            val nextIcon = if (isPlaying) R.drawable.ic_play_arrow else R.drawable.ic_pause
            val action = if (isPlaying) Constants.MusicConstants.ACTION_PAUSE else Constants.MusicConstants.ACTION_PLAY
            isPlaying = !isPlaying

            // animation: fade out + scale -> icon change -> fade in
            playPauseButton.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(150)
                .withEndAction {
                    playPauseButton.setImageResource(nextIcon)
                    playPauseButton.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()
            sendActionToService(action)
        }

        nextButton.setOnClickListener {
            sendActionToService(Constants.MusicConstants.ACTION_NEXT)
        }

        prevButton.setOnClickListener {
            sendActionToService(Constants.MusicConstants.ACTION_PREVIOUS)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                val position = seekBar?.progress ?: 0
                val intent = Intent(this@MainActivity, AudioService::class.java)
                intent.action = Constants.MusicConstants.ACTION_SEEK_TO
                intent.putExtra(Constants.MusicConstants.EXTRA_SEEK_POSITION, position)
                startService(intent)
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    timeText.text = getString(
                        R.string.time_format,
                        formatTime(progress),
                        formatTime(duration)
                    )

                }
            }
        })

        Log.d(logTAG, "All components of the main screen have been initialized")
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Constants.MusicConstants.BROADCAST_MUSIC_STATE)
        registerReceiver(stateReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(stateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun toastShow(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    private fun sendActionToService(action: String) {
        val intent = Intent(this, AudioService::class.java).apply {
            this.action = action
        }
        ContextCompat.startForegroundService(this, intent) // guarantees foreground service
    }
    private fun formatTime(millis: Int): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return getString(R.string.time_mm_ss, minutes, seconds)
    }

}