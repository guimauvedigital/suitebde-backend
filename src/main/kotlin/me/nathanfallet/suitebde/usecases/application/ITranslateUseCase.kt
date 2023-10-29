package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.usecases.IUseCase
import java.util.*

interface ITranslateUseCase : IUseCase<Triple<Locale, String, List<String>>, String> {

    operator fun invoke(locale: Locale, key: String, args: List<String> = listOf()): String {
        return invoke(Triple(locale, key, args))
    }

}