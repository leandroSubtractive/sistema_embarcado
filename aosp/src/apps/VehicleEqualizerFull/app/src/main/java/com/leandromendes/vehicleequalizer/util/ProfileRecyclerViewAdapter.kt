package com.leandromendes.vehicleequalizer.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leandromendes.vehicleequalizer.R
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class ProfileRecyclerViewAdapter(
    private val profiles: MutableList<EqualizerProfile>,
    // Lambdas that will be called in MainActivity when tapping on an item in the list
    private val onItemClick: (profile: EqualizerProfile, position: Int) -> Unit,
    private val onItemLongClick: (profile: EqualizerProfile, position: Int) -> Boolean
) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileName: TextView = itemView.findViewById(R.id.profileName)
        fun bind(profile: EqualizerProfile, position: Int) {
            profileName.text = profile.name

            // Configure or click listener
            itemView.setOnClickListener {
                onItemClick(profile, position)
            }

            //  Configures the long click listener
            itemView.setOnLongClickListener {
                onItemLongClick(profile, position)
            }
        }
    }

    // Creates the ViewHolder (called when a new item is needed)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(view)
    }

    // Connects the data to the ViewHolder (called to reuse the item)
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile, position)
    }

    // Returns the size of the list
    override fun getItemCount(): Int {
        return profiles.size
    }
}