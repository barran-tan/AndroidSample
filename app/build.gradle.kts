plugins {
    id("com.android.application")
    kotlin("android")
//    id("test.thread")
//    id("test.totransform")
//    id("test.kttransform")
    id("test.resplugin")
    id("opt.dataclass")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.2"

    defaultConfig {
        applicationId = "com.barran.androidsample"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // 'compileDebugJavaWithJavac' task (current target is 11) and 'compileDebugKotlin' task (current target is 1.8) jvm target compatibility should be set to the same Java version.
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

//    lintOptions {
//        disable("IconDuplicatesConfig", "IconDuplicates", "GifUsage", "IconColors", "IconDensities", "IconDipSize", "IconExpectedSize", "IconExtension", "IconLauncherShape", "IconLocation", "IconMissingDensityFolder", "IconMixedNinePatch", "IconNoDpi", "NotificationIconCompatibility", "ConvertToWebp")
//    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_compiler_extension
    }
    dynamicFeatures.add(":dynamicfeature")
    namespace = "com.barran.sample"
}

dependencies {
    testImplementation("junit:junit:4.12")
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.constraintlayout)
    implementation(Libs.joda_time)

    implementation(Libs.paging)
    implementation(Libs.core_ktx)
    implementation(Libs.kotlin_stdlib)
    implementation("com.google.code.gson:gson:2.8.9")

    implementation(Libs.android_activity)
    // 低版本fragment存在针对requestCode的校验，但是result api生成的code是随机的，超出范围报错
    implementation(Libs.android_fragment)


    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation(project(":nativelib"))
    implementation(project(":mylibrary"))

    // 测试new Thread插件
//    implementation("com.appsflyer:af-android-sdk:6.5.4")

    // region compose

    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")
    // or Material Design 2
//    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
//    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
//    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
//    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
//    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
//    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
//    implementation("androidx.compose.runtime:runtime-rxjava2")

    implementation(Libs.compose_navi)

    // endregion

    implementation("com.github.tiann:FreeReflection:3.1.0")

    compileOnly(files("libs/hidden-api.jar"))

    implementation("com.headius:unsafe-mock:8.92.1")
}
