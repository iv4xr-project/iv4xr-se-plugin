# Client implementation for JVM in Kotlin

This pilot implementation of a plugin client servers as a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), showing that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks.

This project (in this subdirectory) started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been completely rewritten since. You can still find the original code in the history though.

It is not intended for general uses, other than as a testing project for the development of the Space Engineers iv4XR plugin.

## Kotlin

The project is written in [Kotlin](https://kotlinlang.org/), a JVM language by JetBrains which **is fully interoperable with Java**, i.e., it can be used seamlessly from Java while allowing us to write [less code](https://www.ideamotive.co/blog/a-complete-kotlin-guide-for-java-developers) with the same functionality.

# Setup

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

* Make sure you have installed [plugins](https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html#cucumber-plugin) `Gherkin` and `Cucumber for Java`
* Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features) in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with Kotlin JVM.
We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).

# Basic Space Engineers engine info

## Units and position

- One big block size is 2.5 game distance units. Some vectors are sent in game distance units, some are sent in "cubes".
- Block size is in large cubes so for example 1x1x1 large block is 2.5x2.5x2.5 in game distance units.
- 5 small blocks to one big block. (So small block cube is 0.5x0.5x0.5 distance units.)
- Engineer character can fit onto space of 2x3x2 in small bocks (1x1.5x1 game units), however size in code is 1x1.8x1.
- Position of character is in center, camera is not. Offset vector between center of character and camera is:
  (x=0, y=1.6369286, z=0). Use `Character.DISTANCE_CENTER_CAMERA` constant.
- Block position doesn't always have to be in the center of the block. To locate the center of the block, use midway between `minPosition` and `maxPosition` (extension function `centerPosition`).

## Character and camera orientation

- Character forward vector is identical to camera forward vector - when moving to a side, both forward vectors are changed.
- Character up vector differs from camera up vector when walking. Imagine character moving his head to look up rather than whole body.
- When jetpack is on, up vectors are identical. Imagine character rotating whole body to look up.
- This works for 3d person camera mode, unknown for other modes.
- There is possibility to move camera around character. What is happening with internal variables is not explored.

## Blocks

Blocks are the main concept of the game. Some data and behaviour is better documented to avoid confusion.
Some of these concepts are based on observations and are not verified by code so if you find inconsistency in anything, please raise issue.

### Identification, type, id

Block type itself has unique combination of id and type. Although our API mostly uses only block type, this isn't correct or suitable in all cases.
If 2 blocks have the same type, that doesn't guarantee, that they are same blocks. For some (usually edge cases), there are blocks with same type, but different ids.
Also empty string is a valid block type and blocks like that exist. Explore block definitions to know more.

Created block also has its own id (this is different id from block definition id). This id is unique for every block in the game.

### Definitions

Each block type has its own definition properties, which contains basic immutable properties shared among all block of the same type.
To list all available definitions, call `definitions.blockDefinitions`. Individual properties are documented in the code of `BlockDefinition` class.

### Small vs large cube blocks

Most of the blocks are cube blocks, which have cube size 1x1x1. Cube blocks can be small or big.
Small blocks have 0.5 in game unit size. Big blocks have 2.5 in game unit size, so 5 times bigger, 125 small blocks fit into one big block.

Large blocks usually have "Large" prefix, their small counterpart has "Small" prefix.

Blocks, that are not cube blocks are always large (double-check this).

### Targeting a block

When trying to grind a block down or weld it up, there are a few things to keep in mind:
- Observation has property targetBlock. Property is not null only, if there is currently a block in front of character in close range and cursor is pointing directly at it.
- Different grinders and welders have different range so distance from block matters for different tools.
- Sometimes targeting is not as simple as walking to the block. Cursor has to directly target the model and if model has for example hollow places, if you point at hollow space, you are not targeting the block. This is especially tricky when trying to target block automatically by bot.
- In rare cases targeting block model doesn't work either (wheel models) and it requires some moving around. Seems like game glitch.

One way to fix this targeting issue is to try to move cursor around a bit until targetBlock is the desired block.
Another approach is to try to use mount points. Each block has mount points, which are areas, that can be used to connect to other blocks. Getting to position of mount point has higher chance of that part being solid and flat and to successfully target the block. Iterating over all mount points increases chance even further. This still doesn't have to be success in all cases, but works for most.
There are extension methods to calculate mount point position and orientations of blocks ex: `mountPointToRealWorldPosition`

### Compound blocks

Building some blocks actually doesn't build single block, but a pair of blocks, that are tightly bound together.
They behave as normal 2 separate blocks.

If block A spawns together blocks A and B, trying to build block B either:
- Doesn't work at all.
- Spawns only block B.

At least how it was tested so far.

That is normal game behaviour. When placing block programmatically using hack placeAt, only single block is created and second block is also possible to place even if it's not possible normally.

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
