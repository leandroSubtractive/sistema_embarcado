package com.leandromendes.vehicleequalizer.util

import androidx.recyclerview.widget.DiffUtil
import com.leandromendes.vehicleequalizer.data.model.EqualizerProfile

class ProfileDiffCallback(
    private val oldList: List<EqualizerProfile>,
    private val newList: List<EqualizerProfile>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Usa o ID como identificador único
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compara todos os campos (data class já faz equals corretamente)
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Retorna payload customizado caso só o isSelected mude
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem.isSelected != newItem.isSelected) {
            "selection_changed"
        } else {
            null
        }
    }
}