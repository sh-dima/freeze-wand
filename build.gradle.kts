import com.undefinedcreations.nova.ServerType

plugins {
    alias(libs.plugins.kotlin)

    alias(libs.plugins.paper)
    alias(libs.plugins.nova)

    alias(libs.plugins.yml)
}

repositories {
    mavenCentral()
}

dependencies {
    library(kotlin("stdlib"))
    library(libs.commands)

    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "io.gitlab.shdima"

version = ProcessBuilder("git", "describe", "--tags", "--always", "--dirty")
    .directory(project.projectDir)
    .start()
    .inputStream
    .bufferedReader()
    .readText()
    .trim()

tasks {
    withType<AbstractArchiveTask> {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true

        filePermissions {
            user.read = true
            user.write = true
            user.execute = false

            group.read = true
            group.write = false
            group.execute = false

            other.read = true
            other.write = false
            other.execute = false
        }

        dirPermissions {
            user.read = true
            user.write = true
            user.execute = true

            group.read = true
            group.write = false
            group.execute = true

            other.read = false
            other.write = false
            other.execute = true
        }
    }

    runServer {
        val version = project.findProperty("minecraft.version") as? String ?: "1.21"
        val serverSoftware = (project.findProperty("minecraft.software") as? String ?: "papermc")
        val metricsEnabled = (project.findProperty("metrics") as? String)?.toBoolean() == true

        val run = File(project.projectDir, "run/${serverSoftware}/${version}")

        serverFolder(run)
        serverType(ServerType.valueOf(serverSoftware.uppercase()))

        minecraftVersion(version)

        acceptMojangEula()

        doFirst {
            val metricsConfig = run.resolve("plugins/bStats/config.yml")
            metricsConfig.parentFile.mkdirs()
            metricsConfig.writeText("""
                enabled: $metricsEnabled
                logFailedRequests: true

            """.trimIndent())
        }
    }
}

listOf(
    tasks.jar,
    tasks.kotlinSourcesJar,
).forEach {
    it {
        from("README.md")
        from("LICENSE")
    }
}

bukkit {
    name = "FreezeWand"
    description = "A simple freeze wand plugin, which allows admins and staff to freeze players by right-clicking them with a custom stick item."

    main = "$group.${project.name}.FreezeWand"
    apiVersion = "1.20.6"
    version = project.version.toString()

    authors = listOf(
        "Esoteric Enderman"
    )

    website = "https://gitlab.com/-/p/69682226"
}
