dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

rootProject.name = "demo-mtls"
include(":demo-commons")
include(":demo-old")
include(":demo-server")
include(":demo-client")
