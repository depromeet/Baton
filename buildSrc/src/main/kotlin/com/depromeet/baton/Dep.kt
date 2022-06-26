package com.depromeet.baton

object Dep {

    object Plugins {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.4"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
        const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:6.2.0"
        const val googleService = "com.google.gms:google-services:4.3.10"
        const val firebaseCrashlytics="com.google.firebase:firebase-crashlytics-gradle:2.2.0"
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
        const val flexbox = "com.google.android.flexbox:flexbox:3.0.0"

        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
        const val splashscreen = "androidx.core:core-splashscreen:1.0.0-beta02"
        const val webkit = "androidx.webkit:webkit:1.4.0"
    }

    object Navigation {
        private const val version = "2.4.2"

        const val fragmentKtx ="androidx.navigation:navigation-fragment-ktx:$version"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
        const val testing = "androidx.navigation:navigation-testing:$version"
        const val navigationCompose = "androidx.navigation:navigation-compose:$version"

        const val safeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
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
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx:23.0.0"
    }


    const val keyboardListener = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.3.0"
    const val naver_sdk = "com.naver.maps:map-sdk:3.14.0"

    object Map {
        const val naver_sdk = "com.naver.maps:map-sdk:3.14.0"
        const val gms_location = "com.google.android.gms:play-services-location:17.1.0"
    }

    object XmlParser {
        const val tickaroo = "com.tickaroo.tikxml:annotation:0.8.13"
        const val tickaroo_core = "com.tickaroo.tikxml:core:0.8.13"
        const val tickaroo_retrofit = "com.tickaroo.tikxml:retrofit-converter:0.8.13"
        const val tickaroo_processor = "com.tickaroo.tikxml:processor:0.8.13"
    }

    object rxjava {
        const val rxjava2 = "io.reactivex.rxjava2:rxjava:2.2.2"
        const val rxjava2_android = "io.reactivex.rxjava2:rxandroid:2.1.0"
        const val rxjava2_adpater = "com.squareup.retrofit2:adapter-rxjava3:2.9.0"
    }

    object CustomLibrary{
        const val rangeSeekbar ="com.github.Jay-Goo:RangeSeekBar:v3.0.0"
        const val keyboardListener ="net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.3.0"
        const val lottie ="com.airbnb.android:lottie:3.4.0"
    }

    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val snapper = "dev.chrisbanes.snapper:snapper:<version>"
    const val ballon = "com.github.skydoves:balloon:1.4.5"
    const val glide = "com.github.bumptech.glide:glide:4.13.0"
    const val glide_compiler = "com.github.bumptech.glide:compiler:4.13.0"
    const val imagepicker = "com.github.nguyenhoanglam:ImagePicker:1.5.2"
    const val stickyScrollView="com.github.amarjain07:StickyScrollView:1.0.3"
    const val circleimageview=  "de.hdodenhof:circleimageview:3.1.0"

    // https://developers.kakao.com/docs/latest/ko/getting-started/sdk-android#select-module
    object Kakao {
        private const val version = "2.10.0"

        const val user = "com.kakao.sdk:v2-user:$version"

        const val maven = "https://devrepo.kakao.com/nexus/content/groups/public/"
    }

    // https://developer.android.com/topic/security/data#include-library
    object Security {
        const val core = "androidx.security:security-crypto:1.0.0"
    }

    object Kotest {
        private const val version = "5.3.0"

        const val junitRunner = "io.kotest:kotest-runner-junit5:$version"
        const val assertionsCore = "io.kotest:kotest-assertions-core:$version"
        const val property = "io.kotest:kotest-property:$version"
    }


    object CashApp {
        // https://github.com/cashapp/turbine
        const val turbine = "app.cash.turbine:turbine:0.8.0"
    }
}
