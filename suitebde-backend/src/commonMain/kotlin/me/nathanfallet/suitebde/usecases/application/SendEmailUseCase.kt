package me.nathanfallet.suitebde.usecases.application

import me.nathanfallet.suitebde.services.emails.IEmailsService

class SendEmailUseCase(
    private val emailsService: IEmailsService
) : ISendEmailUseCase {

    override fun invoke(input1: String, input2: String, input3: String) {
        emailsService.sendEmail(input1, input2, input3)
    }

}
