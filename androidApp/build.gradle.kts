plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

android {
    namespace = "icu.fyi.webmon.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "icu.fyi.webmon.android"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.1")
    val workVersion = "2.7.1"
    val androidxVer = "1.3.1"
    implementation(project(":shared"))
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    implementation("androidx.work:work-gcm:$workVersion")
    implementation ("androidx.work:work-multiprocess:$workVersion")
    implementation("androidx.compose.ui:ui:$androidxVer")
    implementation("androidx.compose.ui:ui-tooling:$androidxVer")
    implementation("androidx.compose.ui:ui-tooling-preview:$androidxVer")
    implementation("androidx.compose.foundation:foundation:$androidxVer")
    implementation("androidx.compose.material:material:$androidxVer")
    implementation("androidx.activity:activity-compose:1.6.1")

    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")
}