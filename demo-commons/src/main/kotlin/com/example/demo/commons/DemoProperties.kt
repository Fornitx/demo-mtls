package com.example.demo.commons

import jakarta.validation.Valid
import org.hibernate.validator.constraints.time.DurationMin
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.Ssl
import org.springframework.validation.annotation.Validated
import java.time.Duration

@ConfigurationProperties(prefix = "demo", ignoreUnknownFields = false)
@Validated
data class DemoProperties(
    @field:Valid
    val client: ClientProperties,
)

data class ClientProperties(
    val port: Int,
    val isServerSsl: Boolean,
    @DurationMin(seconds = 1)
    val timeout: Duration,
    val ssl: Ssl,
)
