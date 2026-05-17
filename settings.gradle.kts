pluginManagement {
    repositories {
//        maven("https://maven.myket.ir")
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
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        maven("https://maven.myket.ir")
        google()
        mavenCentral()
    }
}

rootProject.name = "persian-dtpicker"
include(":sample")
include(":lib")
