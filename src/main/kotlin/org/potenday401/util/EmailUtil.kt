package org.potenday401.util

import com.typesafe.config.ConfigFactory
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailUtil {

    private val smtpHost: String
    private val smtpPort: Int
    private val smtpUsername: String
    private val smtpPassword: String

    init {
        val config = ConfigFactory.load()
        smtpHost = config.getString("smtp.host")
        smtpPort = config.getInt("smtp.port")
        smtpUsername = config.getString("smtp.username")
        smtpPassword = config.getString("smtp.password")
    }

    fun sendEmail(subject: String, body: String, to: String) {
        val props = Properties()
        props["mail.smtp.host"] = smtpHost
        props["mail.smtp.port"] = smtpPort
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(smtpUsername, smtpPassword)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(smtpUsername))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            message.subject = subject
            message.setText(body)
            Transport.send(message)
            println("Email sent successfully.")
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw RuntimeException("Failed to send email.")
        }
    }
}