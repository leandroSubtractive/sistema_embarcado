package com.leandromendes.vehicleequalizer.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leandromendes.vehicleequalizer.util.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = "equalizer_profiles")
@Parcelize
data class EqualizerProfile(
    // Sets the ID as the primary key and auto-generated
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // New ID field for Room
    var name: String = Constants.Define.PROFILE_DEFAULT_NAME,
    var band0: Int = Constants.Define.GAIN_VALUE_DEFAULT,
    var band1: Int = Constants.Define.GAIN_VALUE_DEFAULT,
    var band2: Int = Constants.Define.GAIN_VALUE_DEFAULT,
    var band3: Int = Constants.Define.GAIN_VALUE_DEFAULT,
    var band4: Int = Constants.Define.GAIN_VALUE_DEFAULT,
    var masterVolValue: Int = Constants.Define.VOLUME_VALUE_DEFAULT,
    var isSelected: Boolean = false
) : Parcelable

