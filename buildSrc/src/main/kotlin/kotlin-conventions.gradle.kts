import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("java-conventions")

    kotlin("jvm")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

kotlin {
    jvmToolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.findVersion("jdk").get().toString())
        )
    }
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        jvmTarget.set(JvmTarget.valueOf("JVM_${libs.findVersion("jdk").get()}"))
        languageVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.findVersion("kotlin").get().toString().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
        apiVersion.set(
            KotlinVersion.valueOf(
                "KOTLIN_${
                    libs.findVersion("kotlin").get().toString().substringBeforeLast(".").replace(".", "_")
                }"
            )
        )
    }
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
