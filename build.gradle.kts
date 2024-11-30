plugins {
	java
	id("org.springframework.boot") version "2.7.15" // Spring Boot 2.7.15
	id("io.spring.dependency-management") version "1.0.15.RELEASE" // Dependency management
}

group = "vn.ibex"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	// implementation("com.turkraft.springfilter:spring-filter-boot-starter:1.0.7")

	// PostgreSQL
	runtimeOnly("org.postgresql:postgresql")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// DevTools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Swagger / OpenAPI (Optional)
	implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
