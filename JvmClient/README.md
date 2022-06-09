# Client implementation for JVM in Kotlin

This pilot implementation of a plugin client servers as a demo for
the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), showing that iv4XR test agents can control [_
Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to
perform some testing tasks.

This project (in this subdirectory) started as a fork of the [*Lab
Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been completely rewritten since. You can still find
the original code in the history though.

It is not intended for general uses, other than as a testing project for the development of the Space Engineers iv4XR
plugin.

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
target a stable release, change it to a specific version. Versions 0.3.1 and lower do not work as JitPack was not
configured at the time.

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
*
Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features)
in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with
Kotlin JVM. We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).

## Naming convention

The project contains plugin in C# and JVM client in Kotlin. Those 2 languages have different naming conventions.

- Properties and methods in C# usually begin with uppercase ("`PascalCase`"), but they begin with lowercase in
  Kotlin ("`camelCase`").
- Interfaces in C# sometimes have "I" prefix to signal interface. We usually refer to interface without the "I" in the
  documentation unless directly referencing a class.
- If you see reference to variable or property, always interpret it in context of the particular language.
- Since the server is implemented in C#, JSON-RPC protocol uses C# conventions and all method names and parameters are
  in `PascalCase`.

## Moving character

### Basic movement API

-
Method [moveAndRotate](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html)
accepts movement vector. Vector represents direction.
- It's value defines type (speed) of movement. If it's less than 0.4, it is slow movement. If less than or equal to 1.6,
  it is walk. If over 1.6, it is sprint. This is relevant when actually walking (ex. not using jetpack).
- Use convenience extension methods `normalizeAsWalk`, `normalizeAsRun`, `normalizeAsSprint` to adjust vector size to
  your needs.
-
Check [CharacterMovementType](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-movement-type/index.html)
for more information and to check constants.
- There is also movement while in crouch.

### Movement types and speed

There are other movement types on Space Engineers, so this is not full list of possibilities. Following table describes
differences between movements and their speeds. Sprint speed is 10 only when walking forward.

| Movement type | Max speed (m/s) | Movement vector threshold |
| --- | --- | --- |
| Crouch walk |   2  | ? |
| Walk        |   3  | 0.4 <  |
| Run         |   6  | 1.6 <= |
| Sprint      |  10  | 1.6 > |
| Jetpack     | 110  | ? |

### Continuous movement

Calling [`moveAndRotate`](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html)
will behave in a similar fashion as a single keyboard stroke. To keep moving, the command has to be sent repeatedly,
behaving as key constantly being pressed. This is quite inconvenient for the code and not very deterministic since
commands are sent rapidly over TCP without any kind of time synchronization.

For that reason, `moveAndRotate` has `ticks` parameter with the default value of 1, which determines the number of ticks
for the command to be active (equivalent to the key being pressed). One second has 60 ticks. To stop the movement
preemptively before the specified number of ticks elapses, call `moveAndRotate` with 0 ticks (or supersede the movement
by sending a new command).

This movement is quite deterministic when repeating exactly the same scenario with exact positions and movement values.
Sometimes the values are still slightly off, especially when the scenario is loaded for the first time.

## Rotating character

TODO: Explore how roll and rotation vector size affects rotation.

## Advanced/plugin customization

### Adding new block-specific fields

Since the code is polymorphic and everything is static on the code level, we generate the code based on configuration
and source files. All the code is generated by BlockMappingGeneratorRunner, and it generates both the code for C# side
and Kotlin side. It generates classes for Block, BlockDefinitions and their mappings and serializers. Generating blocks
and block definitions are very similar, defining fields and interfaces is the same for both.

Mapping for Block interface is done manually on the C# side and not generated, because we often manually retrieve data
and convert to different structures. Mapping for BlockDefinitions is done automatically, because we expect it to be
simple field passing.

A few tips:

- Commit or stash your changes so that you can easily revert changes in case generated code does not compile.
- If the code doesn't compile after running the generator, find out why, fix, revert the code, then run the generator
  again.

#### Base interface

To add new field to the `Block` interface:

- Add field to variable `commonBlockFields` in `BlockMappingGenerator`.
- Run the generator.
- Add field to the Block interface itself.

#### Polymorphic blocks

To add or modify polymorphic block type:

- Add/modify a record in `blockMappings` in `BlockSerializer`.
- Run the generator.

## Multiple characters

### Overview

Most of the plugin commands by default control the main local character, which is also bound to the camera and input
controls. It is possible to add more characters into the game by using Admin.Character.Create. To switch between
characters, use Admin.Character.Switch, passing character ID. Main character ID is always se0.

A few points:

- This feature is a bit experimental, there may be glitches and unexpected behaviour, please
  file [issue](https://github.com/iv4xr-project/iv4xr-se-plugin/issues/new) if you run into anything.
- Toolbar is shared between characters.
- Only the main character can place blocks normally (but any character can place blocks through admin commands).

### Commands unaffected by character switch

- Session.LoadScenario
- Observer.TakeScreenshot
- Blocks.Place (always places for the main character - use Admin.Blocks.PlaceInGrid/PlaceAt to place blocks for other
  characters)

### TODO

- Remove character
