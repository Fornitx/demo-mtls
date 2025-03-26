package com.example.demomtls.webclient

import com.example.demomtls.AbstractWebTest
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ssl.SslBundle
import org.springframework.boot.ssl.SslBundles
import org.springframework.boot.ssl.SslOptions
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.netty.http.HttpProtocol.H2
import reactor.netty.http.HttpProtocol.HTTP11
import reactor.netty.http.client.HttpClient
import java.util.function.Consumer

abstract class WebClientBase : AbstractWebTest() {
    @Autowired
    private lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    private lateinit var sslBundles: SslBundles

    @Test
    fun test() = runTest {
        val entity = webClientBuilder.baseUrl(url)
            .apply(
                if (isClientSsl)
                    SecureWebClientBuilderConsumer(sslBundles.getBundle(clientProperties.ssl.bundle))
                else
                    InsecureWebClientBuilderConsumer
            )
            .build()
            .get()
            .awaitExchange { it.awaitEntity<String>() }

        log.info { entity.statusCode }
        log.info { entity.headers }
        log.info { entity.body }
    }

    class SecureWebClientBuilderConsumer(private val sslBundle: SslBundle) : Consumer<WebClient.Builder> {
        override fun accept(webClientBuilder: WebClient.Builder) {
            val options = sslBundle.options
            val managers = sslBundle.managers
            val sslContext = SslContextBuilder.forClient()
                .keyManager(managers.keyManagerFactory)
                .trustManager(managers.trustManagerFactory)
                .ciphers(SslOptions.asSet(options.ciphers))
                .protocols(*options.enabledProtocols)
                .build()
            val httpClient = HttpClient.create().protocol(H2).secure { it.sslContext(sslContext) }
            val clientHttpConnector = ReactorClientHttpConnector(httpClient)
            webClientBuilder.clientConnector(clientHttpConnector).build()
        }
    }

    object InsecureWebClientBuilderConsumer : Consumer<WebClient.Builder> {
        override fun accept(webClientBuilder: WebClient.Builder) {
            val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
            val httpClient = HttpClient.create().protocol(HTTP11, H2).secure { it.sslContext(sslContext) }
            val clientHttpConnector = ReactorClientHttpConnector(httpClient)
            webClientBuilder.clientConnector(clientHttpConnector).build()
        }
    }
}
