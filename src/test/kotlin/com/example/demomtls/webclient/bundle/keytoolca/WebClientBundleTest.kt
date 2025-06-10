package com.example.demomtls.webclient.bundle.keytoolca

import com.example.demomtls.AbstractWebTest
import com.example.demomtls.ClientCustomizer
import com.example.demomtls.DemoClient
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

@TestConfiguration
class TestWebClientBundleConfig {
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

@ActiveProfiles(TestProfiles.BUNDLE_KEYTOOL_CA)
@Import(TestWebClientBundleConfig::class)
abstract class WebClientBundleTest : AbstractWebTest() {
    @Autowired
    private lateinit var client: DemoClient

    @Test
    fun testLocalhost() = runTest { client.callLocalhost() }

    @Test
    fun testIp() = runTest { client.callIp() }
}

class WebClientBundleNossl : WebClientBundleTest()

@ActiveProfiles(TestProfiles.TLS)
class WebClientBundleTls : WebClientBundleTest()

@ActiveProfiles(TestProfiles.MTLS)
class WebClientBundleMtls : WebClientBundleTest()
