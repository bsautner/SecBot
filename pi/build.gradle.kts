import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "com.secbot"
version = "1.0"



tasks.withType<KotlinCompile>() {
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
//tasks {
//    "build" {
//        dependsOn(fatJar)
//    }
//}

dependencies {

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("io.ktor:ktor-network:1.4.1")
    implementation("io.ktor:ktor-websockets:1.4.1")
    implementation("com.github.Hopding:JRPiCam:1.1.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.0")

    implementation("com.diozero:diozero-provider-wiringpi:0.11")
    implementation("com.diozero:diozero-core:0.11")
    implementation("com.pi4j:pi4j-core:1.2")
    implementation("com.google.code.gson:gson:2.8.7")

    implementation("com.squareup.okhttp:okhttp:2.7.5")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.dagger:dagger-android:2.35.1")
    annotationProcessor( "com.google.dagger:dagger-android-processor:2.32")
    annotationProcessor( "com.google.dagger:dagger-compiler:2.37")
    kapt("com.google.dagger:dagger-compiler:2.37")

    implementation(project(":core"))


    implementation(kotlin("stdlib"))
}

//val jar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
//    manifest {
//        attributes["Main-Class"] = "com.secbot.pi.Application"
//    }
//}
//val fatJar = task("fatJar", type = org.gradle.jvm.tasks.Jar::class) {
//    baseName = "${project.name}-fat"
//    manifest {
//        attributes["Implementation-Title"] = "SecBot Go!"
//        attributes["Implementation-Version"] = version
//        attributes["Main-Class"] = "com.secbot.pi.Application"
//    }
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    with(tasks.jar.get() as CopySpec)
//}