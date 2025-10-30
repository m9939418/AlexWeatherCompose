pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlinx.kover") version "0.9.3"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AlexWeatherDemo"
include(":app", ":data", ":domain", ":core", ":mock")
include(":feature")
include(":feature:web")
include(":core:navigation")
include(":feature:home")
include(":feature:weekly_detail")
include(":core-ui")
include(":feature:today_detail")
