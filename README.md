# <img src="./docs/iv4xr_logo_1200dpi.png" width="20%"> Space Engineers Demo

This is a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), demonstrating that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks. This repository started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been significantly modified since.

It is not intended for general uses, other than as a testing project for the development of the [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). For more details, please refer to the plugin repository README. 

<img src="./docs/SE-sotf1.png" width="100%">

## Kotlin

The demo is written in [Kotlin](https://kotlinlang.org/), a JVM language by JetBrains which is fully interoperable with Java, i.e., it can be used seamlessly from Java while allowing us to write [less code](https://www.ideamotive.co/blog/a-complete-kotlin-guide-for-java-developers) with the same functionality.

# Setup

## Requirements

Project requires [aplib](https://github.com/iv4xr-project/aplib) dependency. First build `aplib` and install it to the Maven local repository.

Other possibility at the moment is to change dependency do JitPack (in `build.gradle`):

```
implementation 'com.github.iv4xr-project:aplib:v1.2.0'
```

## How to build

For easy copy-paste, here's the git clone command for this repository:

```
git clone git@github.com:iv4xr-project/iv4xrDemo-space-engineers.git
```

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

* Make sure you have installed [plugins](https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html#cucumber-plugin) `Gherkin` and `Cucumber for Java`
* Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features) in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with Kotlin JVM.
We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).