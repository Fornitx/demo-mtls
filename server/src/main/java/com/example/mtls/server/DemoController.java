package com.example.mtls.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/demo")
@Slf4j
public class DemoController {
    @GetMapping(value = "/foo", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> getFoo(@RequestParam("n") Integer n) {
        return Mono.fromCallable(() -> "Bar".repeat(n))
            .map(ResponseEntity::ok);
    }
}
