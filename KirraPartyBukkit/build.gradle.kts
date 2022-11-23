val libVersion: String by project
val projectVersion: String by project

plugins {
    java
    id("io.izzel.taboolib") version "1.40"
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
}

group = "net.sakuragame.eternal.kirraparty.bukkit"
version = projectVersion

taboolib {
    description {
        dependencies {
            name("DataManager-Bukkit")
        }
    }
    install("common")
    install("common-5")
    install("module-lang")
    install("module-configuration")
    install("module-chat")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = libVersion
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("net.sakuragame.eternal:KirraCore-Bukkit:1.2.4-SNAPSHOT@jar")
    compileOnly("net.sakuragame.eternal:JustMessage:1.0.2-SNAPSHOT@jar")
    compileOnly("com.taylorswiftcn:UIFactory:1.0.0-SNAPSHOT@jar")
    compileOnly("biz.paluch.redis:lettuce:4.1.1.Final@jar")
    compileOnly("net.sakuragame.eternal:DragonCore:2.4.8-SNAPSHOT@jar")
    @Suppress("VulnerableLibrariesLocal")
    compileOnly("net.sakuragame:datamanager-bukkit-api:2.0.0-SNAPSHOT") {
        isTransitive = true
    }
    compileOnly("ink.ptms.core:v11200:11200")
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