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
    plugins{
        id("org.jetbrains.kotlin.android") version "2.1.0"
        id("org.jetbrains.kotlin.kapt") version "2.1.0"
        id("androidx.compose.compiler") version "1.5.10"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Adopciones_Adptpet"
include(":app")
 