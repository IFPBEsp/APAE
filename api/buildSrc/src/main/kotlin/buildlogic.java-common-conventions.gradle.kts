plugins {
	java
}

group = "br.edu.ifpb.esp.ps"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

tasks.withType<Test> {
	useJUnitPlatform()
}
