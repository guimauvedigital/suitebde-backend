package com.suitebde.usecases.application

import com.suitebde.services.emails.IEmailsService
import dev.kaccelero.commons.emails.IEmail
import dev.kaccelero.commons.emails.ISendEmailUseCase

class SendEmailUseCase(
    private val emailsService: IEmailsService,
) : ISendEmailUseCase {

    override fun invoke(input1: IEmail, input2: List<String>) = emailsService.sendEmail(input1, input2)

}
