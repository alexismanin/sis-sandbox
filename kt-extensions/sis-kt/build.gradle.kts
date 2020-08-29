dependencies {
    api("org.apache.sis.storage:sis-storage:2.0-M0073")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
}

sourceSets.main {
    java.srcDirs("src/main")
}

sourceSets.test {
    java.srcDirs("src/test")
}