import com.depromeet.baton.App
import com.depromeet.baton.Dep
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import java.io.FileInputStream
import java.util.Properties


plugins {
    id ("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    id ("dagger.hilt.android.plugin")

}


android {
    compileSdk = App.compileSdk

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))


    defaultConfig {

        minSdk = App.minSdk
        targetSdk = App.targetSdk

        buildConfigField( "String", "NAVER_SDK_CLIENT_KEY", keystoreProperties["NAVER_SDK_CLIENT_KEY"].toString())
        manifestPlaceholders ["NAVER_SDK_CLIENT_KEY"]= keystoreProperties["NAVER_SDK_CLIENT_KEY"].toString()

        buildConfigField ("String", "NAVER_API_CLIENT_ID_KEY", keystoreProperties["NAVER_API_CLIENT_ID_KEY"].toString())
        buildConfigField ("String", "NAVER_API_CLIENT_SECRET_KEY", keystoreProperties["NAVER_API_CLIENT_SECRET_KEY"].toString())
        buildConfigField ("String", "KAKAO_REST_API_KEY", keystoreProperties["KAKAO_REST_API_KEY"].toString())
        buildConfigField ("String", "ADDRESS_API_KEY", keystoreProperties["ADDRESS_API_KEY"].toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_11
        targetCompatibility =  JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding=true
        dataBinding=true
    }
}



dependencies {

    implementation(project(":bds"))
    implementation(Dep.Android.core)
    implementation(Dep.Android.appcompat)
    implementation(Dep.Android.material)
    implementation(Dep.Android.constraintLayout)
    implementation(Dep.Android.fragment)

    implementation(Dep.Lifecycle.viewModel)
    implementation(Dep.Lifecycle.runtime)
    implementation(Dep.Lifecycle.savedState)
    implementation(Dep.Lifecycle.livedata)

    implementation(Dep.Dagger.hiltAndroid)
    kapt(Dep.Dagger.hiltCompiler)

    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)
    implementation(Dep.OkHttp.mockWebServer)
    implementation(Dep.Retrofit.core)
    implementation(Dep.Retrofit.converterMoshi)
    implementation(Dep.Moshi.core)
    implementation(Dep.Moshi.kotlin)

    implementation(Dep.timber)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.AndroidTest.core)
    androidTestImplementation(Dep.AndroidTest.rules)
    androidTestImplementation(Dep.AndroidTest.runner)
    androidTestImplementation(Dep.AndroidTest.junitExt)

    implementation(Dep.Map.naver_sdk)
    implementation(Dep.Map.gms_location)

    implementation(Dep.XmlParser.tickaroo)
    implementation(Dep.XmlParser.tickaroo_core)
    implementation(Dep.XmlParser.tickaroo_retrofit)
    kapt(Dep.XmlParser.tickaroo_processor)

    implementation(Dep.rxjava.rxjava2)
    implementation(Dep.rxjava.rxjava2_android)
    implementation(Dep.rxjava.rxjava2_adpater)
    implementation(Dep.Retrofit.converterGson)

}