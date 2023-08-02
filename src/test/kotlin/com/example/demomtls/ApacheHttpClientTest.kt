package com.example.demomtls

import com.example.demomtls.utils.PASSWORD
import org.apache.hc.client5.http.config.TlsConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder
import org.apache.hc.core5.http.ssl.TLS
import org.apache.hc.core5.io.ModalCloseable
import org.apache.hc.core5.ssl.SSLContexts
import org.junit.jupiter.api.Test
import java.io.Closeable
import java.io.File

class ApacheHttpClientTest {
    @Test
    fun test() {
        val sslContext = SSLContexts.custom()
            .loadKeyMaterial(File("etc/client.p12"), PASSWORD, PASSWORD)
            .loadTrustMaterial(File("etc/client-truststore.p12"), PASSWORD)
            .build()
        val sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
            .setSslContext(sslContext)
            .build()
        val connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslSocketFactory)
            .setDefaultTlsConfig(
                TlsConfig.custom()
                    .setSupportedProtocols(TLS.V_1_3)
                    .build()
            )
            .build()

//        HttpClients.custom().setConnectionManager(connectionManager).build().use { client ->
//            val httpGet = HttpGet(TEST_URL)
//            client.execute(httpGet, { response ->
//                println(response)
//            })
//        }
        val a : AutoCloseable = HttpClients.createDefault()
        val b : Closeable = HttpClients.createDefault()
        val c : ModalCloseable = HttpClients.createDefault()
    }
}
