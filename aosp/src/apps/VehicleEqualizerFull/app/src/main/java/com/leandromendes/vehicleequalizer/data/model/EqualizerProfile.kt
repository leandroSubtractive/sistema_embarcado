package com.leandromendes.vehicleequalizer.data.model

import android.os.Parcelable
import com.leandromendes.vehicleequalizer.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class EqualizerProfile(
    var name: String = Constants.define.PROFILE_DEFAULT_NAME,
    var bassEqValue: Int = Constants.define.BASS_VALUE_DEFAULT,
    var midEqValue: Int = Constants.define.MIDDLE_VALUE_DEFAULT,
    var hiEqValue: Int = Constants.define.TREBLE_VALUE_DEFAULT,
    var balanceEqValue: Int = Constants.define.PAN_VALUE_DEFAULT,
    var masterVolValue: Int = Constants.define.VOLUME_VALUE_DEFAULT
) : Parcelable

