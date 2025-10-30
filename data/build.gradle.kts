plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")

    id("org.jetbrains.kotlinx.kover")
}

android {
    namespace = "com.alex.yang.weather.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions { jvmTarget = "11" }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Project Dependencies
    implementation(project(":core"))
    implementation(project(":domain"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit with Kotlinx Serialization Converter
    implementation(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)

    implementation(libs.logging.interceptor)

    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.config)

    implementation(libs.androidx.datastore.preferences)

    // Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:<latest>")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("com.google.truth:truth:1.4.5")
    testImplementation(kotlin("test"))
    testImplementation("joda-time:joda-time:2.14.0")
    testImplementation("com.squareup.okhttp3:mockwebserver3:5.2.1")
    testImplementation("org.robolectric:robolectric:4.16")
    testImplementation("androidx.test:core:1.7.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "com.alex.yang.weather.data.di.*"
                )
            }
        }
        total {
            html { onCheck = false }
            xml { onCheck = true }
            verify { rule { minBound(70) } }
        }
    }
}