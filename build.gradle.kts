import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    idea
    kotlin("jvm") version "1.3.11" apply false
}

allprojects {
    group = "koactor"
    version = "1.0-SNAPSHOT"
}