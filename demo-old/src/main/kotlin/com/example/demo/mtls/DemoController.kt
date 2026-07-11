package com.example.demo.mtls

import com.example.demo.commons.DemoConstants.PATH
import com.example.demo.commons.SSLLoggingUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

private val log = KotlinLogging.logger {}

@RestController
class DemoController {
    @GetMapping(PATH)
    suspend fun foo(request: ServerHttpRequest): ResponseEntity<String> {
        log.info { SSLLoggingUtils.sslInfo(request.sslInfo) }
        return ResponseEntity.ok(LocalTime.now().toString())
    }
}
