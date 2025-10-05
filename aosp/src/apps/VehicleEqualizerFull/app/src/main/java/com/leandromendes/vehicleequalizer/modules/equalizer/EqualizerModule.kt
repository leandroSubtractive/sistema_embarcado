package com.leandromendes.vehicleequalizer.modules.equalizer

import android.content.Context
import android.media.AudioManager
import android.media.audiofx.Equalizer
import android.util.Log
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class EqualizerModule(
    private val context: Context,
    audioSessionId: Int
) : EqualizerInterface {

    private var logTAG = "EqualizerModule"
    private var equalizer: Equalizer? = try {
        if (audioSessionId > 0) {
            Equalizer(0, audioSessionId).apply { enabled = true }
        } else {
            Log.e(logTAG, "audioSessionId inválido ($audioSessionId)")
            null
        }
    } catch (e: Exception) {
        Log.e(logTAG, "Falha ao inicializar Equalizer: ${e.message}")
        null
    }

    private var enabled = true

    override fun applyProfile(profile: EqualizerProfile) {
        if (!enabled) {
            Log.d(logTAG, "Equalizer desativado → ignorando applyProfile")
            return
        }

        Log.d(logTAG, "Aplicando perfil: $profile")
        setBass(profile.bassEqValue)
        setMid(profile.midEqValue)
        setTreble(profile.hiEqValue)
        setVolume(profile.masterVolValue)
    }

    override fun setBass(level: Int) {
        if (!enabled) {
            Log.d(logTAG, "Equalizer desativado → ignorando setBass($level)")
            return
        }
        try {
            equalizer?.setBandLevel(1, (level * 100).toShort())
            Log.d(logTAG, "Bass ajustado para $level (band=0)")
        } catch (e: Exception) {
            Log.e(logTAG, "Erro ao ajustar Bass: ${e.message}")
        }
    }

    override fun setMid(level: Int) {
        if (!enabled) {
            Log.d(logTAG, "Equalizer desativado → ignorando setMid($level)")
            return
        }
        try {
            val midBand = equalizer?.numberOfBands?.div(2)
            equalizer?.setBandLevel(midBand!!.toShort(), (level * 100).toShort())
            Log.d(logTAG, "Mid ajustado para $level (band=$midBand)")
        } catch (e: Exception) {
            Log.e(logTAG, "Erro ao ajustar Mid: ${e.message}")
        }
    }

    override fun setTreble(level: Int) {
        if (!enabled) {
            Log.d(logTAG, "Equalizer desativado → ignorando setTreble($level)")
            return
        }
        try {
            val lastBand = equalizer?.numberOfBands?.minus(1)
            equalizer?.setBandLevel(lastBand!!.toShort(), (level * 100).toShort())
            Log.d(logTAG, "Treble ajustado para $level (band=$lastBand)")
        } catch (e: Exception) {
            Log.e(logTAG, "Erro ao ajustar Treble: ${e.message}")
        }
    }

    override fun setBalance(level: Int) {
        // Android Equalizer não tem controle direto de balance (panning).
        // Isso pode ser implementado via AudioTrack ou mixer customizado.
        Log.d(logTAG, "setBalance($level) chamado (não implementado nativamente)")
    }

    override fun setVolume(level: Int) {
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val newVolume = ((level.toFloat() / 100f) * maxVolume).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
            Log.d(logTAG, "Volume ajustado para $level (stream=$newVolume/$maxVolume)")
        } catch (e: Exception) {
            Log.e(logTAG, "Erro ao ajustar Volume: ${e.message}")
        }
    }

    override fun setBandLevelSafe(band: Int, level: Int) {
        val eq = equalizer ?: return
        if (!enabled) return

        try {
            val range = eq.bandLevelRange // [min, max] em décimos de dB
            val min = range[0]
            val max = range[1]

            // garante que o valor está dentro do intervalo suportado
            val safeLevel = level.coerceIn(min.toInt(), max.toInt()).toShort()

            eq.setBandLevel(band.toShort(), safeLevel)
            Log.d("EqualizerModule", "Banda $band ajustada para $safeLevel (range: $min .. $max)")
        } catch (e: Exception) {
            Log.e("EqualizerModule", "Erro ao ajustar banda $band: ${e.message}")
        }
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        equalizer?.enabled = enabled
        Log.d(logTAG, "Equalizer ${if (enabled) "ativado" else "desativado"}")
    }

    override fun reset() {
        if (!enabled) {
            Log.d(logTAG, "Equalizer desativado → ignorando reset()")
            return
        }
        for (i in 0 until equalizer!!.numberOfBands) {
            equalizer!!.setBandLevel(i.toShort(), 0)
        }
        Log.d(logTAG, "Equalizer resetado (todas bandas = 0)")
    }

    override fun printBandsInfo() {
        val bands = equalizer?.numberOfBands ?: 0
        val range = equalizer?.bandLevelRange ?: shortArrayOf(0, 0)

        Log.d(logTAG, "Equalizer possui $bands bandas (Min=${range[0]} dB, Max=${range[1]} dB)")

        for (i in 0 until bands) {
            val centerFreq = equalizer?.getCenterFreq(i.toShort()) ?: 0
            Log.d(logTAG, "Banda $i → Freq: ${centerFreq / 1000} Hz")
        }
    }

    override fun release() {
        equalizer?.release()
        equalizer = null
        Log.d(logTAG, "Equalizer liberado")
    }
}
