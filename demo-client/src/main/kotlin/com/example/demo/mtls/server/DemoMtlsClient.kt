package com.example.demo.mtls.server

import com.example.demo.commons.DemoProperties
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableConfigurationProperties(DemoProperties::class)
class DemoMtlsClient {
	@Component
	class DemoClientRunner(private val client: DemoClient) : CommandLineRunner {
		override fun run(vararg args: String) {
			runBlocking {
				client.callLocalhost()
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<DemoMtlsClient>(*args)
}
