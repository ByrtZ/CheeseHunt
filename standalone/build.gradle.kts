plugins {
    alias(libs.plugins.paperweight)
    kotlin("jvm")
}

dependencies {
    paperweight.paperDevBundle( libs.versions.minecraft.map { "$it-R0.1-SNAPSHOT" })
    implementation(project(":common"))
    implementation(project(":cheesehunt"))
}
