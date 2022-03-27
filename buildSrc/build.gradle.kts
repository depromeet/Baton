plugins {
    `kotlin-dsl`
}
repositories {
    gradlePluginPortal()
}

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("src/main/kotlin")
}
