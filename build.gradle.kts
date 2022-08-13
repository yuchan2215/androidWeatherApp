// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val navVersion by extra("2.5.0")
    val roomVersion by extra("2.4.3")
    val retrofitVersion by extra("2.9.0")
    val kotlinVersion by extra("1.7.10")
    val leakcanaryVersion by extra("2.9.1")
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.create("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}