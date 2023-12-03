package me.nathanfallet.suitebde.usecases.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nathanfallet.suitebde.models.application.Email
import me.nathanfallet.suitebde.services.emails.IEmailsService
import kotlin.test.Test

class SendEmailUseCaseTest {

    @Test
    fun invoke() {
        val service = mockk<IEmailsService>()
        val useCase = SendEmailUseCase(service)
        val email = Email("subject", "content")
        every { service.sendEmail(email, listOf("email")) } returns Unit
        useCase(email, listOf("email"))
        verify { service.sendEmail(email, listOf("email")) }
    }

}
