plugins {
	id("project.common-conventions")
	alias(libs.plugins.shadow)
  alias(libs.plugins.pluginyml)
  alias(libs.plugins.blossom)
}

dependencies {
  api(project(":api"))
  
	compileOnly(libs.spigot)
	compileOnly(libs.placeholderapi)
	compileOnly(libs.vault)
	compileOnly(libs.nametagedit)
	compileOnly(libs.worldedit)
	compileOnly(libs.worldguard)
	
	compileOnly(libs.iridiumcolorapi)
	compileOnly(libs.dazzleconf)
	
	compileOnly(libs.storageapi)
	compileOnly(libs.storagegson)
	compileOnly(libs.storagecaffeine)
	
	implementation("org.jetbrains:annotations:24.0.1")
}

val directory = property("group") as String
val release = property("version") as String

tasks {
	shadowJar {
		archiveFileName.set("clans-v$release.jar")
		
		destinationDirectory.set(file("$rootDir/bin/"))
		minimize()
		
		val librariesFolder = "$directory.libs"
		relocate("org.jetbrains.annotations", "$librariesFolder.jetbrains")
	}
	
	withType<JavaCompile> {
		options.encoding = "UTF-8"
	}
	
	clean {
		delete("$rootDir/bin/")
	}
}

bukkit {
	name = "Clans"
	main = "$directory.plugin.ClansPlugin"
	authors = listOf("Qekly")
	
	description = "A recode of original EpicSet-Clans plugin designed for newer versions."
	apiVersion = "1.13"
	version = release
	
	depend = listOf("Vault")
	softDepend = listOf("PlaceholderAPI", "NameTagEdit", "WorldGuard")
	
	libraries = listOf(
		"com.github.VelexNetwork:iridium-color-api:1.2.0",
		"space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.3.0-M2",
		"es.revengenetwork:storage-api-codec:3.1.1",
		"es.revengenetwork:storage-gson-dist:3.1.1",
		"es.revengenetwork:storage-caffeine-dist:3.1.1"
	)
	
	commands {
		register("clan") {
			description = "Command to see multiple clans features."
			aliases = listOf("cl")
		}
		register("clanadmin") {
			description = "Command to see admin plugin commands."
			aliases = listOf("cla")
		}
	}
}

blossom {
	val tokenRoute = "src/main/java/com/aivruu/clans/plugin/Constants.java"
	replaceToken("{version}", release, tokenRoute)
}
