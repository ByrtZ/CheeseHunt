plugins {
    kotlin("jvm") version "1.6.21"
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.byrt"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.reflections:reflections:0.10.2")
    implementation("cloud.commandframework:cloud-paper:1.7.0")
    implementation("cloud.commandframework:cloud-annotations:1.7.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}