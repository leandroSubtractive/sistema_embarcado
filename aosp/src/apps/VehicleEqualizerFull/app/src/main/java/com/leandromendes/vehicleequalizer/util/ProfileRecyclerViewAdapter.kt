package com.leandromendes.vehicleequalizer.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val iconView: ImageView = itemView.findViewById(R.id.iconView)
        fun bind(profile: EqualizerProfile, position: Int) {
            profileName.text = profile.name

            if(profile.isSelected){
                iconView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.selected)
                )
            }else {
                iconView.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.notSelected)
                )
            }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        // Creates the ViewHolder (called when a new item is needed)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        // Connects the data to the ViewHolder (called to reuse the item)
        val profile = profiles[position]
        holder.bind(profile, position)
    }

    override fun getItemCount(): Int {
        // Returns the size of the list
        return profiles.size
    }

    fun updateProfiles(newProfiles: List<EqualizerProfile>) {
        //Update the LiveData profile list
        profiles.clear()
        profiles.addAll(newProfiles)
        notifyDataSetChanged()
    }

}