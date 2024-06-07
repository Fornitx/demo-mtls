plugins {
    id("org.springframework.boot") version System.getProperty("spring_version")
    id("io.spring.dependency-management") version System.getProperty("spring_dm_version")
    kotlin("jvm") version System.getProperty("kotlin_version")
    kotlin("plugin.spring") version System.getProperty("kotlin_version")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

ext["kotlin-coroutines.version"] = System.getProperty("kotlin_coroutines_version")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("io.github.oshai:kotlin-logging-jvm:" + System.getProperty("kotlin_logging_version"))

//	implementation("io.github.hakky54:sslcontext-kickstart-for-netty:8.3.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

//	implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
//	implementation("org.apache.httpcomponents.core5:httpcore5:5.2.1")
//	implementation("org.apache.httpcomponents.core5:httpcore5-h2:5.2.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    enabled = false
}
