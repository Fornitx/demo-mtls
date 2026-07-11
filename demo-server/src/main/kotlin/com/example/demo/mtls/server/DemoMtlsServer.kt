package com.example.demo.mtls.server

import com.example.demo.commons.DemoConstants.PATH
import com.example.demo.commons.SSLLoggingUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import kotlin.reflect.jvm.jvmName

@SpringBootApplication
class DemoMtlsServer {
    @RestController
    class DemoController {
        private val log = KotlinLogging.logger(DemoController::class.jvmName)

        @GetMapping(PATH)
        suspend fun foo(request: ServerHttpRequest): ResponseEntity<String> {
            log.info { SSLLoggingUtils.sslInfo(request.sslInfo) }
            return ResponseEntity.ok(LocalTime.now().toString())
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoMtlsServer>(*args)
}
