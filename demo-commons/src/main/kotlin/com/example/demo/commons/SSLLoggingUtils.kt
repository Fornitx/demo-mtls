package com.example.demo.commons

import io.netty.handler.ssl.SslHandler
import org.springframework.http.server.reactive.SslInfo
import reactor.netty.Connection

object SSLLoggingUtils {
     fun sslInfo(sslInfo: SslInfo?): CharSequence {
        val builder = StringBuilder()
        buildLog(sslInfo, builder)
        return builder.trimEnd()
    }

    private fun buildLog(sslInfo: SslInfo?, builder: StringBuilder) {
        if (sslInfo == null) {
            builder.appendLine("sslInfo is null")
            return
        }

        val peerCertificates = sslInfo.peerCertificates
        if (peerCertificates == null) {
            builder.appendLine("peerCertificates is null")
            return
        }

        builder.appendLine("peerCertificates.size = ${peerCertificates.size}")

        for (peerCertificate in peerCertificates) {
            builder.appendLine("peerCertificate = $peerCertificate")
        }
    }

    fun connectionSsl(con: Connection): CharSequence {
        val builder = StringBuilder()
        builder.appendLine("HttpClient.doOnConnected() :")
        buildLog(con, builder)
        return builder.trimEnd()
    }

    private fun buildLog(con: Connection, builder: StringBuilder) {
        val sslHandler = con.channel().pipeline().get(SslHandler::class.java)
        builder.appendLine("sslHandler = $sslHandler")

        if (sslHandler == null) {
            return
        }

        val sslEngine = sslHandler.engine()
        builder.appendLine("sslEngine = $sslEngine")

        if (sslEngine == null) {
            return
        }

        val sslSession = sslEngine.session
        builder.appendLine("sslSession = $sslSession")

        if (sslSession == null) {
            return
        }

        builder.appendLine("protocol = ${sslSession.protocol}")
        builder.appendLine("sessionContext = ${sslSession.sessionContext}")
        builder.appendLine("cipherSuite = ${sslSession.cipherSuite}")
        builder.appendLine("valueNames = ${sslSession.valueNames.contentToString()}")
        builder.appendLine("isValid = ${sslSession.isValid}")
        builder.appendLine("peerHost = ${sslSession.peerHost}")
        builder.appendLine("peerPort = ${sslSession.peerPort}")
        builder.appendLine("localPrincipal = ${sslSession.localPrincipal}")
        builder.appendLine("peerPrincipal = ${sslSession.peerPrincipal}")
        builder.appendLine("localCertificates.size = ${sslSession.localCertificates?.size ?: -1}")
        sslSession.localCertificates?.forEach { cert ->
            builder.appendLine("localCertificate = $cert")
        }
        builder.appendLine("peerCertificates.size = ${sslSession.peerCertificates?.size ?: -1}")
        sslSession.peerCertificates?.forEach { cert ->
            builder.appendLine("peerCertificate = $cert")
        }
    }
}