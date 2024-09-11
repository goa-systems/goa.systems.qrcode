plugins {
    `java-library`
    `maven-publish`
}

// Specify the Java version via property. "11", "17", "21", ...
val javaversion = project.property("java.version")
val group = "goa.systems"
val artifact = "qrcode"
version = project.property("ARTIFACT_VERSION").toString()

repositories {
	mavenCentral()
	maven {
        url = uri("https://mvn.goa.systems")
    }
}

dependencies {
    
    /* https://central.sonatype.com/artifact/org.junit.jupiter/junit-jupiter-api */
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    
    /* https://central.sonatype.com/artifact/org.junit.jupiter/junit-jupiter-engine */
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    
    /* https://central.sonatype.com/artifact/org.junit.platform/junit-platform-commons */
	testImplementation("org.junit.platform:junit-platform-commons:1.11.0")
	
	api("goa.systems:commons:0.6.0")
	
	/* Logging https://central.sonatype.com/artifact/org.slf4j/slf4j-api */
	implementation("org.slf4j:slf4j-api:2.0.16")
	
	/* Specify all dependencies in configuration fullSetup that are conveniently used during development and
	   that allow execution of the application but which are optional and up to the customer to define.
	   https://central.sonatype.com/artifact/ch.qos.logback/logback-classic
	*/
	testImplementation("ch.qos.logback:logback-core:1.5.8")
	testImplementation("ch.qos.logback:logback-classic:1.5.8")
	
	/* QR code logic https://central.sonatype.com/artifact/com.google.zxing/core */
	api("com.google.zxing:core:3.5.3")
	api("com.google.zxing:javase:3.5.3")
	
	/* Batik required for testing of svg graphics. https://central.sonatype.com/artifact/org.apache.xmlgraphics/batik */
	api("org.apache.xmlgraphics:batik:1.17"){
        exclude(group = "xml-apis", module = "xml-apis")
    }
	api("org.apache.xmlgraphics:batik-transcoder:1.17"){
        exclude(group = "xml-apis", module = "xml-apis")
    }
}

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_" + javaversion)
    targetCompatibility = JavaVersion.valueOf("VERSION_" + javaversion)
}

tasks.withType<Test> {
	
	useJUnitPlatform()
	
	// Take the system property from the "gradle.properties" file or 
	// from the -Dsave.generated.images=[true|false] command line parameter.
	systemProperty("save.generated.images", providers.systemProperty("save.generated.images"))
}

tasks.register("writeVariables") {
    group = "build"
    description = "Write vars file"
    doLast {
        file("build/export/vars").writeText("export VERSION=${version}\nexport GROUP=goa.systems\nexport ARTIFACT=qrcode")
    }
}
		
tasks.register<Copy>("copyGeneratedLibraries") {
    group = "build"
    description = "Copy generated libraries."
    dependsOn("jar")
    dependsOn("sourcesJar")
    dependsOn("javadocJar")
    
    from(layout.buildDirectory.dir("libs"))
    include("*.jar")
    into(layout.buildDirectory.dir("export/lib"))
}

tasks.register<Copy>("copyGeneratedPomFile") {
    group = "build"
    description = "Copy generated POM description"
    dependsOn("generatePomFileForQrcodePublication")
    
    from(layout.buildDirectory.file("publications/Qrcode/pom-default.xml"))
    into(layout.buildDirectory.dir("export/conf"))
}

java {
	withSourcesJar()
	withJavadocJar()
}

publishing {
	publications {
		create<MavenPublication>(artifact) {
			groupId = group
			artifactId = artifact
			version = version
			from(components["java"])
			pom {
				name = "GOA systems " + artifact
				description = "A QR code library."
			}
		}
	}
}  
    
tasks.register<Tar>("distribute") {
    group = "build"
    description = "Create dist"
    dependsOn("writeVariables")
    dependsOn("copyGeneratedPomFile")
    dependsOn("copyGeneratedLibraries")
    archiveBaseName = rootProject.name
    destinationDirectory = layout.buildDirectory.dir("distributions")
    from(layout.buildDirectory.dir("export"))
    archiveFileName.set("goa.systems.qrcode-${version}.tar.gz")
    compression = Compression.GZIP
}   
