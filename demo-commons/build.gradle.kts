plugins {
    id("kotlin-conventions")
}

dependencies {
    api(platform(libs.spring.bom))

    api("org.springframework.boot:spring-boot-web-server")
    api("org.springframework.boot:spring-boot-webclient")
    api("org.springframework:spring-webflux")

    api("io.projectreactor.netty:reactor-netty-http")

    api("org.hibernate.validator:hibernate-validator")
    api("jakarta.validation:jakarta.validation-api")
}
