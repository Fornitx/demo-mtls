package com.example.demomtls.webclient

import com.example.demomtls.AbstractWebTest
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.netty.http.client.HttpClient
import java.util.function.Consumer

abstract class WebClientBase : AbstractWebTest() {
    @Autowired
    private lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    private lateinit var webClientSsl: WebClientSsl

    @Test
    fun test() = runTest {
        val entity = webClientBuilder.baseUrl(url)
            .apply(
                if (isClientSsl)
                    webClientSsl.fromBundle(clientProperties.ssl.bundle)
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

    object InsecureWebClientBuilderConsumer : Consumer<WebClient.Builder> {
        override fun accept(webClientBuilder: WebClient.Builder) {
            val sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
            val httpClient = HttpClient.create().secure { it.sslContext(sslContext) }
            val clientHttpConnector = ReactorClientHttpConnector(httpClient)
            webClientBuilder.clientConnector(clientHttpConnector).build()
        }
    }
}
