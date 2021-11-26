plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.ly.wanandroid"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
        kotlinCompilerVersion = Libs.Kotlin.version
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Kotlin.serialization)
    implementation(Libs.Http.retrofit)
    implementation(Libs.Http.logger)
    implementation(Libs.mmkv)

    implementation(Libs.AndroidX.Activity.activityCompose)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.splashScreen)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.coil)
    implementation(Libs.x5Web)
    for (lib in Libs.Accompanist.libs) {
        implementation(lib)
    }

    for (lib in Libs.AndroidX.Paging.libs) {
        implementation(lib)
    }

    implementation(Libs.material)
    for (lib in Libs.AndroidX.Compose.libs) {
        implementation(lib)
    }
    for (lib in Libs.AndroidX.Navigation.libs) {
        implementation(lib)
    }

    for (lib in Libs.AndroidX.Lifecycle.libs) {
        implementation(lib)
    }
    testImplementation(Libs.AndroidX.Test.Ext.junit)
    debugImplementation(Libs.AndroidX.Compose.tooling)

    for (lib in Libs.AndroidX.Compose.tests) {
        androidTestImplementation(lib)
    }

    androidTestImplementation(Libs.junit)
    for (lib in Libs.AndroidX.Test.androidTests) {
        androidTestImplementation(lib)
    }

}