package com.example.demomtls

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(DemoProperties::class)
class DemoMtlsApplication

fun main(args: Array<String>) {
	runApplication<DemoMtlsApplication>(*args)
}
