package com.example.mtls.client;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@SpringBootTest
@Slf4j
class DemoClientTest {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private DemoClientProperties properties;

    @Test
    void test() {
        log.info(properties.toString());

        var timeout = Duration.ofSeconds(2);

        var httpClient = NettySslUtils.newClientWithSslIfPossible(properties.ssl(), resourceLoader)
            .responseTimeout(timeout)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout.toMillis());

        var client = WebClient.builder()
            .baseUrl("https://localhost:8443/demo/foo?n=4")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        var body = client.get().retrieve().bodyToMono(String.class).block();
        log.info("body: {}", body);
    }
}
