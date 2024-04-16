plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

buildscript {

    repositories {

        maven { url =uri("https://maven.aliyun.com/repository/central") }
        maven { url =uri("https://maven.aliyun.com/repository/google") }
        maven { url =uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url =uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url =uri("https://maven.aliyun.com/repository/public") }
        mavenLocal()
        google()
    }
    dependencies {
        //这里版本根据自己项目而定

    }
}

android {
    namespace = "com.example.myfirstweatherapp"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.myfirstweatherapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.core3)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android3)
}