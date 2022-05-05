package com.depromeet.baton

object Dep {

    object Plugins {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.4"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
        const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:6.2.0"
        const val googleService = "com.google.gms:google-services:4.3.10"
    }

    object Kotlin {
        const val version = "1.6.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object Coroutine {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Android {
        const val core = "androidx.core:core-ktx:1.7.0"
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val material = "com.google.android.material:material:1.5.0"

        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.3"
        const val fragment = "androidx.fragment:fragment-ktx:1.4.1"
        const val flexbox ="com.google.android.flexbox:flexbox:3.0.0"

        const val navigationCompose = "androidx.navigation:navigation-compose:2.4.0"
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Lifecycle {
        private const val version = "2.4.1"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        const val savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
        const val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:2.5.0-alpha05"
    }

    object Room {
        private const val version = "2.4.1"
        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"

        // test
        const val test = "androidx.room:room-testing:$version"
    }

    object Dagger {
        const val version = "2.40.5"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
        const val dagger = "com.google.dagger:dagger:$version"
        const val hiltTest = "com.google.dagger:hilt-android-testing:$version"
    }

    object Compose {
        const val version = "1.1.0-rc03"

        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val material = "androidx.compose.material:material:$version"
        const val materialIcon = "androidx.compose.material:material-icons-core:$version"
        const val materialIconsExtended =
            "androidx.compose.material:material-icons-extended:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val navigation = "androidx.navigation:navigation-compose:2.5.0-alpha01"
        const val activity = "androidx.activity:activity-compose:1.4.0"
        const val mdc3 = "androidx.compose.material3:material3:1.0.0-alpha02"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    }

    object Accompanist {
        private const val version = "0.24.2-alpha"
        const val inset = "com.google.accompanist:accompanist-insets:$version"
        const val insetUi = "com.google.accompanist:accompanist-insets-ui:$version"
        const val systemUi = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val indicator = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val mockk = "io.mockk:mockk:1.12.2"
    }

    object AndroidTest {
        const val core = "androidx.test:core:1.4.0"
        const val rules = "androidx.test:rules:1.4.0"
        const val runner = "androidx.test:runner:1.4.0"
        const val junitExt = "androidx.test.ext:junit:1.1.3"
        const val compose = "androidx.compose.ui:ui-test:${Compose.version}"
        const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Compose.version}"
        const val composeManifest = "androidx.compose.ui:ui-test-manifest:${Compose.version}"
        const val mockk = "io.mockk:mockk-android:1.12.2"
    }

    object OkHttp {
        private const val version = "4.9.2"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$version"
        const val mock = "com.squareup.retrofit2:retrofit-mock:$version"
        const val converterGson = "com.squareup.retrofit2:converter-gson:2.9.0"
    }

    object Moshi {
        private const val version = "1.12.0"
        const val core = "com.squareup.moshi:moshi:$version"
        const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:29.0.4"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx:23.0.0"
    }

    object Map{
        const val naver_sdk = "com.naver.maps:map-sdk:3.14.0"
        const val gms_location="com.google.android.gms:play-services-location:17.1.0"
    }

    object XmlParser{
        const val tickaroo = "com.tickaroo.tikxml:annotation:0.8.13"
        const val tickaroo_core = "com.tickaroo.tikxml:core:0.8.13"
        const val tickaroo_retrofit = "com.tickaroo.tikxml:retrofit-converter:0.8.13"
        const val tickaroo_processor= "com.tickaroo.tikxml:processor:0.8.13"
    }

    object rxjava{
        const val rxjava2= "io.reactivex.rxjava2:rxjava:2.2.2"
        const val rxjava2_android = "io.reactivex.rxjava2:rxandroid:2.1.0"
        const val rxjava2_adpater ="com.squareup.retrofit2:adapter-rxjava3:2.9.0"
    }


    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val snapper = "dev.chrisbanes.snapper:snapper:<version>"
}
