plugins {
  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("io.micronaut.application") version "3.6.2"
  id("io.micronaut.test-resources") version "3.6.2"
}

version = "0.1"
group = "com.example"

repositories {
  mavenCentral()
}

dependencies {
  annotationProcessor("io.micronaut.data:micronaut-data-processor")
  annotationProcessor("io.micronaut:micronaut-http-validation")
  implementation("io.micronaut:micronaut-http-client")
  implementation("io.micronaut:micronaut-jackson-databind")
  implementation("io.micronaut.data:micronaut-data-jdbc")
  implementation("io.micronaut.liquibase:micronaut-liquibase")
  implementation("io.micronaut.sql:micronaut-jdbc-hikari")
  implementation("jakarta.annotation:jakarta.annotation-api")
  implementation("io.micronaut.reactor:micronaut-reactor")
  implementation("io.projectreactor:reactor-core")
  implementation("io.projectreactor:reactor-tools:3.4.23")
  implementation("io.projectreactor:reactor-test")
  runtimeOnly("ch.qos.logback:logback-classic")
  runtimeOnly("mysql:mysql-connector-java")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:mysql")
  testImplementation("org.testcontainers:testcontainers")
  implementation("io.micronaut:micronaut-validation")

  runtimeOnly("p6spy:p6spy:3.9.1")
  testImplementation("org.assertj:assertj-core:3.23.1")
}


application {
  mainClass.set("com.example.Application")
}
java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}


graalvmNative.toolchainDetection.set(false)
micronaut {
  runtime("netty")
  testRuntime("junit5")
  processing {
    incremental(true)
    annotations("com.example.*")
  }
}



