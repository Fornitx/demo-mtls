package com.example.demo.commons

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.boot.webclient.autoconfigure.WebClientSsl
import org.springframework.util.ResourceUtils
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

object SSLUtils {
    fun WebClient.Builder.applySslIfNeeded(
        properties: ClientProperties,
        webClientSsl: WebClientSsl
    ): WebClient.Builder {
        return if (properties.ssl.isEnabled && !properties.ssl.bundle.isNullOrBlank()) {
            this.apply(webClientSsl.fromBundle(properties.ssl.bundle!!))
        } else this
    }

    fun HttpClient.applySslIfNeeded(properties: ClientProperties): HttpClient {
        val ssl = properties.ssl
        val isClientSsl = ssl.isEnabled
        val isServerSsl = properties.isServerSsl
        val noBundle = ssl.bundle.isNullOrBlank()
        return if (isClientSsl && noBundle) {
            this.secure {
                val sslContext = SslContextBuilder.forClient()
                    .keyManager(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                        val keyStorePassword = ssl.keyStorePassword!!.toCharArray()
                        init(KeyStore.getInstance(ssl.keyStoreType).apply {
                            ResourceUtils.getURL(ssl.keyStore!!).openStream().use { stream ->
                                load(stream, keyStorePassword)
                            }
                        }, keyStorePassword)
                    })
                    .trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                        init(KeyStore.getInstance(ssl.trustStoreType).apply {
                            ResourceUtils.getURL(ssl.trustStore!!).openStream().use { stream ->
                                load(stream, ssl.trustStorePassword!!.toCharArray())
                            }
                        })
                    })
                it.sslContext(sslContext.build())
            }
        } else if (isServerSsl && !isClientSsl) {
            this.secure {
                val sslContextBuilder = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                it.sslContext(sslContextBuilder.build())
            }
        } else {
            this
        }
    }
}