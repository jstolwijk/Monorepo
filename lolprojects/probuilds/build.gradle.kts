import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import org.gradle.kotlin.dsl.kotlin

val kotlinVersion = "1.3.21"
val ktorVersion = "1.1.3"

group = "org.jessestolwijk.probuilds"
version = "1.0-SNAPSHOT"

plugins {
    java
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"

    compile(kotlin("stdlib-jdk8"))

    compile(ktor("server-netty"))
    compile(ktor("jackson"))

    compile(ktor("client-apache"))
    compile(ktor("client-jackson"))
    compile(ktor("client-json"))
    compile(ktor("client-logging"))

    compile(project(":riot-api-model"))
    compile(project(":ddragon-model"))

    implementation("org.slf4j:slf4j-simple:1.6.1")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")
    implementation("io.github.microutils:kotlin-logging:1.6.22")

    testCompile("junit:junit:4.12")
    testCompile(ktor("server-test-host"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
