package com.example.mtls.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.Ssl;

@ConfigurationProperties(prefix = "demo", ignoreUnknownFields = false)
public record DemoClientProperties(
    Ssl ssl
) {
}
