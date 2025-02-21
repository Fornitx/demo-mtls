package com.example.demomtls.javahttpclient

import com.example.demomtls.AbstractWebTest
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import kotlinx.coroutines.future.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ssl.SslBundles
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

abstract class JavaHttpClientBase : AbstractWebTest() {
    @Autowired
    private lateinit var sslBundles: SslBundles

    private fun newClient(): HttpClient {
        return HttpClient.newBuilder()
            .apply {
                if (isServerSsl) {
                    version(HttpClient.Version.HTTP_2)
                }
                val sslContext = if (isClientSsl) {
                    sslBundles.getBundle(clientProperties.ssl.bundle).createSslContext()
                } else {
                    SSLContext.getInstance("TLS")
                        .apply { init(null, InsecureTrustManagerFactory.INSTANCE.trustManagers, null) }
                }
                sslContext(sslContext)
            }
            .build()
    }

    @Test
    fun test() = runTest {
        newClient().use { httpClient ->
            val httpRequest = HttpRequest.newBuilder(URI(url))
                .apply {
                    if (isServerSsl) {
                        version(HttpClient.Version.HTTP_2)
                    }
                }
                .GET()
                .build()
            val httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).await()

            log.info { httpResponse.statusCode() }
            log.info { httpResponse.headers() }
            log.info { httpResponse.body() }
        }
    }

    object InsecureTrustManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate?>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate?>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<out X509Certificate?>? = emptyArray()
    }
}
