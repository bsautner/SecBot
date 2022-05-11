import org.jetbrains.kotlin.config.JvmAnalysisFlags.useIR

plugins {
    id("org.jetbrains.compose") version "1.0.1"
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

group = "com.secbot"
version = "1.0"

repositories {
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

configurations {
    "implementation" {
     ///   exclude(group = "androidx.compose.animation")
      //  exclude(group = "androidx.compose.foundation")
   //     exclude(group = "androidx.compose.material")
 //       exclude(group = "androidx.compose.runtime")
      //  exclude(group = "androidx.compose.ui")
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
// https://mvnrepository.com/artifact/androidx.compose.runtime/runtime-livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.0.5")
    implementation(project(":core"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.secbot.android"
        minSdkVersion(30)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
       // useIR = true
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
        kotlinCompilerVersion = "1.5.10"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}