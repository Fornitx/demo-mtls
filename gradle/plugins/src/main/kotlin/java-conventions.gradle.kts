plugins {
    java
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

java {
    toolchain {
        languageVersion.set(
            JavaLanguageVersion.of(libs.findVersion("jdk").get().toString())
        )
    }
}

//tasks.compileJava {
//    options.compilerArgs.addAll(
//        listOf(
//            "-Xlint:all", // Enables all recommended warnings.
//            "-Werror", // Terminates compilation when warnings occur.
//        )
//    )
//    options.encoding = "UTF-8"
//}

tasks.register<DependencyReportTask>("allDeps")
