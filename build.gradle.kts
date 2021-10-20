// Top-level build file where you can add configuration options common to all sub-projects/modules.
//extra["compose_version"] = "1.0.1"
//val compose_version: String by extra
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Libs.androidGradlePlugin)
        classpath(Libs.Kotlin.gradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}