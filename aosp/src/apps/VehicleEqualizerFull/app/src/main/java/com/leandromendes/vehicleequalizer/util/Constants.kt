package com.leandromendes.vehicleequalizer.util

/**
 * Classe singleton (object) que contém todas as constantes estáticas
 * e não mutáveis do aplicativo.
 */
object Constants {

    object Define {
        const val INTENT_PARCELABLE_NAME = "profile"
        const val INTENT_INT_POSITION = "position"
        const val INTENT_INT_POSITION_DEFAULT = -1
        const val GAIN_VALUE_DEFAULT = 0
        const val MIDDLE_VALUE_DEFAULT = 0
        const val TREBLE_VALUE_DEFAULT = 0
        const val PAN_VALUE_DEFAULT = 5
        const val VOLUME_VALUE_DEFAULT = 75
        const val PROFILE_DEFAULT_NAME = "Default"
        const val NEW_PROFILE = -1
        const val DATETIME_FORMAT = "dd-MM-yyyy HH-mm-ss"

    }

    object MusicConstants {
        const val ACTION_PLAY = "com.leandromendes.vehicleequalizer.ACTION_PLAY"
        const val ACTION_PAUSE = "com.leandromendes.vehicleequalizer.ACTION_PAUSE"
        const val ACTION_STOP = "com.leandromendes.vehicleequalizer.ACTION_STOP"
        const val ACTION_NEXT = "com.leandromendes.vehicleequalizer.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.leandromendes.vehicleequalizer.ACTION_PREVIOUS"
        const val ACTION_SEEK_TO = "com.leandromendes.vehicleequalizer.ACTION_SEEK_TO"

        const val BROADCAST_MUSIC_STATE = "com.leandromendes.vehicleequalizer.BROADCAST_MUSIC_STATE"

        const val ACTION_APPLY_PROFILE = "com.leandromendes.vehicleequalizer.ACTION_APPLY_PROFILE"
        const val ACTION_ENABLE_EQUALIZER = "com.leandromendes.vehicleequalizer.ACTION_ENABLE_EQUALIZER"
        const val ACTION_EQUALIZER_STATUS = "com.leandromendes.vehicleequalizer.ACTION_EQUALIZER_STATUS"
        const val ACTION_UPDATE_UI = "com.leandromendes.vehicleequalizer.ACTION_UPDATE_UI"
        const val EXTRA_EQUALIZER_ENABLED = "com.leandromendes.vehicleequalizer.EXTRA_EQUALIZER_ENABLED"

        const val ACTION_SET_VOLUME = "com.leandromendes.vehicleequalizer.ACTION_SET_VOLUME"
        const val ACTION_SET_BAND_LEVEL = "com.leandromendes.vehicleequalizer.ACTION_SET_BAND_LEVEL"

        const val EXTRA_LEVEL = "com.leandromendes.vehicleequalizer.EXTRA_LEVEL"
        const val EXTRA_BAND = "com.leandromendes.vehicleequalizer.EXTRA_BAND"
        const val EXTRA_PROFILE = "com.leandromendes.vehicleequalizer.EXTRA_PROFILE"
        const val EXTRA_ENABLED = "com.leandromendes.vehicleequalizer.EXTRA_ENABLED"
        const val EXTRA_SEEK_POSITION = "com.leandromendes.vehicleequalizer.EXTRA_SEEK_POSITION"
        const val EXTRA_STATE = "com.leandromendes.vehicleequalizer.EXTRA_STATE"
        const val EXTRA_CURRENT_POSITION = "com.leandromendes.vehicleequalizer.EXTRA_CURRENT_POSITION"
        const val EXTRA_DURATION = "com.leandromendes.vehicleequalizer.EXTRA_DURATION"
        const val EXTRA_TRACK_TITLE = "com.leandromendes.vehicleequalizer.EXTRA_TRACK_TITLE"
    }

    object PlaybackStates {
        const val PLAYING = "Playing"
        const val STOPPED = "Stopped"
        const val PAUSED = "Paused"
    }
}
