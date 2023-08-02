package com.example.demomtls

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime

@RestController
class DemoController {
    @GetMapping("/foo")
    suspend fun foo(): ResponseEntity<String> {
        return ResponseEntity.ok(LocalTime.now().toString())
    }
}
