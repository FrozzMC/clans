plugins {
	id("project.publish-conventions")
	alias(libs.plugins.shadow)
}

dependencies {
	compileOnly(libs.spigot)
	
	compileOnly(libs.dazzleconf)
	
	compileOnly(libs.storageapi)
	compileOnly(libs.storagegson)
	compileOnly(libs.storagecaffeine)
	
	implementation("org.jetbrains:annotations:24.0.1")
}

tasks {
	shadowJar {
    val release = property("group") as String
		archiveFileName.set("clans-api-v$release.jar")
		
		destinationDirectory.set(file("$rootDir/bin/"))
		minimize()
    
    val directory = property("group") as String
		val librariesFolder = "$directory.libs"
		relocate("org.jetbrains.annotations", "$librariesFolder.jetbrains")
	}
	
	clean {
		delete("$rootDir/bin/")
	}
}