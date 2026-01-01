import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "2.2.0-RC2"
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "dev.reassembly"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "dev.reassembly.MainKt"
        )
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("net.dv8tion:JDA:5.2.2")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.0")
    implementation("dev.morphia.morphia:morphia-core:2.4.14")
    implementation("dev.morphia.morphia:morphia-kotlin:2.4.14")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("com.aallam.openai:openai-client:3.8.2")
    implementation("io.ktor:ktor-client-okhttp:2.0.0")
    implementation("io.ktor:ktor-client-plugins:2.0.0")
}

tasks.register<Exec>("uploadJar") {
    dependsOn("shadowJar")
    commandLine("./uploadToRemote.sh")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveFileName = "SFTHBot.jar"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-XXLanguage:+BreakContinueInInlineLambdas"))
}