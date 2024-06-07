package com.example.demomtls

import com.example.demomtls.utils.PASSWORD
import com.example.demomtls.utils.TEST_URL
import io.netty.handler.ssl.ApplicationProtocolConfig
import io.netty.handler.ssl.ApplicationProtocolNames
import io.netty.handler.ssl.SslContextBuilder
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import reactor.netty.http.HttpProtocol
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

@Deprecated("")
class ReactorNettyHttpClientTest {
//    @Test
    fun test() = runTest {
        val httpClient = HttpClient.create()
            .protocol(HttpProtocol.H2)
            .secure {
                it.sslContext(
                    SslContextBuilder.forClient()
                        .protocols("TLSv1.3")
                        .applicationProtocolConfig(
                            ApplicationProtocolConfig(
                                ApplicationProtocolConfig.Protocol.ALPN,
                                ApplicationProtocolConfig.SelectorFailureBehavior.FATAL_ALERT,
                                ApplicationProtocolConfig.SelectedListenerFailureBehavior.FATAL_ALERT,
                                ApplicationProtocolNames.HTTP_2
                            )
                        )
                        .keyManager(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                            init(KeyStore.getInstance("PKCS12").apply {
                                FileInputStream("etc/client.p12").use { load(it, PASSWORD) }
                            }, PASSWORD)
                        })
                        .trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                            init(KeyStore.getInstance("PKCS12").apply {
                                FileInputStream("etc/client-truststore.p12").use { load(it, PASSWORD) }
                            })
                        })
                        .build()
                )
            }
        val response = httpClient.get()
            .uri(TEST_URL)
            .responseSingle { res, content -> content.map { res to it } }
            .awaitSingle()

        println(response.first.status())
        println(response.first.responseHeaders())
        println(response.first.version())
        println(response.second.toString(StandardCharsets.UTF_8))
    }
}
