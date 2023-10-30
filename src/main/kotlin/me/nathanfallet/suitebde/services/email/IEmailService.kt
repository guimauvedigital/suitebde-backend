package me.nathanfallet.suitebde.services.email

interface IEmailService {

    fun sendEmail(destination: String, subject: String, content: String)

}