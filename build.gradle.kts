//plugins {
//    kotlin("jvm") version "2.2.21"
//    kotlin("plugin.spring") version "2.2.21"
//
//    id("org.springframework.boot") version "4.0.7"
//    id("io.spring.dependency-management") version "1.1.7"
//
//    id("com.bakdata.mockito") version "2.2.0"
//}
//
//group = "com.example"
//version = "0.0.1-SNAPSHOT"
//
//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21)
//    }
//}
//
//dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-webclient")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")
//
//    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//    implementation("tools.jackson.module:jackson-module-kotlin")
//
//    implementation("io.github.oshai:kotlin-logging:8.0.4")
//
//    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
//
//    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-webclient-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test")
//
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
//
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//}
//
//kotlin {
//    compilerOptions {
//        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
//    }
//}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}
//
//tasks.jar {
//    enabled = false
//}
