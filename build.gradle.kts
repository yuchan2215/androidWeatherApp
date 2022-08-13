// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val nav_version by extra("2.5.0")
    val roomVersion by extra("2.4.3")
    val retrofit_version by extra("2.9.0")
    val kotlin_version by extra("1.7.10")
    val leakcanary_version by extra("2.9.1")
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
    }
}
plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

task clean(type: Delete) {
    delete(rootProject.buildDir)
}