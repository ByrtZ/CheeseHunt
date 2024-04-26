plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.2.4"
}

group = "dev.byrt"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.reflections:reflections:0.10.2")
    implementation("cloud.commandframework:cloud-paper:1.8.4")
    implementation("cloud.commandframework:cloud-annotations:1.8.4")
    implementation("org.incendo.interfaces:interfaces-paper:1.0.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}

tasks.runServer {
    minecraftVersion("1.20.4")
}
