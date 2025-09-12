@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.serialization)
//    alias(libs.plugins.androidLibrary)
}

group = "io.github.kotlin"
version = "1.0.0"

kotlin {
//    androidTarget {
//        publishLibraryVariants("release")
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()
    linuxX64()
    jvm("desktop")
//    wasmJs {
//        browser()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.encoding)
                implementation(libs.ktor.serialization.kotlinx.json)

                api(libs.kotlinx.serialization.cbor)
                api("com.fleeksoft.ksoup:ksoup:0.2.5")
                api("ai.koog:koog-agents:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val desktopMain by getting {
            resources.srcDir("py") // files end up at classpath root
        }
    }
}

//android {
//    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//    defaultConfig {
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}