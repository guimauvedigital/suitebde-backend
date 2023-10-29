package me.nathanfallet.suitebde.usecases.application

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.R
import java.util.*

class TranslateUseCase(
    private val i18n: MessageResolver
) : ITranslateUseCase {

    override fun invoke(input: Triple<Locale, String, List<String>>): String {
        return i18n.t(input.first, R(input.second), *input.third.toTypedArray())
    }

}