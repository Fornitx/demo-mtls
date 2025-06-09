package com.example.demomtls.webclient.nobundle

import com.example.demomtls.AbstractWebTest
import com.example.demomtls.ClientCustomizer
import com.example.demomtls.DemoClient
import com.example.demomtls.DemoProperties
import com.example.demomtls.utils.TestProfiles
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.ResourceUtils
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

@TestConfiguration
class TestWebClientNoBundleConfig {
    @ConditionalOnProperty("demo.client.ssl.enabled", havingValue = "true")
    @Bean
    fun secureSslCustomizer(properties: DemoProperties): ClientCustomizer = ClientCustomizer { builder ->
        builder.withHttpClientCustomizer { httpClient ->
            httpClient.secure {
                val ssl = properties.client.ssl
                val sslContext = SslContextBuilder.forClient()
                    .keyManager(KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                        val keyStorePassword = ssl.keyStorePassword.toCharArray()
                        init(KeyStore.getInstance(ssl.keyStoreType).apply {
                            ResourceUtils.getURL(ssl.keyStore).openStream().use { stream ->
                                load(stream, keyStorePassword)
                            }
                        }, keyStorePassword)
                    })
                    .trustManager(TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                        init(KeyStore.getInstance(ssl.trustStoreType).apply {
                            ResourceUtils.getURL(ssl.trustStore).openStream().use { stream ->
                                load(stream, ssl.trustStorePassword.toCharArray())
                            }
                        })
                    })
                    .build()

                it.sslContext(sslContext)
            }
        }
    }

    @ConditionalOnProperty("demo.client.ssl.enabled", havingValue = "false")
    @Bean
    fun insecureTrustManagerCustomizer(): ClientCustomizer = ClientCustomizer { builder ->
        builder.withHttpClientCustomizer { httpClient ->
            httpClient.secure {
                it.sslContext(
                    SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build()
                )
            }
        }
    }
}

@ActiveProfiles(TestProfiles.NOBUNDLE)
@Import(TestWebClientNoBundleConfig::class)
abstract class WebClientNoBundleTest : AbstractWebTest() {
    @Autowired
    private lateinit var client: DemoClient

    @Test
    fun testLocalhost() = runTest { client.callLocalhost() }

    @Test
    fun testIp() = runTest { client.callIp() }
}

class WebClientNoBundleNossl : WebClientNoBundleTest()

@ActiveProfiles(TestProfiles.TLS)
class WebClientNoBundleTls : WebClientNoBundleTest()

@ActiveProfiles(TestProfiles.MTLS)
class WebClientNoBundleMtls : WebClientNoBundleTest()
