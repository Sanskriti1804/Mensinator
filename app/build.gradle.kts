plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.mensinator.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mensinator.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 24
        versionName = "2.0"
        manifestPlaceholders.putAll(
            mapOf(
                "auth0Domain" to "@string/com_auth0_domain",
                "auth0Scheme" to "demo"
            )
        )


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
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
    }
    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
    lint {
        sarifReport = true
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        metricsDestination = layout.buildDirectory.dir("compose_compiler")
    }
    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.material3.adaptive.layout)
    implementation(libs.androidx.material3.window.size.classes)
    implementation(libs.androidx.window)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.ktx)


    implementation(libs.kizitonwose.calendar.compose)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation(platform(libs.koin.bom))
    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.pdfbox.android)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.runtime.livedata)
//    implementation("com.auth0.android:auth0:2.11.0")
    implementation("com.auth0.android:auth0:2.0.0")


//    implementation(libs.androidx.biometric.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //Firebase
//    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
//    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")
//    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")

    // To be used to profile performance. Don't include in release builds
    // implementation("androidx.compose.runtime:runtime-tracing")
}
