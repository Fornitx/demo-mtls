package com.example.demo.mtls.server

import com.example.demo.commons.DemoConstants.PATH
import com.example.demo.commons.DemoProperties
import com.example.demo.commons.SSLUtils.applySslIfNeeded
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.webclient.autoconfigure.WebClientSsl
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.CoExchangeFilterFunction
import org.springframework.web.reactive.function.client.CoExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange

private val log = KotlinLogging.logger {}

@Component
class DemoClient(
    demoProperties: DemoProperties,
    private val webClientBuilder: WebClient.Builder,
    private val webClientSsl: WebClientSsl,
) {
    private val clientProperties = demoProperties.client
    private val isServerSsl = clientProperties.isServerSsl
    private val prefix = if (isServerSsl) "https" else "http"
    private var localServerPort = clientProperties.port

    suspend fun callLocalhost() {
        call("$prefix://localhost:${localServerPort}${PATH}")
    }

    suspend fun callIp() {
        call("$prefix://127.0.0.1:${localServerPort}${PATH}")
    }

    suspend fun call(url: String) {
        val entity = webClientBuilder.baseUrl(url)
            .applySslIfNeeded(clientProperties, webClientSsl)
            .filters {
                it.add(logRequest())
                it.add(logResponse())
            }
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
