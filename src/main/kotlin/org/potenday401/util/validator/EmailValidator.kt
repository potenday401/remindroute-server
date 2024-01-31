package org.potenday401.util.validator

import java.util.regex.Pattern

object EmailValidator {

    private val EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})"
    )

    fun isValid(email: String): Boolean {
        if(email.isNullOrEmpty()) {
            return false
        }
        val matcher = EMAIL_PATTERN.matcher(email)
        return matcher.matches()
    }

}