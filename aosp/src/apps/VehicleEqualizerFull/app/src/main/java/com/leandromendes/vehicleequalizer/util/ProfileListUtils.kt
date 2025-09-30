package com.leandromendes.vehicleequalizer.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class ProfileListUtils(
    private val context: Context,
    private val profiles: MutableList<EqualizerProfile>
) : ArrayAdapter<EqualizerProfile?>(
    context, android.R.layout.simple_list_item_2,
    profiles
) {
    // Overwrites the main method to create and return the View of an item in the list
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.item_profile, parent, false)

        val profileName = rowView.findViewById<TextView>(R.id.profileName)
        val profile = profiles[position]
        profileName.text = profile.name

        return rowView
    }
}