plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.squareup.sqldelight")
}

android {
    namespace = "com.example.advanced_qr_scanner"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.advanced_qr_scanner"
        minSdk = 27
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.multidex:multidex:2.0.1")

    // CameraX
    val cameraXversion = "1.2.0-beta01"
    implementation("androidx.camera:camera-core:$cameraXversion")
    implementation("androidx.camera:camera-camera2:$cameraXversion")
    implementation("androidx.camera:camera-lifecycle:$cameraXversion")
    implementation("androidx.camera:camera-view:$cameraXversion")

    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.accompanist:accompanist-permissions:0.25.1")

    val sqlDelightVersion = "1.5.4"
    implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:$sqlDelightVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation("com.google.mlkit:barcode-scanning:17.0.2")

    val datastoreVersion = "1.0.0"
    implementation("androidx.datastore:datastore-core:$datastoreVersion")
    implementation("androidx.datastore:datastore-preferences:$datastoreVersion")

    val hiltVersion = "2.43.2"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
}
