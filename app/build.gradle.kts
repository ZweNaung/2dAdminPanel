plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)   // ✅ ဒီလို
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.twodamin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.twodamin"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "messages/JavaOptionBundle.properties"
            excludes += "kotlin/reflect/reflect.kotlin_builtins"
        }
    }
}
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.graphics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Navigation
    val nav_version = "2.7.7" // Replace with the latest stable version
    implementation("androidx.navigation:navigation-compose:$nav_version")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // KotlinX Serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // KotlinX Serialization JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // Kotlin core libs for serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.1")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //okhttp-logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    ksp("androidx.room:room-compiler:2.8.4")

}

configurations {
    all {
        exclude(group = "com.google.auto.value", module = "auto-value")
    }
}