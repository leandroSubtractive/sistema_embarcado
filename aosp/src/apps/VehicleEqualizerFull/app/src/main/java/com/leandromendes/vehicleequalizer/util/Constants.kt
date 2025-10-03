package com.leandromendes.vehicleequalizer.util

/**
 * Classe singleton (object) que contém todas as constantes estáticas
 * e não mutáveis do aplicativo.
 */
object Constants {

    /**
     * Objeto aninhado que espelha a classe 'Define' do Java para agrupar
     * nomes de intents, valores padrão e outros limites.
     * Usamos 'const val' para garantir que sejam constantes de tempo de compilação.
     */
    object Define {
        const val INTENT_PARCELABLE_NAME = "profile"
        const val INTENT_INT_POSITION = "position"
        const val INTENT_INT_POSITION_DEFAULT = -1
        const val INTENT_VIEW_MODEL = "viewModel"
        const val BASS_VALUE_DEFAULT = 0
        const val MIDDLE_VALUE_DEFAULT = 0
        const val TREBLE_VALUE_DEFAULT = 0
        const val PAN_VALUE_DEFAULT = 5
        const val VOLUME_VALUE_DEFAULT = 6
        const val PROFILE_DEFAULT_NAME = "Default"
        const val NEW_PROFILE = -1
        const val DATETIME_FORMAT = "dd-MM-yyyy HH-mm-ss"

    }

    object MusicConstants {
        const val ACTION_PLAY = "com.leandromendes.vehicleequalizer.ACTION_PLAY"
        const val ACTION_PAUSE = "com.leandromendes.vehicleequalizer.ACTION_PAUSE"
        const val ACTION_NEXT = "com.leandromendes.vehicleequalizer.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.leandromendes.vehicleequalizer.ACTION_PREVIOUS"
        const val ACTION_SEEK_TO = "com.leandromendes.vehicleequalizer.ACTION_SEEK_TO"

        const val EXTRA_SEEK_POSITION = "extra_seek_position"
        const val EXTRA_TRACK_INDEX = "extra_track_index"

        const val BROADCAST_MUSIC_STATE = "com.leandromendes.vehicleequalizer.BROADCAST_MUSIC_STATE"
        const val EXTRA_STATE = "extra_state"              // "playing" / "paused"
        const val EXTRA_CURRENT_POSITION = "extra_position"
        const val EXTRA_DURATION = "extra_duration"
        const val EXTRA_TRACK_TITLE = "extra_track_title"
    }
}
