repositories {
    google()
    mavenCentral()
}
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.2")
    implementation(kotlin("gradle-plugin", "1.7.0"))
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.12")
    implementation("org.ajoberstar:grgit:1.1.0")
    implementation("org.ow2.asm:asm-tree:9.1")
    implementation("com.android.tools.build:bundletool:1.6.0")
    compileOnly("com.android.tools.build:aapt2-proto:0.4.0")
}