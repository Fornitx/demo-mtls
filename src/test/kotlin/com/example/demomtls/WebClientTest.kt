package com.example.demomtls

import com.example.demomtls.utils.TEST_URL
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.Ssl
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebClientTest {
    @Autowired
    private lateinit var webClientBuilder: WebClient.Builder

    @Autowired
    private lateinit var properties: DemoProperties

    @Test
    fun test() = runTest {
        var httpClient = HttpClient.create()
        val ssl = properties.ssl
        if (ssl.isEnabled) {
            httpClient = httpClient.secure {
                it.sslContext(createSslContext(ssl))
            }
        }
        val connector = ReactorClientHttpConnector(httpClient)
        val entity = webClientBuilder.baseUrl(TEST_URL)
            .clientConnector(connector)
            .build()
            .get()
            .awaitExchange { it.awaitEntity<String>() }

        println(entity.statusCode)
        println(entity.headers)
        println(entity.body)
    }

    private fun createSslContext(ssl: Ssl): SslContext = SslContextBuilder.forClient()
        .keyManager(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(KeyStore.getInstance(ssl.keyStoreType).apply {
                FileInputStream(ssl.keyStore).use { load(it, ssl.keyStorePassword.toCharArray()) }
            }, ssl.keyStorePassword.toCharArray())
        })
        .trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(KeyStore.getInstance(ssl.trustStoreType).apply {
                FileInputStream(ssl.trustStore).use { load(it, ssl.trustStorePassword.toCharArray()) }
            })
        })
        .build()
}
