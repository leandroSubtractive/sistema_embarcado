package com.leandromendes.vehicleequalizerapp.util

class CanMessage (val id: Int, val data: ByteArray){

    override fun equals(other: Any?): Boolean {
        /**
         * Verifica se o objeto atual e o objeto passado como parâmetro
         * são a mesma instância na memória. Se forem, a comparação de conteúdo é desnecessária
         * e a função retorna true imediatamente.
         */
        if (this === other) return true

        /**
         * verifica se os dois objetos são da mesma classe. Se eles não forem da mesma classe,
         * eles não podem ser iguais, então a função retorna false.
         */
        if (javaClass != other?.javaClass) return false

        /**
         * Realiza um cast seguro para converter o other do tipo genérico Any
         * para o tipo específico CanMessage
         */
        other as CanMessage

        /**
         * Compara a propriedade id do objeto atual com a propriedade id do objeto other.
         */
        if (id != other.id) return false

        /**
         * Verifica se os conteúdos de dois arrays são iguais
         */
        if (!data.contentEquals(other.data)) return false

        /**
         * Se todas as verificações anteriores passaram, significa que os objetos são
         * logicamente iguais em seu conteúdo. A função retorna true
         */
        return true
    }

    override fun hashCode(): Int {
        var result = id

        /**
         * Calcula código hash baseado no ID e conteudo da mensagem.
         */
        result = 31 * result + data.contentHashCode()
        
        return result
    }
}