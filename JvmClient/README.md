# <img src="./docs/iv4xr_logo_1200dpi.png" width="20%"> Space Engineers Demo

This is a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), demonstrating that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks. This repository started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been significantly modified since.

It is not intended for general uses, other than as a testing project for the development of the [Space Engineers iv4XR plugin](https://github.com/iv4xr-project/iv4xr-se-plugin). For more details, please refer to the plugin repository README.

<img src="./docs/SE-sotf1.png" width="100%">

## Kotlin

The demo is written in [Kotlin](https://kotlinlang.org/), a JVM language by JetBrains which is fully interoperable with Java, i.e., it can be used seamlessly from Java while allowing us to write [less code](https://www.ideamotive.co/blog/a-complete-kotlin-guide-for-java-developers) with the same functionality.

# Setup

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

## Basic Space Engineers engine info

### Units and position

- One big block size is 2.5 game distance units. Some vectors are sent in game distance units, some are sent in "cubes".
- Block size is in large cubes so for example 1x1x1 large block is 2.5x2.5x2.5 in game distance units.
- 5 small blocks to one big block. (So small block cube is 0.5x0.5x0.5 distance units.)
- Engineer character can fit onto space of 2x3x2 in small bocks (1x1.5x1 game units), however size in code is 1x1.8x1.
- Position of character is in center, camera is not. Offset vector between center of character and camera is:
  (x=0, y=1.6369286, z=0). Use `Character.DISTANCE_CENTER_CAMERA` constant.
- Block position doesn't always have to be in the center of the block. To locate the center of the block, use midway between `minPosition` and `maxPosition` (extension function `centerPosition`).

### Character and camera orientation

- Character forward vector is identical to camera forward vector - when moving to a side, both forward vectors are changed.
- Character up vector differs from camera up vector when walking. Imagine character moving his head to look up rather than whole body.
- When jetpack is on, up vectors are identical. Imagine character rotating whole body to look up.
- This works for 3d person camera mode, unknown for other modes.
- There is possibility to move camera around character. What is happening with internal variables is not explored.

### Welding and grinding

#### Shared constants:

```
ToolCooldownMs = 250 //not sure if this ever changes, never did for me
```

#### Welder constants and formula

Welder
SpeedMultiplier: 1
DistanceMultiplier: 1
Welder2
SpeedMultiplier: 1.5
DistanceMultiplier: 1.2
Welder3
SpeedMultiplier: 2
DistanceMultiplier: 1.4
Welder4
SpeedMultiplier: 5
DistanceMultiplier: 1.6

```
WELDER_AMOUNT_PER_SECOND = 1 //constant
WelderSpeedMultiplier = 2 //default, configurable in scenario between 0.5 and 5
```
Final formula:
```
WelderSpeedMultiplier * SpeedMultiplier * WELDER_AMOUNT_PER_SECOND * ToolCooldownMs / 1000.0
```

#### Angle grinder constants and formula

AngleGrinder
SpeedMultiplier: 1
DistanceMultiplier: 1
AngleGrinder2
SpeedMultiplier: 1.5
DistanceMultiplier: 1.2
AngleGrinder3
SpeedMultiplier: 2
DistanceMultiplier: 1.4
AngleGrinder4
SpeedMultiplier: 5
DistanceMultiplier: 1.6


```
GRINDER_AMOUNT_PER_SECOND = 2 //constant
GrinderSpeedMultiplier = 2 //default, configurable in scenario between 0.5 and 5
```
Final formula:
```
GrinderSpeedMultiplier * SpeedMultiplier * GRINDER_AMOUNT_PER_SECOND * ToolCooldownMs / 1000.0
```
