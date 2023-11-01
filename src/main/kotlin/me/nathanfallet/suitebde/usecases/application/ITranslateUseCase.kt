package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.usecases.triple.ITripleUseCase
import java.util.*

interface ITranslateUseCase : ITripleUseCase<Locale, String, List<String>, String> {

    operator fun invoke(locale: Locale, key: String): String {
        return invoke(locale, key, listOf())
    }

}