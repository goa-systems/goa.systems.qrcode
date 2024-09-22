plugins {
    `java-library`
    `maven-publish`
    `jacoco`
}

group = "goa.systems".toString()
if (hasProperty("ARTIFACT_GROUP")) {
    group = property("ARTIFACT_GROUP").toString()
}

var artifactname: String = "qrcode"
if (hasProperty("ARTIFACT_ID")) {
    artifactname = property("ARTIFACT_ID").toString()
}

version = "0.0.0".toString()
if (hasProperty("ARTIFACT_VERSION")) {
    version = property("ARTIFACT_VERSION").toString()
}

// Specify the Java version via property. "11", "17", "21", ...
var javaversion: String = "17"
if (hasProperty("JAVA_VERSION")) {
    javaversion = property("JAVA_VERSION").toString()
}

// Specify the Java version via property. "11", "17", "21", ...
var savegeneratedimages: Boolean = false
if (hasProperty("SAVE_GENERATED_IMAGES")) {
    savegeneratedimages = property("SAVE_GENERATED_IMAGES").toString().toBoolean()
}

val localreponame = "Project"
val repodir = "repo"

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
	
    implementation("goa.systems:commons:0.7.0")
	
    /* Logging https://central.sonatype.com/artifact/org.slf4j/slf4j-api */
    implementation("org.slf4j:slf4j-api:2.0.16")
	
    /* Specify all dependencies in configuration fullSetup that are conveniently used during development and
       that allow execution of the application but which are optional and up to the customer to define.
       https://central.sonatype.com/artifact/ch.qos.logback/logback-classic
    */
    testImplementation("ch.qos.logback:logback-core:1.5.8")
    testImplementation("ch.qos.logback:logback-classic:1.5.8")
	
    /* QR code logic https://central.sonatype.com/artifact/com.google.zxing/core */
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    /* Batik required for testing of svg graphics. https://central.sonatype.com/artifact/org.apache.xmlgraphics/batik */
    implementation("org.apache.xmlgraphics:batik:1.17"){
        exclude(group = "xml-apis", module = "xml-apis")
    }
    implementation("org.apache.xmlgraphics:batik-transcoder:1.17"){
        exclude(group = "xml-apis", module = "xml-apis")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.valueOf("VERSION_" + javaversion)
    targetCompatibility = JavaVersion.valueOf("VERSION_" + javaversion)
}

tasks.withType<Test> {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
	
    // Take the system property from the "gradle.properties" file or 
    // from the -Dsave.generated.images=[true|false] command line parameter.
    systemProperty("save.generated.images", savegeneratedimages)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

tasks.register<Copy>("exportFromLocalRepo"){

    group = "build"
    description = "Exports from local Maven repository"
    
    dependsOn(tasks.clean)
    dependsOn(tasks.get("publish" + artifactname.replaceFirstChar(Char::titlecase) + "PublicationTo" + localreponame + "Repository"))
    
    from(layout.buildDirectory.dir(repodir + "/" + project.getGroup().toString().replace(".", "/") + "/" + artifactname + "/" + version))
    
    include(artifactname + "-" + version + ".jar")
    include(artifactname + "-" + version + "-javadoc.jar")
    include(artifactname + "-" + version + "-sources.jar")
    include(artifactname + "-" + version + ".pom")
    
    into(layout.buildDirectory.dir("test"))
}
        
tasks.register<Tar>("distribute") {

    group = "build"
    description = "Creates tgz distribution."
    
    dependsOn(tasks["exportFromLocalRepo"])
    
    from(layout.buildDirectory.dir("test"))

    archiveFileName = artifactname + "-" + version + ".tar.gz"
    destinationDirectory = layout.buildDirectory.dir("distributions")
    compression = Compression.GZIP
}

publishing {

    repositories {
        maven {
            name = localreponame
            url = uri(layout.buildDirectory.dir(repodir))
        }
    }
    
    publications {
        create<MavenPublication>(artifactname) {
            groupId = group.toString()
            artifactId = artifactname.toString()
            version = version
            from(components["java"])
            pom {
                name = "GOA systems " + artifactname
                description = "A library."
            }
        }
    }
} 
