package com.leandromendes.vehicleequalizer

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.leandromendes.vehicleequalizer.ui.EqualizerActivity
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModel
import com.leandromendes.vehicleequalizer.ui.viewmodel.MainViewModelFactory
import com.leandromendes.vehicleequalizer.util.Constants
import com.leandromendes.vehicleequalizer.util.ProfileRecyclerViewAdapter
import java.util.Locale


class MainActivity : AppCompatActivity() {
    var mainViewModel: MainViewModel? = null
    private lateinit var adapter: ProfileRecyclerViewAdapter
    private val logTAG = "VehicleEqualizerApp"

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
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java] // Usa o Factory

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
            if (result!!.resultCode == RESULT_OK) {
                // Get the return Intent
                val intentRet = result.data

                // Extract the EqualizerProfile object
                val currentProfile: EqualizerProfile = intentRet?.getParcelableExtra(
                    Constants.define.INTENT_PARCELABLE_NAME,
                    EqualizerProfile::class.java
                ) as EqualizerProfile

                // Extracts the position of the profile in the list.
                val position = intentRet.getIntExtra(
                    Constants.define.INTENT_INT_POSITION,
                    Constants.define.INTENT_INT_POSITION_DEFAULT
                )

                // If the index received is -1, it means that it is a new configuration,
                // so it saves a new profile
                if (position == Constants.define.NEW_PROFILE) {
                    mainViewModel!!.addProfile(currentProfile)

                    Log.d(logTAG, "Saving new profile")
                } else {
                    // Update uses the object ID.
                    // The returned ‘currentProfile’ object already has the database ID.
                    mainViewModel!!.updateProfile(currentProfile)

                    Log.d(logTAG, "Updating current profile")
                }

                mainViewModel!!.setToastText(
                    String.format(
                        Locale.getDefault(),
                        "%s",
                        getString(R.string.saved)
                    )
                )
            }
        }

        // Initialize adapter for profile list
        val recyclerView = findViewById<RecyclerView>(R.id.profileList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Variable to store the current list of profiles.
        // Initialized with an empty list. It will be filled by LiveData.
        var currentProfileList: List<EqualizerProfile> = emptyList()

        // Start observing the LiveData containing the Toast text in the ViewModel
        mainViewModel!!.getToastText()
            .observe(this, Observer { text: String? -> this.toastShow(text!!) })

        adapter = ProfileRecyclerViewAdapter(
            profiles = currentProfileList.toMutableList(), // Pass an empty/copyable list
            // onQuickClick (Item Click)
            onItemClick = { profile: EqualizerProfile, position: Int ->
                val intent = Intent(this, EqualizerActivity::class.java)
                intent.putExtra(
                    Constants.define.INTENT_PARCELABLE_NAME,
                    profile
                )
                intent.putExtra(Constants.define.INTENT_INT_POSITION, position)

                eqActivity.launch(intent)

                val text = String.format(
                    Locale.getDefault(), getString(R.string.current_profile),
                    profile.name
                )
                mainViewModel!!.setToastText(text)
            },
            // onLongClick (Item Long Click)
            onItemLongClick = { profile: EqualizerProfile, position: Int ->
                val nameProfileSelected = profile.name
                // If the selected item is different from the default profile, delete the profile from the list
                if (nameProfileSelected != Constants.define.PROFILE_DEFAULT_NAME) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(
                        String.format(
                            Locale.getDefault(),
                            getString(R.string.exclusion)
                        )
                    )
                    builder.setMessage(
                        String.format(
                            Locale.getDefault(), getString(R.string.confirmation_question_delete),
                            profile.name
                        )
                    )
                    builder.setPositiveButton(
                        String.format(Locale.getDefault(), getString(R.string.positive_button_name))
                    ) { dialog: DialogInterface?, which: Int ->
                        mainViewModel!!.removeProfile(profile)
                        adapter.notifyItemRemoved(position)
                    }
                    builder.setNegativeButton(
                        String.format(
                            Locale.getDefault(),
                            getString(R.string.negative_button_name)
                        ),
                        null
                    )
                    builder.show()
                } else {
                    val text = String.format(
                        Locale.getDefault(),
                        getString(R.string.profile_cannot_deleted),
                        nameProfileSelected
                    )
                    mainViewModel!!.setToastText(text)
                }
                true
            }
        )

        // Define the Adapter in RecyclerView
        recyclerView.adapter = adapter

        // Observe the Room's LiveData and update the Adapter
        mainViewModel!!.allProfilesLiveData.observe(this) { profiles ->
            // Updates the list in the Adapter and notifies the change
            currentProfileList = profiles // Updates the reference list
            (recyclerView.adapter as ProfileRecyclerViewAdapter).updateProfiles(profiles)
            Log.d(logTAG, "Live Data profiles updated. Count: ${profiles.size}")
        }

        Log.d(logTAG, "All components of the main screen have been initialized")
    }

    fun toastShow(text: String) {
        Toast.makeText(this, String.format(Locale.getDefault(), text), Toast.LENGTH_SHORT).show()
    }
}

