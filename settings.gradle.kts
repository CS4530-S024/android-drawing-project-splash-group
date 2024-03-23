pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            library("androidx-material3-android", "androidx.compose.material3", "material3-android").withoutVersion()
        }
    }
}

rootProject.name = "Drawing_prototype"
include(":app")
 