buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.14") // Ažurirano
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
