package me.nathanfallet.suitebde.usecases.application

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.R
import java.util.*

class TranslateUseCase(
    private val i18n: MessageResolver
) : ITranslateUseCase {

    override fun invoke(input1: Locale, input2: String, input3: List<String>): String {
        return i18n.t(input1, R(input2), *input3.toTypedArray())
    }

}