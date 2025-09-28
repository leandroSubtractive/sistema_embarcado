package com.leandromendes.vehicleequalizer.util

/**
 * Classe singleton (object) que contém todas as constantes estáticas
 * e não mutáveis do aplicativo.
 */
object Constants {

    /**
     * Objeto aninhado que espelha a classe 'define' do Java para agrupar
     * nomes de intents, valores padrão e outros limites.
     * Usamos 'const val' para garantir que sejam constantes de tempo de compilação.
     */
    object define {
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
}
