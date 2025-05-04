// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false // Google Services plugin
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" apply false // KSP plugin
    alias(libs.plugins.hilt.android) apply false // Hilt plugin
}

allprojects {
    tasks.withType<JavaCompile> {options.compilerArgs.add("-Xlint:deprecation")}
}