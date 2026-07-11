package com.example.demo.mtls.webclient.nobundle

import com.example.demo.mtls.AbstractWebTest
import com.example.demo.mtls.DemoClient
import com.example.demo.mtls.utils.TestProfiles
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(TestProfiles.NOBUNDLE)
abstract class WebClientNoBundleTest : AbstractWebTest() {
    @Autowired
    private lateinit var client: DemoClient

    @Test
    fun testLocalhost() = runTest { client.callLocalhost() }

//    @Test
    fun testIp() = runTest { client.callIp() }
}

class WebClientNoBundleNossl : WebClientNoBundleTest()

@ActiveProfiles(TestProfiles.TLS)
class WebClientNoBundleTls : WebClientNoBundleTest()

@ActiveProfiles(TestProfiles.MTLS)
class WebClientNoBundleMtls : WebClientNoBundleTest()
