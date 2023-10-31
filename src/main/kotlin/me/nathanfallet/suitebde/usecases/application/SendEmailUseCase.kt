package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.services.email.IEmailService

class SendEmailUseCase(
    private val emailService: IEmailService
) : ISendEmailUseCase {

    override fun invoke(input1: String, input2: String, input3: String) {
        emailService.sendEmail(input1, input2, input3)
    }

}