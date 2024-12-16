plugins {
    id("com.android.application")
    //id("org.jetbrains.kotlin.android") version "1.9.0"
    id("org.sonarqube") version "5.1.0.4882"
    id("io.objectbox")
    id("jacoco")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

//    kotlinOptions {
//        jvmTarget = "1.8"
//    }



}

sonarqube  {
    properties {
        property("sonar.projectKey", "Atuyto-Fac-groupe_The-Rift-Mobile_53065491-1184-4c29-90c8-48ee261aa76f")
        property("sonar.projectName", "The-Rift-Mobile")
        property("sonar.organization", "Atuyto-Fac-groupe")
        property("sonar.sources", "src/main/java/main")
        property("sonar.host.url", "https://condor-funky-completely.ngrok-free.app")
        property("sonar.token", "squ_69f986dd2782786dec9596bfd09b1de4c9dd6a76")
        property("sonar.java.binaries", file("build/intermediates/classes/debug"))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport") // Générez le rapport après les tests
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest") // Exécutez les tests avant de générer le rapport

    reports {
        xml.required.set(true) // Fichier XML pour SonarQube
        html.required.set(true) // Rapport HTML pour visualisation locale
        csv.required.set(false)
    }

    // Définir les sources et les classes pour le rapport
    val fileFilter = listOf("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*")
    val debugTree = fileTree(mapOf("dir" to "$buildDir/tmp/kotlin-classes/debug", "excludes" to fileFilter))
    val mainSrc = "$projectDir/src/main/java"

    sourceDirectories.setFrom(files(listOf(mainSrc)))
    classDirectories.setFrom(files(listOf(debugTree)))
    executionData.setFrom(fileTree(mapOf("dir" to "$buildDir", "includes" to listOf("**/*.exec", "**/*.ec"))))
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    //releaseImplementation(libs.objectbox.java)
    //debugImplementation(libs.objectbox.android.objectbrowser)
    implementation(libs.zxing.android.embedded)
    implementation(libs.core)

}

//apply(plugin = "io.objectbox")
