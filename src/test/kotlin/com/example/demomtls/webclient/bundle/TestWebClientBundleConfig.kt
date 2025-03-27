package com.example.demomtls.webclient.bundle

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestWebClientBundleConfig {
    @ConditionalOnProperty("demo.client.ssl.enabled", havingValue = "false")
    @Bean
    fun insecureSslMapper() = ReactorNettyHttpClientMapper { httpClient ->
        val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
        httpClient.secure { it.sslContext(sslContext) }
    }
}
