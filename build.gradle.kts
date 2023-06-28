plugins {
    kotlin("jvm") version "1.8.20"
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.byrt"
version = "2.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.reflections:reflections:0.10.2")
    implementation("cloud.commandframework:cloud-paper:1.8.3")
    implementation("cloud.commandframework:cloud-annotations:1.8.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
