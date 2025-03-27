package com.example.demomtls

import jakarta.validation.Valid
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.Ssl
import org.springframework.validation.annotation.Validated

@ConfigurationProperties("demo")
@Validated
data class DemoProperties(
    @field:Valid
    val client: ClientProperties,
)

data class ClientProperties(
    val ssl: Ssl,
)
