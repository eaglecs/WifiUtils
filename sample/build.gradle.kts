plugins {
    id(GradlePluginId.ANDROID_APPLICATION)
    id(GradlePluginId.KOTLIN_ANDROID)
//    id(GradlePluginId.KOTLIN_ANDROID_EXTENSIONS)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = Artifact.ARTIFACT_GROUP + ".sample"
    compileSdk= AndroidConfig.COMPILE_SDK_VERSION
    defaultConfig {
        applicationId = Artifact.ARTIFACT_GROUP + ".sample"
        minSdk= AndroidConfig.MIN_SDK_VERSION
        targetSdk = AndroidConfig.TARGET_SDK_VERSION
        versionCode = Artifact.VERSION_CODE
        versionName = Artifact.VERSION_NAME
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
//        getByName("debug") {
//            storeFile = file("debug.keystore")
//            storePassword = "android"
//            keyAlias = "androiddebugkey"
//            keyPassword = "android"
//        }
        register("release") {
            storeFile = file("release.jks")
            storePassword = "123456a@A"
            keyAlias = "wifi_util"
            keyPassword = "123456a@A"
        }

//        getByName("release") {
//            storeFile = file("release.jks")
//            storePassword = "123456a@A"
//            keyAlias = "wifi_util"
//            keyPassword = "123456a@A"
//        }
    }

    buildTypes {

        getByName(BuildType.DEBUG) {
//            applicationIdSuffix = ".debug"
//            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            isShrinkResources = BuildTypeDebug.isShrinkResources
            isDebuggable = BuildTypeDebug.isDebuggable
        }

        getByName(BuildType.RELEASE) {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            isShrinkResources = BuildTypeRelease.isShrinkResources
            isDebuggable = BuildTypeRelease.isDebuggable
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    addAppModuleDependencies()
    addTestDependencies()
}
