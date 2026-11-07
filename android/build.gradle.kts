allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    // Only redirect build output for modules under this repo.
    // Plugins live in the Pub cache (often another Windows drive); forcing their
    // buildDir onto the project drive breaks AGP with "different roots".
    val projectRoot = rootProject.projectDir.canonicalPath
    val isLocalProject = project.projectDir.canonicalPath.startsWith(projectRoot)
    if (isLocalProject) {
        val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
        project.layout.buildDirectory.value(newSubprojectBuildDir)
    }
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
