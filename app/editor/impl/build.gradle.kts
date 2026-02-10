plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.healthconnect.editor.impl"
    compileSdk = 36

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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":app:components:api"))
    implementation(project(":app:utility:api"))
    implementation(project(":app:editor:api"))
    implementation(project(":app:navigation:api"))

    // reflection
    implementation(kotlin("reflect"))

    //compose
    implementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //coroutines viewmodel scope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Use to implement health connects
    implementation(libs.androidx.connect.client)

    //compose navigation v3
    implementation(libs.androidx.navigation3.runtime)
}