plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.facility_bookuitm"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.facility_bookuitm"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Retrofit https://square.github.io/retrofit/ - latest vesion https://github.com/square/retrofit.
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson -> json data to java or kotlin format
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}