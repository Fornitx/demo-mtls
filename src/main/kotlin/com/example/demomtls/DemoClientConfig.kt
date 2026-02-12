package com.example.demomtls

import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.http.client.autoconfigure.reactive.ClientHttpConnectorBuilderCustomizer
import org.springframework.boot.http.client.reactive.ReactorClientHttpConnectorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.channel.MicrometerChannelMetricsRecorder
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

private val timeout = Duration.ofSeconds(5)

@Configuration
class DemoClientConfig {
    @Bean
    fun clientHttpConnectorBuilderCustomizer(): ClientHttpConnectorBuilderCustomizer<ReactorClientHttpConnectorBuilder> =
        ClientHttpConnectorBuilderCustomizer { builder ->
            builder.withHttpClientCustomizer { httpClient ->
                httpClient
                    .wiretap(
                        "reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG,
                        AdvancedByteBufFormat.TEXTUAL
                    )
                    .followRedirect { request, response -> true }
                    .responseTimeout(timeout)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout.toMillis().toInt())
                    .doOnConnected { con ->
                        con.addHandler(ReadTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS))
                        con.addHandler(WriteTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS))
                    }
                    .metrics(true, Supplier { MicrometerChannelMetricsRecorder("demo.webclient", "") })
            }
        }
}
