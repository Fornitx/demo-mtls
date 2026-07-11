plugins {
    id("kotlin-conventions")
    id("spring-conventions")
    id("testing-conventions")
}

dependencies {
    implementation(project(":demo-commons"))
}
