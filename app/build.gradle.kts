plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
    id("org.sonarqube") version "5.1.0.4882"
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

sonarqube  {
    properties {
        property("sonar.projectKey", "Atuyto-Fac-groupe_The-Rift-Mobile_53065491-1184-4c29-90c8-48ee261aa76f")
        property("sonar.projectName", "The-Rift-Mobile")
        property("sonar.organization", "Atuyto-Fac-groupe")
        property("sonar.sources", "src/main/java/main")
        property("sonar.host.url", "https://condor-funky-completely.ngrok-free.app")
        property("sonar.login", "sqp_adc6e6441eb48fbf9894a71a1e42d2a38b80d234")
        property("sonar.java.binaries", file("build/classes/java/debug"))
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
    debugImplementation(libs.objectbox.android.objectbrowser)
    implementation(libs.zxing.android.embedded)
    implementation(libs.core)

}

apply(plugin = "io.objectbox")
