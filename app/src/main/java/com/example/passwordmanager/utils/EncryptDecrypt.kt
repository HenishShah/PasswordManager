package com.example.passwordmanager.utils

import android.util.Base64
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptDecrypt {
    private val cipherInstance = "AES/CBC/PKCS5Padding"
    private lateinit var KEY: ByteArray
    private lateinit var IV: ByteArray
    private lateinit var ivParameterSpec: IvParameterSpec
    private var encodedUuid: String? = null
    private var encryptedString: String? = null

    private var mdString: String? = null

    fun getEncryptedBody(accountType: String, plainText: String): String? {
        createKeys(accountType)
        encryptedString = encrypt(plainText)
        return encryptedString
    }

    fun getDecryptedBody(accountType: String, encryptedText: String): String {
        createKeys(accountType)
        return decrypt(encryptedText)
    }

    private fun createKeys(accountType: String) {
        encodedUuid = Base64.encodeToString(
            accountType.toByteArray(), Base64.DEFAULT
        )
        val uuidSimple = accountType.replace("-", "")
        mdString = md5(uuidSimple)
        KEY = mdString!!.substring(0, 16).toByteArray()
        IV = mdString!!.substring(16).toByteArray()
        ivParameterSpec = IvParameterSpec(IV)
    }

    @Throws(Exception::class)
    private fun encrypt(textToEncrypt: String): String? {
        val skeySpec = SecretKeySpec(KEY, "AES")
        val cipher = Cipher.getInstance(cipherInstance)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(textToEncrypt.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    @Throws(java.lang.Exception::class)
    private fun decrypt(textToDecrypt: String?): String {
        val encryptedBytes = Base64.decode(textToDecrypt, Base64.DEFAULT)
        val skeySpec = SecretKeySpec(KEY, "AES")
        val cipher = Cipher.getInstance(cipherInstance)
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)
        val decrypted = cipher.doFinal(encryptedBytes)
        return String(decrypted)
    }

    @Throws(Exception::class)
    private fun encryptMessage(textToEncrypt: String): String? {
        val skeySpec = SecretKeySpec(KEY, "AES")
        val cipher = Cipher.getInstance(cipherInstance)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(textToEncrypt.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    @Throws(java.lang.Exception::class)
    private fun decryptMessage(textToDecrypt: String?): String {
        val encryptedBytes = Base64.decode(textToDecrypt, Base64.NO_WRAP)
        val skeySpec = SecretKeySpec(KEY, "AES")
        val cipher = Cipher.getInstance(cipherInstance)
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)
        val decrypted = cipher.doFinal(encryptedBytes)
        return String(decrypted)
    }

    private fun md5(s: String): String {
        val mD5 = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(mD5)
            digest.update(s.toByteArray(StandardCharsets.US_ASCII), 0, s.length)
            val magnitude = digest.digest()
            val bi = BigInteger(1, magnitude)
            return String.format("%0" + (magnitude.size shl 1) + "x", bi)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}