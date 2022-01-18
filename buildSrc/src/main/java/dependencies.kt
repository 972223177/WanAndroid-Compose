object Libs {
    private const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"


    const val junit = "junit:junit:4.13"

    const val material = "com.google.android.material:material:1.3.0"

    const val coil = "io.coil-kt:coil-compose:1.4.0"

    const val mmkv = "com.tencent:mmkv-static:1.2.11"

    const val x5Web = "com.tencent.tbs.tbssdk:sdk:43993"

    val plugins =
        listOf(
            androidGradlePlugin,
            AndroidX.Hilt.plugin,
            Kotlin.gradlePlugin,
            Kotlin.serializationPlugin
        )

    object Accompanist {
        const val version = "0.22.0-rc"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val placeholder = "com.google.accompanist:accompanist-placeholder:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val permission = "com.google.accompanist:accompanist-permissions:$version"
        const val refresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val systemUiController =
            "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val flowlayout = "com.google.accompanist:accompanist-flowlayout:$version"
        const val navigationAnim =
            "com.google.accompanist:accompanist-navigation-animation:$version"

        val libs = listOf(
            insets, placeholder, pager, permission, refresh, systemUiController, flowlayout,
            navigationAnim
        )
    }


    object Kotlin {
        const val version = "1.5.31"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-alpha02"
        const val constraintCompose =
            "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.1"
        }

        object Compose {
            const val version = "1.0.5"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended =
                "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"
            val libs = listOf(
                foundation, layout, material, materialIconsExtended, runtime,
                runtimeLivedata, tooling, uiUtil, viewBinding
            )

            val tests = listOf(test, uiTest, uiTestManifest)
        }

        object Navigation {
            private const val version = "2.4.0-alpha10"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val compose = "androidx.navigation:navigation-compose:$version"
            val libs = listOf(fragment, uiKtx, compose)
        }

        object Hilt {
            private const val version = "2.38.1"
            private const val composeVersion = "1.0.0-alpha03"
            const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$composeVersion"
            const val navigation = "androidx.hilt:hilt-navigation-compose:$composeVersion"
            const val kaptDep2 = "androidx.hilt:hilt-compiler:$composeVersion"
            const val plugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
            const val android = "com.google.dagger:hilt-android:$version"
            const val kaptDep = "com.google.dagger:hilt-android-compiler:$version"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val benchmark = "androidx.benchmark:benchmark-junit4:1.0.0"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"

            val androidTests = listOf(core, rules, espressoCore, benchmark)
        }

        object Lifecycle {
            private const val version = "2.4.0-rc01"

            //            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
            val libs = listOf(livedata, viewmodel, viewModelCompose)
        }

        object Paging {
            private const val version = "3.0.1"
            val runtime = "androidx.paging:paging-runtime:$version"
            val ompose = "androidx.paging:paging-compose:1.0.0-alpha14"
            val libs = listOf(runtime, ompose)
        }
    }

    object Http {
        const val okhttpVersion = "4.9.2"
        const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
        const val logger = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
    }


}
