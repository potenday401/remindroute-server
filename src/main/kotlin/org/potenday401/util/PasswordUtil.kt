package org.potenday401.util
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object PasswordUtil {

    private const val SALT_ROUNDS = 12
    private const val ALGORITHM_AES = "AES"

    fun encodePassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(SALT_ROUNDS))
    }

    fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, hashedPassword)
    }

    fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun generateRandom5DigitCode(): String {
        val random = Random.Default
        return random.nextInt(10000, 99999).toString()
    }

    fun aesEncrypt(key: String, value: String): String {
        return encrypt(key, value, ALGORITHM_AES)
    }

    fun aesDecrypt(key: String, encryptedValue: String): String {
        return decrypt(key, encryptedValue, ALGORITHM_AES)
    }

    private fun encrypt(key: String, value: String, alg: String): String {
        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), alg)
        val cipher = Cipher.getInstance(alg)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)

        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun decrypt(key: String, encryptedValue: String, alg: String): String {
        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), alg)
        val cipher = Cipher.getInstance(alg)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)

        val decodedValue = Base64.getDecoder().decode(encryptedValue)
        val decrypted = cipher.doFinal(decodedValue)
        return String(decrypted, Charsets.UTF_8)
    }

}