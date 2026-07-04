import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.configure

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

extensions.configure<LibraryExtension> {
    namespace = "com.example.healthconnect.editor"
    compileSdk = 36
    compileSdkExtension = 19

    defaultConfig {
        minSdk = 26

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
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":app:navigation:api"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation3.runtime)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
}