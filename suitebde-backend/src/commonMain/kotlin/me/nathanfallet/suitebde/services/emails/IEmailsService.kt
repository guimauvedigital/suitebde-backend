package me.nathanfallet.suitebde.services.emails

interface IEmailsService {

    fun sendEmail(destination: String, subject: String, content: String)

}
