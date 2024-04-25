plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false

    alias(libs.plugins.navigation.safeargs.kotlin) apply false

    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kapt) apply false
}

tasks.register("clean", Delete::class) {
    delete(getLayout().buildDirectory)
}