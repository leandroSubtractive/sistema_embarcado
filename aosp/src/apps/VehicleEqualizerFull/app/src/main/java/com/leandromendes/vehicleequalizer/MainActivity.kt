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

        // Creates or obtains the MainViewModel instance.
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

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

                        val newPosition = mainViewModel!!.getAllEqualizerProfiles().size - 1
                        adapter.notifyItemInserted(newPosition)

                        Log.d(logTAG, "Saving new profile")
                    } else {
                        mainViewModel!!.updateProfile(position, currentProfile)
                        adapter.notifyItemChanged(position)

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

        // Start observing the LiveData containing the Toast text in the ViewModel
        mainViewModel!!.getToastText()
            .observe(this, Observer { text: String? -> this.toastShow(text!!) })

        // MUDANÇA: Usar o novo ProfileRecyclerViewAdapter e passar as ações de click/long click como lambdas
        adapter = ProfileRecyclerViewAdapter(
            profiles = mainViewModel!!.getAllEqualizerProfiles(),
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
                        mainViewModel!!.removeProfile(position)
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
        Log.d(logTAG, "All components of the main screen have been initialized")
    }

    fun toastShow(text: String) {
        Toast.makeText(this, String.format(Locale.getDefault(), text), Toast.LENGTH_SHORT).show()
    }
}

