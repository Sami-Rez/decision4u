plugins {
    java
    id("org.springframework.boot") version "3.3.0-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "at.spengergasse"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // Password Strength Assessment
    implementation("com.nulab-inc:zxcvbn:1.8.2")

    // Mail
    // implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")

    // Rest Assured
    testImplementation("io.rest-assured:rest-assured:5.3.1")

    // Crypto Bouncy Castle
    // implementation("org.bouncycastle:bcpkix-jdk15on:1.70")

    // Map Struct
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

    // Json Path
    // implementation("com.jayway.jsonpath:json-path:2.9.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
