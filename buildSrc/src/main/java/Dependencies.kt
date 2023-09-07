object Versions {
    const val supportVersion = "1.1.0"
    const val materialVersion = "1.0.0"
    const val constraintVersion = "1.1.3"
    const val jodaTimeVersion = "2.9.9"
    const val pagingVersion = "2.1.0"
    const val ktsVersion = "1.1.0"
    // Something went wrong while checking for version compatibility between the Compose Compiler and the Kotlin Compiler.  It is possible that the versions are incompatible.  Please verify your kotlin version  and consult the Compose-Kotlin compatibility map located at https://developer.android.com/jetpack/androidx/releases/compose-kotlin
    const val kgp_version = "1.8.0"
    const val kotlin_verson = "1.7.0"
    const val compose_compiler_extension = "1.2.0"
}

object Libs {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.supportVersion}"
    const val material = "com.google.android.material:material:${Versions.materialVersion}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintVersion}"
    const val joda_time = "joda-time:joda-time:${Versions.jodaTimeVersion}"
    const val paging = "androidx.paging:paging-common:${Versions.pagingVersion}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.ktsVersion}"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_verson}"

    const val android_activity_ktx = "androidx.activity:activity-ktx:1.6.1"
    const val android_activity = "androidx.activity:activity:1.6.1"

    const val android_fragment = "androidx.fragment:fragment-ktx:1.5.7"

    const val compose_navi = "androidx.navigation:navigation-compose:2.5.3"
}
