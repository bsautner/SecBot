plugins {
    java
    kotlin("jvm") version "1.4.21"
}

group = "secbot"
version = "1.0-SNAPSHOT"

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "secbot.Application"
    }
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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


//    runtimeClasspath files("build/classes/kotlin/main")
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")
}
