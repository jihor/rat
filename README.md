# rat - Remove Advice Tool
A simple `javac` annotation processor, which produces warnings for code elements annotated with `@Remove`, `@RemoveInVersion` and `@RemoveAfterDate` annotations.
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
- `@Remove`: denotes that the element should be removed. 
    - Fields: 
        - `reason` (optional) 
- `@RemoveInVersion`: denotes that the element should be removed in a specific version (e.g. `2.0`). 
   
    - Fields: 
        - `version` (mandatory, in any sensible format)
        - `reason` (optional) 
- `@RemoveAfterDate`: denotes that the element should be removed after a certain date.
    - Fields: 
        - `date` (mandatory, in `yyyy-MM-dd` format)
        - `reason` (optional) 

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