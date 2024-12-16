plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "ml.volder"
version = System.getenv().getOrDefault("VERSION", "1.0.0")

labyMod {
    defaultPackageName = "ml.volder"

    addonInfo {
        namespace = "sa-transporter"
        displayName = "Transporter Addon"
        author = "Mads_Gamer_DK"
        description = "Et addon med redskaber der forbedre din spil oplevelse på SA."
        minecraftVersion = "*"
        version = getVersion().toString()
    }

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }

                val file = file("./game-runner/src/$sourceSetName/resources/sa-transporter-$versionId.accesswidener");
                accessWidener.set(file)
            }
        }
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}