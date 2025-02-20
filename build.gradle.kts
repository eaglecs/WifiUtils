import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        mavenCentral()
        maven ("https://plugins.gradle.org/m2/")
        maven("https://www.jitpack.io")
        maven("https://mvnrepository.com")
        maven("https://jcenter.bintray.com")
        google()
        mavenLocal()
    }
    dependencies {

        classpath(GradlePluginId.ANDROID_GRADLE_PLUGIN)
        classpath(GradlePluginId.KOTLIN_GRADLE_PLUGIN)
    }
}

plugins {
    id(GradlePluginId.DEPENDENCY_UPDATE) version GradlePluginVersion.GRADLE_VERSION_PLUGIN
}

allprojects {

    repositories {
        mavenCentral()
        maven ("https://plugins.gradle.org/m2/")
        maven("https://www.jitpack.io")
        maven("https://mvnrepository.com")
        maven("https://jcenter.bintray.com")
        google()
        mavenLocal()
        gradlePluginPortal()
    }
}

tasks.withType<DependencyUpdatesTask> {

    // Reject all non stable versions
    rejectVersionIf {
        isNonStable(candidate.version)
    }

    // Disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

    // Using the full syntax
    resolutionStrategy {
        componentSelection {
            all {
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Release candidate")
                }
            }
        }
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}