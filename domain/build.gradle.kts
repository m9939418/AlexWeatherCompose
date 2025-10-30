plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")

    id("org.jetbrains.kotlinx.kover")
}

android {
    namespace = "com.alex.yang.weather.domain"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.compose.runtime.annotation)
    testImplementation(libs.junit)
    testImplementation(libs.junit.junit)
    testImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Project Dependencies
    implementation(project(":core"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Joda-Time
    implementation(libs.android.joda)

    // Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("com.google.truth:truth:1.4.5")
    testImplementation(kotlin("test"))
    testImplementation("joda-time:joda-time:2.14.0")
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "com.alex.yang.weather.domain.repository.*",
                    "com.alex.yang.weather.domain.di.*"
                )
            }
        }
        total {
            html { onCheck = false }
            xml { onCheck = true }
            verify { rule { minBound(80) } }
        }
    }
}