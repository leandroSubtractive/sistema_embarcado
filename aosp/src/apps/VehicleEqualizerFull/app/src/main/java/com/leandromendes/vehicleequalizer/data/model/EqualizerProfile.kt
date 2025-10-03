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
    var bassEqValue: Int = Constants.Define.BASS_VALUE_DEFAULT,
    var midEqValue: Int = Constants.Define.MIDDLE_VALUE_DEFAULT,
    var hiEqValue: Int = Constants.Define.TREBLE_VALUE_DEFAULT,
    var balanceEqValue: Int = Constants.Define.PAN_VALUE_DEFAULT,
    var masterVolValue: Int = Constants.Define.VOLUME_VALUE_DEFAULT,
    var isSelected: Boolean = true
) : Parcelable

