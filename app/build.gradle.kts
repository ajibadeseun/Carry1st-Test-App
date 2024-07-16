plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    alias(libs.plugins.kotlinParcelize)
    id("kotlin-parcelize")

//    alias(libs.plugins.kotlinKapt)
//    id("dagger.hilt.android.plugin")
    id ("com.google.dagger.hilt.android")
//    alias(libs.plugins.hiltAndroidPlugin)

    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.kapt")

//    alias(libs.plugins.kotlinPluginSerialization)
}

android {
    namespace = "com.carry1st_shop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.carry1st_shop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlin.serialization)
    implementation(libs.coil.kt)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.reflect)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)
    implementation (libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    // Annotation processor
    kapt (libs.androidx.lifecycle.compiler)
    // Lifecycle utilities for Compose
    implementation (libs.androidx.lifecycle.runtime.compose)
    //timber
    implementation(libs.timber.log)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
hilt {
    enableAggregatingTask = true
}