package com.example.demo.mtls.webclient.bundle.openssl

import com.example.demo.mtls.AbstractWebTest
import com.example.demo.mtls.DemoClient
import com.example.demo.mtls.utils.TestProfiles
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TestProfiles.BUNDLE_OPENSSL)
abstract class WebClientBundleTest : AbstractWebTest() {
    @Autowired
    private lateinit var client: DemoClient

    @Test
    fun testLocalhost() = runTest { client.callLocalhost() }

    //    @Test
    fun testIp() = runTest { client.callIp() }
}

class WebClientBundleNossl : WebClientBundleTest()

@ActiveProfiles(TestProfiles.TLS)
class WebClientBundleTls : WebClientBundleTest()

@ActiveProfiles(TestProfiles.MTLS)
class WebClientBundleMtls : WebClientBundleTest()
