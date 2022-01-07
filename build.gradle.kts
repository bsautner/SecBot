buildscript {

    val compose_version by extra("1.0.0")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.0.4")
    }
}

group = "com.secbot"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
        mavenCentral()
    }
  //  buildDir = File("/mnt/ram/${rootProject.name}/${project.name}")
}
