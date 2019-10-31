plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(28)
    buildToolsVersion("28.0.3")

    defaultConfig {
        applicationId = "com.example.mdsample"
        minSdkVersion(21)
        targetSdkVersion(26)
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.constraintlayout)
    implementation(Libs.joda_time)

    implementation(Libs.paging)
    implementation(Libs.core_ktx)
    implementation(Libs.kotlin_stdlib_jdk7)
}
