import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.secbot"
version = "1.0"



tasks.withType<KotlinCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("io.ktor:ktor-network:1.6.7")
    implementation("io.ktor:ktor-websockets:1.6.7")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation(kotlin("stdlib"))
}