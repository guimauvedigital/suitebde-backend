package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.services.email.IEmailService

class SendEmailUseCase(
    private val emailService: IEmailService
) : ISendEmailUseCase {

    override fun invoke(input: Triple<String, String, String>) {
        emailService.sendEmail(input.first, input.second, input.third)
    }

}