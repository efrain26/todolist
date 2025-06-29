import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kover)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation("com.russhwolf:multiplatform-settings:1.0.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.koin.test)
        }
        
        androidUnitTest.dependencies {
            implementation(libs.junit)
            implementation(libs.mockk)
        }
    }
}

android {
    namespace = "org.efradev.todolist"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.efradev.todolist"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.ui.tooling)
}

// Tareas personalizadas para facilitar el trabajo con code coverage
tasks.register("testWithCoverage") {
    group = "verification"
    description = "Ejecuta tests de Android y genera reportes de coverage"
    dependsOn("testDebugUnitTest", "testReleaseUnitTest", "koverHtmlReport", "koverXmlReport")
}

tasks.register("coverageReport") {
    group = "reporting"
    description = "Genera solo los reportes de coverage sin ejecutar tests"
    dependsOn("koverHtmlReport", "koverXmlReport")
}

tasks.register("testAndroidWithCoverage") {
    group = "verification"
    description = "Ejecuta solo tests de Android y genera reportes de coverage"
    dependsOn("testDebugUnitTest", "koverHtmlReport", "koverXmlReport")
}

// Configuración de Kover para Code Coverage
kover {
    reports {
        total {
            html {
                onCheck = true
            }
            xml {
                onCheck = true
            }
        }
    }
    
    currentProject {
        sources {
            excludedSourceSets.addAll("iosMain", "iosTest")
        }
        
        instrumentation {
            // Excluir clases generadas automáticamente
            excludedClasses.addAll(
                "*.BuildConfig",
                "*.*\$\$serializer.*",
                "*.di.*", // Archivos de inyección de dependencias
                "*.*Test*.*",
                "android.*",
                "androidx.*"
            )
        }
    }
}
