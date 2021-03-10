pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    
}
rootProject.name = "secbot"


include(":android")
include(":desktop")
include(":common")
include(":core")
include(":pi")
include(":api")

