package com.example.mtls.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.Ssl;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.time.Duration;

@SpringBootTest
@Slf4j
class DemoClientTest {
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private DemoClientProperties properties;

    @Test
    void test() throws Exception {
        log.info(properties.toString());
        var ssl = properties.ssl();
        var sslContext = createSslContext(ssl);

        var timeout = Duration.ofSeconds(2);

        var httpClient = HttpClient.create()
            .responseTimeout(timeout)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout.toMillis());

        if (sslContext != null) {
            httpClient = httpClient.secure(spec -> spec.sslContext(sslContext));
        }

        var client = WebClient.builder()
            .baseUrl("https://localhost:8443/demo/foo?n=4")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        var body = client.get().retrieve().bodyToMono(String.class).block();
        log.info("body: {}", body);
    }

    private SslContext createSslContext(Ssl ssl) throws Exception {
        if (ssl == null || !ssl.isEnabled()) {
            return null;
        }
        var keyStore = KeyStore.getInstance(ssl.getKeyStoreType());
        var trustStore = KeyStore.getInstance(ssl.getTrustStoreType());

        try (var keyStoreInput = resourceLoader.getResource(ssl.getKeyStore()).getInputStream();
             var trustStoreInput = resourceLoader.getResource(ssl.getTrustStore()).getInputStream()) {
            keyStore.load(keyStoreInput, ssl.getKeyStorePassword().toCharArray());
            trustStore.load(trustStoreInput, ssl.getTrustStorePassword().toCharArray());
        }

        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, ssl.getKeyStorePassword().toCharArray());

        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return SslContextBuilder.forClient()
            .keyManager(keyManagerFactory)
            .trustManager(trustManagerFactory)
//            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
    }
}
