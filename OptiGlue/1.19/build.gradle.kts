plugins {
    id("fabric-loom")
    kotlin("jvm")
    id("net.kyori.blossom")
    id("maven-publish")
}

base { archivesName.set(project.extra["archives_base_name"] as String) }

version = "${project.extra["mod_version"]}-mc.${project.extra["minecraft_version"]}"
group = project.extra["maven_group"] as String

repositories {}

dependencies {
    minecraft("com.mojang", "minecraft", project.extra["minecraft_version"] as String)
    mappings("net.fabricmc", "yarn", project.extra["yarn_mappings"] as String, null, "v2")
    modImplementation(fabricApi.module("fabric-resource-loader-v0", project.extra["fabric_version"] as String))
    modImplementation(
        "net.fabricmc", "fabric-language-kotlin", project.extra["fabric_language_kotlin_version"] as String
    )

    implementation(project(":OptiGUI", configuration = "namedElements"))
}

blossom.replaceToken("@mod_version@", version)

tasks {
    val javaVersion = JavaVersion.toVersion((project.extra["java_version"] as String).toInt())
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions { jvmTarget = javaVersion.toString() } }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } } }
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "version" to version,
                    "fabric_language_kotlin" to project.extra["fabric_language_kotlin_version"] as String,
                    "java" to project.extra["java_version"] as String
                )
            )
        }
        filesMatching("*.mixins.json") { expand(mutableMapOf("java" to project.extra["java_version"] as String)) }
    }
    java {
        toolchain { languageVersion.set(JavaLanguageVersion.of(javaVersion.toString())) }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = base.archivesName.get()
            version = project.version.toString()

            from(components["java"])
        }
    }
}
