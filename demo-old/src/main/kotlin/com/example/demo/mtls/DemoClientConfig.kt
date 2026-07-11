package com.example.demo.mtls

import com.example.demo.commons.DemoProperties
import com.example.demo.commons.SSLLoggingUtils
import com.example.demo.commons.SSLUtils.applySslIfNeeded
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.channel.MicrometerChannelMetricsRecorder
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.util.function.Supplier

private val log = KotlinLogging.logger {}

@Configuration
class DemoClientConfig(demoProperties: DemoProperties) {
    private val properties = demoProperties.client

    @Bean
    fun clientHttpConnectorBuilderCustomizer(): ClientCustomizer {
        val timeout = properties.timeout
        val timeoutMillis = timeout.toMillis()
        return ClientCustomizer { builder ->
            builder.withHttpClientCustomizer { httpClient ->
                httpClient
                    .wiretap(
                        "reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG,
                        AdvancedByteBufFormat.TEXTUAL
                    )
//                    .followRedirect { request, response -> true }
                    .responseTimeout(timeout)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis.toInt())
                    .doOnConnected { con ->
                        log.info { SSLLoggingUtils.connectionSsl(con) }
//                        con.addHandler(ReadTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
//                        con.addHandler(WriteTimeoutHandler(timeoutMillis, TimeUnit.MILLISECONDS))
                    }
                    .metrics(true, Supplier { MicrometerChannelMetricsRecorder("demo.webclient", "") })
                    .applySslIfNeeded(properties)
            }
        }
    }
}
