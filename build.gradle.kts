buildscript {
    val kotlin_version by extra("1.8.10")
    val compose_version = "1.4.3"

    dependencies {
        classpath("com.android.tools.build:gradle:3.5.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.4")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

tasks.register("clean",Delete::class.java) {
    delete(rootProject.buildDir)
}
