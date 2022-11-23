val libVersion: String by project
val projectVersion: String by project

plugins {
    java
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
}

group = "net.sakuragame.eternal.kirraparty.bungee"
version = projectVersion

taboolib {
    description {
        dependencies {
            name("DataManager-Bungee")
        }
    }
    install("common")
    install("common-5")
    install("module-chat")
    install("platform-bungee")
    classifier = null
    version = libVersion
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.sakuragame:datamanager-bungee-api:2.0.0-SNAPSHOT") {
        isTransitive = true
    }
    compileOnly("net.md_5.bungee:BungeeCord:1@jar")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}