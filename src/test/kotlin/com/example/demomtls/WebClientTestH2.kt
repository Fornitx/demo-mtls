package com.example.demomtls

import com.example.demomtls.utils.PASSWORD
import com.example.demomtls.utils.PROFILE_H2
import com.example.demomtls.utils.TEST_URL
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitExchange
import reactor.netty.http.Http2SslContextSpec
import reactor.netty.http.HttpProtocol
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(PROFILE_H2)
class WebClientTestH2 {
    @Test
    fun test() = runTest {
        val sslContext = Http2SslContextSpec.forClient()
//                        .protocols("TLSv1.3")
//                        .applicationProtocolConfig(
//                            ApplicationProtocolConfig(
//                                ApplicationProtocolConfig.Protocol.ALPN,
//                                ApplicationProtocolConfig.SelectorFailureBehavior.FATAL_ALERT,
//                                ApplicationProtocolConfig.SelectedListenerFailureBehavior.FATAL_ALERT,
//                                ApplicationProtocolNames.HTTP_2
//                            )
//                        )
            .configure { sslCtxBuilder ->
                sslCtxBuilder
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
            }
        val httpClient = HttpClient.create()
            .protocol(HttpProtocol.H2)
            .secure { it.sslContext(sslContext) }
        val connector = ReactorClientHttpConnector(httpClient)
        val entity = WebClient.builder()
            .baseUrl(TEST_URL)
            .clientConnector(connector)
            .build()
            .get()
            .awaitExchange { it.awaitEntity<String>() }

        println(entity.statusCode)
        println(entity.headers)
        println(entity.body)
    }
}
