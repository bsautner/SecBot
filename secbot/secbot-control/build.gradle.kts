plugins {
    kotlin("jvm")
    java
}

group = "com.secbot"
version = "1.0-SNAPSHOT"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    mavenCentral()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    kotlinOptions.jvmTarget = "11"
}
dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation(kotlin("stdlib"))
}
