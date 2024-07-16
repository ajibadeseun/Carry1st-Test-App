// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath ("com.squareup:javapoet:1.13.0")
        //classpath("com.diffplug.spotless:spotless-plugin-gradle:6.13.0")
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinPluginSerialization) apply false
    id ("com.google.dagger.hilt.android") version "2.44" apply false
}