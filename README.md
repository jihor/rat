# rat - Removal Advice Tool
This is a simple `javac` annotation processor, which produces compilation warnings for code elements annotated with `@Remove`, `@RemoveInVersion` and `@RemoveAfterDate` annotations.
Essentially, these are TODOs, done the annotation processor way!

[![Download](https://api.bintray.com/packages/jihor/maven/rat/images/download.svg)](https://bintray.com/jihor/maven/rat/_latestVersion)
[![CircleCI](https://circleci.com/gh/jihor/rat/tree/master.svg?style=shield)](https://circleci.com/gh/jihor/rat/tree/master)

### Download
##### Gradle
```
repositories {
    jcenter()
}

dependencies {
    compileOnly group: 'ru.jihor.rat', name: 'rat', version: '<version>'
    // or
    // compileOnly 'ru.jihor.rat:rat:<version>'
}
```
##### Maven
```
<dependency>
    <groupId>ru.jihor.rat</groupId>
    <artifactId>rat</artifactId>
    <version>(version)</version>
    <type>pom</type>
</dependency>
```

### Usage
#### Annotations
The following annotations are available:
- `@Remove`: denotes that the element should be removed
    - Fields: 
        - `reason` (optional) 
    - How does it work:  the annotation processor will always print a warning for the annotated element
- `@RemoveInVersion`: denotes that the element should be removed in an upcoming version (e.g. `2.0`) 
    - Fields: 
        - `version` (mandatory, in any sensible format)
        - `reason` (optional) 
    - How does it work: the annotation processor will print a warning for the annotated element if version in the annotation <= current version. Snapshots are not respected, e.g. an element annotated with `@RemoveInVersion(version = "2.0")` will produce a warning in a `2.0-SNAPSHOT` build 
- `@RemoveAfterDate`: denotes that the element should be removed after a certain date
    - Fields: 
        - `date` (mandatory, in `yyyy-MM-dd` format)
        - `reason` (optional) 
    - How does it work:  the annotation processor will print a warning for the annotated element if date in the annotation < current date

See `ru.jihor.rat.Demo` in `test` scope for examples.

#### Processor
##### Gradle
The processor will work out-of-the-box, the only thing that needs to be passed is the project version to compare the `version` in `@RemoveInVersion` against.
This variable is controlled by `rat.project.version` annotation processor option:
```
tasks.withType(JavaCompile) {
    options.compilerArgs += ["-Arat.project.version=$project.version"]
}
// or, if Groovy is used:
tasks.withType(GroovyCompile) {
    options.compilerArgs += ["-Arat.project.version=$project.version"]
}
```
##### IntelliJ Idea
The Gradle settings mentioned before will sadly NOT set the version for the annotation processor when `Build`&rarr;`Build project` is executed in IntelliJ Idea, because of this bug: https://youtrack.jetbrains.com/issue/IDEA-154038 .

The workaround is to open the IDE settings and set up the version manually in `Build, Execution, Environment` -> `Compiler` -> `Java Compiler` -> `Additional command line parameters`: `-Arat.project.version=<project version>`