package com.example.demomtls.webclient

import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import reactor.netty.channel.MicrometerChannelMetricsRecorder
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

private val timeout = Duration.ofSeconds(5)

@TestConfiguration
class TestWebClientConfig {
    @Bean
    fun wiretapMapper() = ReactorNettyHttpClientMapper { httpClient ->
        httpClient.wiretap(
            "reactor.netty.http.client.HttpClient",
            LogLevel.DEBUG,
            AdvancedByteBufFormat.TEXTUAL
        )
    }

    @Bean
    fun redirectMapper() = ReactorNettyHttpClientMapper { httpClient ->
        httpClient.followRedirect { request, response -> true }
    }

    @Bean
    fun timeoutMapper() = ReactorNettyHttpClientMapper { httpClient ->
        httpClient.responseTimeout(timeout)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout.toMillis().toInt())
            .doOnConnected { con ->
                con.addHandler(ReadTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS))
                con.addHandler(WriteTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS))
            }
    }

    @Bean
    fun metricsMapper() = ReactorNettyHttpClientMapper { httpClient ->
        httpClient.metrics(true, Supplier { MicrometerChannelMetricsRecorder("demo.webclient", "") })
    }
}
