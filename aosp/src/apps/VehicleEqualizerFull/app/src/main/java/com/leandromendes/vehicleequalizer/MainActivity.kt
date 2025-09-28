package com.leandromendes.vehicleequalizer

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile
import com.leandromendes.vehicleequalizer.ui.EqualizerActivity
import com.leandromendes.vehicleequalizer.ui.MainViewModel
import com.leandromendes.vehicleequalizer.util.Constants
import com.leandromendes.vehicleequalizer.util.ProfileListUtils
import java.util.Locale


class MainActivity : AppCompatActivity() {
    var mainViewModel: MainViewModel? = null

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

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Define the title of the list of profile names
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.toolbar_name)

        // Initialize adapter for profile list
        val profileList = findViewById<ListView>(R.id.profileList)
        val adapter = ProfileListUtils(this, mainViewModel!!.getAllEqualizerProfiles())
        profileList.setAdapter(adapter)

        mainViewModel!!.getToastText()
            .observe(this, Observer { text: String? -> this.toastShow(text!!) })

        val eqActivity = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult? ->
            if (result!!.resultCode == RESULT_OK) {
                val intentRet = result.data

                val currentProfile: EqualizerProfile = intentRet?.getParcelableExtra(
                    Constants.define.INTENT_PARCELABLE_NAME,
                    EqualizerProfile::class.java
                ) as EqualizerProfile

                val position = intentRet.getIntExtra(
                    Constants.define.INTENT_INT_POSITION,
                    Constants.define.INTENT_INT_POSITION_DEFAULT
                )

                // If the index received is -1, it means that it is a new configuration,
                // so it saves a new profile
                if (position == Constants.define.NEW_PROFILE) {
                    mainViewModel!!.addProfile(currentProfile)
                } else {
                    mainViewModel!!.updateProfile(position, currentProfile)
                }

                // Update the ListView adapter
                adapter.notifyDataSetChanged()
                mainViewModel!!.setToastText(
                    String.format(
                        Locale.getDefault(),
                        "%s",
                        getString(R.string.saved)
                    )
                )
            }
        }

        // Defines an action for a quick click in the listview
        profileList.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                val intent = Intent(this, EqualizerActivity::class.java)
                intent.putExtra(
                    Constants.define.INTENT_PARCELABLE_NAME,
                    mainViewModel!!.getEqualizerProfiles(position)
                )
                intent.putExtra(Constants.define.INTENT_INT_POSITION, position)

                eqActivity.launch(intent)

                val text = String.format(
                    Locale.getDefault(), getString(R.string.current_profile),
                    mainViewModel!!.getEqualizerProfiles(position).name
                )
                mainViewModel!!.setToastText(text)
            }

        // Defines an action for a long click on a profile list
        profileList.setOnItemLongClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val nameProfileSelected = mainViewModel!!.getEqualizerProfiles(position).name
            // If the selected item is different from the default profile, delete the profile from the list
            if (nameProfileSelected != Constants.define.PROFILE_DEFAULT_NAME) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(String.format(Locale.getDefault(), getString(R.string.exclusion)))
                builder.setMessage(
                    String.format(
                        Locale.getDefault(), getString(R.string.confirmation_question_delete),
                        mainViewModel!!.getEqualizerProfiles(position).name
                    )
                )
                builder.setPositiveButton(
                    String.format(Locale.getDefault(), getString(R.string.positive_button_name))
                ) { dialog: DialogInterface?, which: Int ->
                    mainViewModel!!.removeProfile(position)
                    adapter.notifyDataSetChanged()
                }
                builder.setNegativeButton(
                    String.format(Locale.getDefault(), getString(R.string.negative_button_name)),
                    null
                )
                builder.show()
            } else {
                val text = String.format(
                    Locale.getDefault(),
                    getString(R.string.profile_cannot_deleted),
                    nameProfileSelected
                )
                toastShow(text)
            }
            true
        }
    }

    fun toastShow(text: String) {
        Toast.makeText(this, String.format(Locale.getDefault(), text), Toast.LENGTH_SHORT).show()
    }
}

