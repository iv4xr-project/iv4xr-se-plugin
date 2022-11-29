# Client implementation in Kotlin

This pilot implementation of a plugin client servers as a demo for
the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), showing that iv4XR test agents can control [_
Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to
perform some testing tasks.

Current API documentation is [here](https://iv4xr-project.github.io/iv4xr-se-plugin/)

## Kotlin

The project is written in [Kotlin](https://kotlinlang.org/), a JVM language by JetBrains which **is fully interoperable
with Java**, i.e., it can be used seamlessly from Java while allowing us to
write [less code](https://www.ideamotive.co/blog/a-complete-kotlin-guide-for-java-developers) with the same
functionality.

# Setup without sources

When using [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/) build tool, you can simply add our
library as a dependency from [JitPack](https://jitpack.io/) repository.

The version `main-SNAPSHOT` always points to the newest main branch, which is our development branch. If you want to
target a stable release, change it to a specific version.

Check the official releases here:
https://github.com/iv4xr-project/iv4xr-se-plugin/releases/

Check other possible version values (can target specific commit, branch, etc.) at JitPack here:
https://jitpack.io/#iv4xr-project/iv4xr-se-plugin/

## Maven

Add jitpack repository to your `pom.xml`:

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:

```
<dependency>
    <groupId>com.github.iv4xr-project</groupId>
    <artifactId>iv4xr-se-plugin</artifactId>
    <version>main-SNAPSHOT</version>
</dependency>
```

## Gradle

Add jitpack repository to `build.gradle`:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:

```
dependencies {
  implementation 'com.github.iv4xr-project:iv4xr-se-plugin:main-SNAPSHOT'
}
```

# Manual setup from sources

## How to build

We are using Gradle as the build system. To build the project, run the Gradle task `build`:

```
./gradlew build
```

## How to run unit tests

To build and run unit tests, run:

```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.mock.*"
```

## Running iv4xr tests

The tests require Space Engineers running with the iv4XR plugin enabled.

```
./gradlew :cleanJvmTest :jvmTest --tests "spaceEngineers.iv4xr.*"
```

## Running BDD feature tests

Test scenarios also require Space Engineers running with the iv4XR plugin enabled.

For now, we run BDD tests from IDEA.

* Make sure you have
  installed [plugins](https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html#cucumber-plugin) `Gherkin`
  and `Cucumber for Java`
* Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features)
in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with
Kotlin JVM. We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).

