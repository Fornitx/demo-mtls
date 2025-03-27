package com.example.demomtls.webclient.bundle

import com.example.demomtls.AbstractWebTest
import com.example.demomtls.utils.TestProfiles
import com.example.demomtls.webclient.TestWebClientConfig
import io.micrometer.observation.ObservationRegistry
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.CoExchangeFilterFunction
import org.springframework.web.reactive.function.client.CoExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import java.util.function.Consumer

@ActiveProfiles(TestProfiles.BUNDLE)
@Import(TestWebClientConfig::class, TestWebClientBundleConfig::class)
abstract class WebClientBundleBase : AbstractWebTest() {
    @Autowired
    private lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    private lateinit var observationRegistry: ObservationRegistry

    @Autowired
    private lateinit var webClientSsl: WebClientSsl

    @Test
    fun test() = runTest {
        val entity = webClientBuilder.baseUrl(url)
            .apply(
                if (isClientSsl) {
                    webClientSsl.fromBundle(clientProperties.client.ssl.bundle)
                } else {
                    Consumer<WebClient.Builder> {}
                })
            .filters {
                it.add(logRequest())
                it.add(logResponse())
            }
            .observationRegistry(observationRegistry)
            .build()
            .get()
            .awaitExchange { it.awaitEntity<String>() }

        log.info { entity.statusCode }
        log.info { entity.headers }
        log.info { entity.body }
    }

    private fun logRequest(): CoExchangeFilterFunction = object : CoExchangeFilterFunction() {
        override suspend fun filter(request: ClientRequest, next: CoExchangeFunction): ClientResponse {
            return next.exchange(request)
        }
    }

    private fun logResponse(): CoExchangeFilterFunction = object : CoExchangeFilterFunction() {
        override suspend fun filter(request: ClientRequest, next: CoExchangeFunction): ClientResponse {
            return next.exchange(request)
        }
    }
}
