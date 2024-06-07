package com.example.demomtls

import com.example.demomtls.utils.PROFILE_H2
import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

private val log = KotlinLogging.logger {}

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles(PROFILE_H2)
class WebTestClientTestH2 {
    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun test() {
        val responseBody = client.get()
            .uri(PATH)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<String>()
            .returnResult()
            .responseBody

        log.info { "responseBody = $responseBody" }
    }
}
