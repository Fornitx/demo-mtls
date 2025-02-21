package com.example.demomtls

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.reflect.jvm.jvmName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractWebTest {
    protected val log = KotlinLogging.logger(this::class.jvmName)

    @LocalServerPort
    private var localServerPort: Int = 0

    @Autowired
    private lateinit var serverProperties: ServerProperties

    @Autowired
    protected lateinit var clientProperties: DemoProperties

    protected val isServerSsl: Boolean
        get() = serverProperties.ssl.isEnabled

    protected val isClientSsl: Boolean
        get() = clientProperties.ssl.isEnabled

    private val prefix: String
        get() = if (isServerSsl) "https" else "http"

    protected val url: String
        get() = "$prefix://localhost:$localServerPort${PATH}"
}
