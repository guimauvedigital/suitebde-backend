package me.nathanfallet.suitebde.services.emails

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.commons.mail.HtmlEmail

class EmailsService(
    private val host: String,
    private val username: String,
    private val password: String
) : IEmailsService {

    override fun sendEmail(destination: String, subject: String, content: String) {
        CoroutineScope(Job()).launch {
            val email = HtmlEmail()
            email.hostName = host
            email.isStartTLSEnabled = true
            email.setSmtpPort(587)
            email.setAuthentication(username, password)
            email.setFrom(username)
            email.addTo(destination)
            email.subject = subject
            email.setHtmlMsg(content)
            email.send()
        }
    }

}
