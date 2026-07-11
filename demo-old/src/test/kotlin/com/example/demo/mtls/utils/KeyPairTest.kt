package com.example.demo.mtls.utils

import jakarta.xml.bind.DatatypeConverter
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.ArgumentSet
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource
import java.security.KeyPair

class KeyPairTest {
    fun test(): List<ArgumentSet> = listOf(
        argumentSet("RSA", KeyUtils.rsaKeyPair(1024)),
        argumentSet("EC", KeyUtils.ecKeyPair()),
    )

    @ParameterizedTest
    @MethodSource
    fun test(keyPair: KeyPair) {
        println(keyPair.private)
        println(keyPair.private.algorithm)
        println(keyPair.private.encoded)
        println(keyPair.private.format)
        println()
        println(keyPair.public.algorithm)
        println(keyPair.public.encoded)
        println(keyPair.public.format)
        println()
        DatatypeConverter.printBase64Binary(keyPair.private.encoded).let(::println)
        DatatypeConverter.printBase64Binary(keyPair.public.encoded).let(::println)
    }
}
