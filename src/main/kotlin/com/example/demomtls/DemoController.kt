package com.example.demomtls

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

const val PATH = "/foo"

private val log = KotlinLogging.logger {}

@RestController
class DemoController {
    @GetMapping(PATH)
    suspend fun foo(request: ServerHttpRequest): ResponseEntity<String> {
        log.info { request.sslInfo!!.peerCertificates!![0] }
        return ResponseEntity.ok(LocalTime.now().toString())
    }
}
