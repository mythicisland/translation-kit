plugins {
    id("i18n.base-conventions")
    id("dev.xxjanisxx.kraken")
}

maven {
    artifactId = project.name
    url = "https://github.com/mythicisland/translation-kit"

    licenses {
        license {
            name = "Apache 2.0"
            url = "https://www.apache.org/licenses/"
        }
    }

    developers {
        developer {
            name = "Janis"
        }
    }

    scm {
        url = "https://github.com/mythicisland/translation-kit"
        connection = "scm:git:https://github.com/mythicisland/translation-kit.git"
        developerConnection = "scm:git:ssh://git@github.com/mythicisland/translation-kit.git"
    }

    ciManagement {
        system = "GitHub Actions"
        url = "https://github.com/mythicisland/translation-kit/actions"
    }

    repositories {
        register("releases") {
            url.set("https://repo.mythicisland.club/releases")
            username.set(System.getenv("MYTHICISLAND_USER"))
            password.set(System.getenv("MYTHICISLAND_PASSWORD"))
        }
        register("snapshots") {
            url.set("https://repo.mythicisland.club/snapshots")
            username.set(System.getenv("MYTHICISLAND_USER"))
            password.set(System.getenv("MYTHICISLAND_PASSWORD"))
        }
    }
}
