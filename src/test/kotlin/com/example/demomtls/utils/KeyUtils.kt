package com.example.demomtls.utils

import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

object KeyUtils {
    fun rsaKeyPair(keysize: Int): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(keysize)
        return keyPairGenerator.generateKeyPair()
    }

    fun ecKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ECGenParameterSpec("secp256r1"))
        return keyPairGenerator.generateKeyPair()
    }

    fun Key.toBase64(): String {
        return Base64.getEncoder().encodeToString(this.encoded)
    }

    fun KeyFactory.parsePublic(key: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val keySpec = X509EncodedKeySpec(keyBytes)
        return this.generatePublic(keySpec)
    }

    fun KeyFactory.parsePrivate(key: String): PrivateKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        return this.generatePrivate(keySpec)
    }

    fun printPrivate(keyBase64: String) {
        println("Private key %d:%n\t%s".format(keyBase64.length, keyBase64))
    }

    fun printPublic(keyBase64: String) {
        println("Public key %d:%n\t%s".format(keyBase64.length, keyBase64))
    }
}
