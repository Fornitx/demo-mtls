package com.example.demomtls.webclient.nobundle

import com.example.demomtls.DemoProperties
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

@TestConfiguration
class TestWebClientNoBundleConfig {
    @ConditionalOnProperty("demo.client.ssl.enabled", havingValue = "true")
    @Bean
    fun secureSslMapper(properties: DemoProperties) = ReactorNettyHttpClientMapper { httpClient ->
        val ssl = properties.client.ssl
        val sslContext = SslContextBuilder.forClient()
            .keyManager(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                val keyStorePassword = ssl.keyStorePassword.toCharArray()
                init(KeyStore.getInstance(ssl.keyStoreType).apply {
                    ResourceUtils.getURL(ssl.keyStore).openStream().use { stream ->
                        load(stream, keyStorePassword)
                    }
                }, keyStorePassword)
            })
            .trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(KeyStore.getInstance(ssl.trustStoreType).apply {
                    ResourceUtils.getURL(ssl.trustStore).openStream().use { stream ->
                        load(stream, ssl.trustStorePassword.toCharArray())
                    }
                })
            })
            .build()

        httpClient.secure { it.sslContext(sslContext) }
    }

    @ConditionalOnProperty("demo.client.ssl.enabled", havingValue = "false")
    @Bean
    fun insecureSslMapper() = ReactorNettyHttpClientMapper { httpClient ->
        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        httpClient.secure { it.sslContext(sslContext) }
    }
}
