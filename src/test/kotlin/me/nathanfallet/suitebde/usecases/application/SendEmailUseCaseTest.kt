package me.nathanfallet.suitebde.usecases.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.nathanfallet.suitebde.services.emails.IEmailsService
import kotlin.test.Test

class SendEmailUseCaseTest {

    @Test
    fun invoke() {
        val service = mockk<IEmailsService>()
        val useCase = SendEmailUseCase(service)
        every { service.sendEmail(any(), any(), any()) } returns Unit
        useCase("email", "subject", "content")
        verify { service.sendEmail("email", "subject", "content") }
    }

}
