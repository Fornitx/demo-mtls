package com.example.demomtls.utils

import com.example.demomtls.utils.KeyUtils.ecKeyPair
import com.example.demomtls.utils.KeyUtils.rsaKeyPair
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec

class KeyPairTest {
    fun test(): List<Arguments> = listOf(
        argumentSet("RSA", rsaKeyPair(1024)),
        argumentSet("EC", ecKeyPair()),
    )

    @ParameterizedTest
    @MethodSource
    fun test(keyPair: KeyPair) {
//        println(keyPair.private)
//        println(keyPair.private.algorithm)
//        println(keyPair.private.encoded)
//        println(keyPair.private.format)
//        println()
//        println(keyPair.public.algorithm)
//        println(keyPair.public.encoded)
//        println(keyPair.public.format)
//        println()
//        DatatypeConverter.printBase64Binary(keyPair.private.encoded).let(::println)
        println(PKCS8EncodedKeySpec(keyPair.public.encoded).encoded)
//        DatatypeConverter.printBase64Binary(keyPair.public.encoded).let(::println)
    }
}
