plugins {
    java
    kotlin("jvm")
    kotlin("kapt")


}

group = "com.secbot"
version = "1.0-SNAPSHOT"

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.secbot.Application"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    kotlinOptions.jvmTarget = "11"
}

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
    google()
    maven(url= "https://oss.sonatype.org/content/groups/public")
    maven(url ="https://jitpack.io")
}
dependencies {
    implementation("com.github.Hopding:JRPiCam:1.1.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("com.diozero:diozero-provider-wiringpi:0.11")
    implementation("com.diozero:diozero-core:0.11")
    implementation("com.pi4j:pi4j-core:1.2")
    implementation("com.google.code.gson:gson:2.8.6")

    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    implementation("com.squareup.retrofit2:converter-gson:2.7.2")
    implementation("com.google.dagger:dagger-android:2.32")
    annotationProcessor( "com.google.dagger:dagger-android-processor:2.32")
    annotationProcessor( "com.google.dagger:dagger-compiler:2.32")
    kapt("com.google.dagger:dagger-compiler:2.32")

    implementation(project(":secbot-control"))


    implementation(kotlin("stdlib"))
}