package me.nathanfallet.suitebde.usecases.application

import com.github.aymanizz.ktori18n.MessageResolver
import com.github.aymanizz.ktori18n.R
import io.mockk.every
import io.mockk.mockk
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TranslateUseCaseTest {

    @Test
    fun invoke() {
        val i18n = mockk<MessageResolver>()
        val useCase = TranslateUseCase(i18n)
        every { i18n.t(Locale.ENGLISH, any(), any()) } answers {
            "t:${secondArg<R>().iterator().next()}:${thirdArg<Array<String>>().toList()}"
        }
        assertEquals("t:error_mock:[args]", useCase(Triple(Locale.ENGLISH, "error_mock", listOf("args"))))
    }

}