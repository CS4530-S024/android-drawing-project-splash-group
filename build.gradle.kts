buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    //enable automatic JSON serialization/deserialization
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"

    //enable KSP processor used by Room
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}