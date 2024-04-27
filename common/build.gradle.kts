plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
    kotlin("jvm")
}

dependencies {
    paperweight.paperDevBundle( libs.versions.minecraft.map { "$it-R0.1-SNAPSHOT" })
}
