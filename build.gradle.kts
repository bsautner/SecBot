buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}

group = "com.secbot"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
        google()
        jcenter()
        mavenLocal()
        mavenCentral()
    }
    buildDir = File("/mnt/ram/${rootProject.name}/${project.name}")
}
