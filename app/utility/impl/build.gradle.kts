import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

extensions.configure<LibraryExtension> {
    namespace = "com.example.healthconnect.utilty.impl"
    compileSdk = 36
    compileSdkExtension = 19

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {

    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //internal module dependencies
    implementation(project(":app:permissions:api"))
    implementation(project(":app:utility:api"))
    implementation(project(":app:editor:api"))
    implementation(project(":app:navigation:api"))
    implementation(project(":app:components:api"))
    implementation(project(":app:models:api"))

    // Use to implement health connects
    implementation(libs.androidx.connect.client)

    //compose
    implementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //coroutines viewmodel scope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //compose navigation v3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.material3.adaptive.navigation3)
}
