plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    id("test.thread")
}

android {
    compileSdk = 33
    buildToolsVersion = "33.0.2"

    defaultConfig {
        applicationId = "com.barran.androidsample"
        minSdk = 23
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

//    lintOptions {
//        disable("IconDuplicatesConfig", "IconDuplicates", "GifUsage", "IconColors", "IconDensities", "IconDipSize", "IconExpectedSize", "IconExtension", "IconLauncherShape", "IconLocation", "IconMissingDensityFolder", "IconMixedNinePatch", "IconNoDpi", "NotificationIconCompatibility", "ConvertToWebp")
//    }

    viewBinding {
        isEnabled = true
    }
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

    // 测试new Thread插件
//    implementation("com.appsflyer:af-android-sdk:6.5.4")
}
