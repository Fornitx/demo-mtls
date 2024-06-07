package com.example.demomtls

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.server.Ssl

@ConfigurationProperties("demo")
data class DemoProperties(
    val ssl: Ssl
)
