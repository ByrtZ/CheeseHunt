plugins {
    kotlin("jvm")
    alias(libs.plugins.paperweight)
}

dependencies {
    paperweight.paperDevBundle( libs.versions.minecraft.map { "$it-R0.1-SNAPSHOT" })
    implementation(project(":common"))
    implementation(project(":cheesehunt"))
    compileOnly("net.luckperms:api:5.4")
}
