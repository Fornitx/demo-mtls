package com.example.demomtls

import org.junit.jupiter.api.Test
import java.net.http.HttpClient
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

@Deprecated("")
class JavaHttpClientTest {
    @Test
    fun test() {
        val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init()
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .sslContext(sslContext)
            .build()
    }
}
