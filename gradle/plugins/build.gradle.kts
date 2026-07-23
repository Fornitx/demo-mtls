import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main'
    // that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.versions.jdk.get())
        )
    }
    compilerOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs.add("-Xjsr305=strict")
        allWarningsAsErrors = true
        jvmTarget.set(JvmTarget.valueOf("JVM_${libs.versions.jdk.get()}"))
        languageVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.versions.kotlin.core.get().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
        apiVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.versions.kotlin.core.get().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
    }
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.spring.gradlePlugin)
    implementation(libs.spring.boot.gradlePlugin)
    implementation(libs.spring.dm.gradlePlugin)
    implementation(libs.bakdata.mockito.gradlePlugin)
}
