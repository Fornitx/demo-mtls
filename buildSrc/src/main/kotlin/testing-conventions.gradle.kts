plugins {
    id("java-conventions")

    id("com.bakdata.mockito")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
