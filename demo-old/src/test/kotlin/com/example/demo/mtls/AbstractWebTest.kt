package com.example.demo.mtls

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import kotlin.reflect.jvm.jvmName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractWebTest {
    protected val log = KotlinLogging.logger(this::class.jvmName)
}
