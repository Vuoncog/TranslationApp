import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.translator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.translator"
        minSdk = 26
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
            isMinifyEnabled = true
            isShrinkResources = true

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
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Icon
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    //Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // CameraX
    val camerax_version = "1.3.0-rc01"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

    //Coil
    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    //Lifecycle viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //ML KIT
    val mlkit_text = "16.0.0"
    implementation ("com.google.mlkit:text-recognition:${mlkit_text}")
    implementation ("com.google.mlkit:text-recognition-chinese:${mlkit_text}")
    implementation ("com.google.mlkit:text-recognition-devanagari:${mlkit_text}")
    implementation ("com.google.mlkit:text-recognition-japanese:${mlkit_text}")
    implementation ("com.google.mlkit:text-recognition-korean:${mlkit_text}")

    implementation ("com.google.mlkit:language-id:17.0.4")
    implementation ("com.google.mlkit:translate:17.0.2")

    //Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.24.11-rc")
}