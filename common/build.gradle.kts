plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
    kotlin("jvm")
}

dependencies {
    api("net.kyori:adventure-extra-kotlin:4.16.0")
    api(libs.bundles.cloud)
    paperweight.paperDevBundle( libs.versions.minecraft.map { "$it-R0.1-SNAPSHOT" })
}
