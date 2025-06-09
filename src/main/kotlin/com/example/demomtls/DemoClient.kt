package com.example.demomtls

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.observation.ObservationRegistry
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl
import org.springframework.boot.web.reactive.context.ReactiveWebServerInitializedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*
import java.util.function.Consumer

private val log = KotlinLogging.logger {}

@Component
class DemoClient(
    serverProperties: ServerProperties,
    demoProperties: DemoProperties,
    private val webClientBuilder: WebClient.Builder,
    private val webClientSsl: WebClientSsl,
    private val observationRegistry: ObservationRegistry,
) {
    private val clientProperties = demoProperties.client

    private val isServerSsl = serverProperties.ssl?.isEnabled == true
    private val isClientSsl = clientProperties.ssl.isEnabled

    private val prefix = if (isServerSsl) "https" else "http"

    private var localServerPort = 0

    @EventListener(ReactiveWebServerInitializedEvent::class)
    fun setPort(event: ReactiveWebServerInitializedEvent) {
        this.localServerPort = event.webServer.port
    }

    suspend fun callLocalhost() {
        call("$prefix://localhost:${localServerPort}${PATH}")
    }

    suspend fun callIp() {
        call("$prefix://127.0.0.1:${localServerPort}${PATH}")
    }

    suspend fun call(url: String) {
        val entity = webClientBuilder.baseUrl(url)
            .apply(
                if (isClientSsl && clientProperties.ssl.bundle != null) {
                    webClientSsl.fromBundle(clientProperties.ssl.bundle)
                } else {
                    Consumer {}
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
