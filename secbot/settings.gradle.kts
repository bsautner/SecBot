pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

}

rootProject.name = "secbot"
include("secbot-simulator")
include("secbot-pi")
include("secbot-control")
include("secbot-api")
include("secbot-android")
