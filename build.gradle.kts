plugins {
    kotlin("jvm") version "1.4.0"
}

allprojects {
    repositories {
        maven(url="https://nexus.geomatys.com/repository/maven-public")
        mavenCentral()
        jcenter() 
    }
}

subprojects {
    version = "0.1"
    apply{plugin("kotlin")}

    dependencies {
        // Use JUnit Jupiter API for testing.
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")

        // Use JUnit Jupiter Engine for testing.
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

        // Use the Kotlin test library.
        //testImplementation("org.jetbrains.kotlin:kotlin-test")

        // Use the Kotlin JUnit integration.
        //testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}