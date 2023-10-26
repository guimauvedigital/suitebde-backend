package me.nathanfallet.suitebde.utils

object IdHelper {

    fun generateId(): String {
        val charPool: List<Char> = ('a'..'z') + ('0'..'9')
        return List(32) { charPool.random() }.joinToString("")
    }

}