import org.gradle.kotlin.dsl.maven

plugins {
	`java-library`
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
	mavenLocal()
	mavenCentral()
	maven("https://maven.enginehub.org/repo/")
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	maven("https://repo.revengenetwork.es/repository/libs/")
	maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
	maven("https://ci.nametagedit.com/plugin/repository/everything/")
	maven("https://jitpack.io/")
}
