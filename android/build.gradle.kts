plugins {
    id("org.jetbrains.compose") version "0.2.0-build132"
    id("com.android.application")
    kotlin("android")
}

group = "com.secbot"
version = "1.0"

repositories {
    google()
    jcenter()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {

    val lifecycle_version = "2.3.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")


    implementation(project(":common"))
    implementation(project(":core"))
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.secbot.android"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}