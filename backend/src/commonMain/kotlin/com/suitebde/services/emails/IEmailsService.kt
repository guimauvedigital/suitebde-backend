package com.suitebde.services.emails

import dev.kaccelero.commons.emails.IEmail

interface IEmailsService {

    fun sendEmail(email: IEmail, destination: List<String>)

}
