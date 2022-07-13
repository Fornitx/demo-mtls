package com.example.mtls.client;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.boot.web.server.Ssl;
import org.springframework.core.io.ResourceLoader;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

import static org.apache.commons.lang3.ObjectUtils.anyNull;

@UtilityClass
public class NettySslUtils {
    public static HttpClient newClientWithSslIfPossible(Ssl ssl, ResourceLoader resourceLoader) {
        var sslContext = createSslContext(ssl, resourceLoader);
        var client = HttpClient.create();
        if (sslContext != null) {
            client = client.secure(spec -> spec.sslContext(sslContext));
        }
        return client;
    }

    @SneakyThrows
    public static SslContext createSslContext(Ssl ssl, ResourceLoader resourceLoader) {
        if (ssl == null || !ssl.isEnabled()) {
            return null;
        }

        var contextBuilder = SslContextBuilder.forClient();

        var keyManagerFactory = createKeyManagerFactory(ssl, resourceLoader);
        if (keyManagerFactory != null) {
            contextBuilder = contextBuilder.keyManager(keyManagerFactory);
        }

        var trustManagerFactory = createTrustManagerFactory(ssl, resourceLoader);
        if (trustManagerFactory != null) {
            contextBuilder = contextBuilder.trustManager(trustManagerFactory);
        }

        return contextBuilder.build();
    }

    @SneakyThrows
    private static KeyManagerFactory createKeyManagerFactory(Ssl ssl, ResourceLoader resourceLoader) {
        if (anyNull(ssl.getKeyStore(), ssl.getKeyStoreType(), ssl.getKeyStorePassword())) {
            return null;
        }
        var keyStore = KeyStore.getInstance(ssl.getKeyStoreType());
        try (var keyStoreInput = resourceLoader.getResource(ssl.getKeyStore()).getInputStream()) {
            keyStore.load(keyStoreInput, ssl.getKeyStorePassword().toCharArray());
        }
        var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, ssl.getKeyStorePassword().toCharArray());
        return keyManagerFactory;
    }

    @SneakyThrows
    private static TrustManagerFactory createTrustManagerFactory(Ssl ssl, ResourceLoader resourceLoader) {
        if (anyNull(ssl.getTrustStore(), ssl.getTrustStoreType(), ssl.getTrustStorePassword())) {
            return null;
        }
        var trustStore = KeyStore.getInstance(ssl.getTrustStoreType());
        try (var trustStoreInput = resourceLoader.getResource(ssl.getTrustStore()).getInputStream()) {
            trustStore.load(trustStoreInput, ssl.getTrustStorePassword().toCharArray());
        }
        var trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory;
    }
}
