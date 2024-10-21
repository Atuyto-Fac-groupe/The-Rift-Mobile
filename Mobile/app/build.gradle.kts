plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
//    id("io.objectbox")
}
android {
    namespace = "com.example.therift"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.therift"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            buildConfigField( "boolean", "DEBUG_MODE", "true")
        }
        release {
            buildConfigField( "boolean", "DEBUG_MODE", "false")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    releaseImplementation(libs.objectbox.java)
    debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.0.3")
    implementation(libs.zxing.android.embedded)
    implementation(libs.core)
}

apply(plugin = "io.objectbox")
