plugins {
	id("project.common-conventions")
	`maven-publish`
}

publishing {
	publications {
		create<MavenPublication>("release") {
			from(components["java"])
		}
	}
}