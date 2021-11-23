# Client implementation for JVM in Kotlin

This pilot implementation of a plugin client servers as a demo for the [iv4XR testing framework](https://github.com/iv4xr-project/aplib), showing that iv4XR test agents can control [_Space Engineers_](https://www.spaceengineersgame.com/) (a game by [Keen Software House](https://www.keenswh.com/)) to perform some testing tasks.

This project (in this subdirectory) started as a fork of the [*Lab Recruits* demo](https://github.com/iv4xr-project/iv4xrDemo), but has been completely rewritten since. You can still find the original code in the history though.

It is not intended for general uses, other than as a testing project for the development of the Space Engineers iv4XR plugin.

Current API documentation is [here](https://iv4xr-project.github.io/iv4xr-se-plugin/)

## Kotlin

The project is written in [Kotlin](https://kotlinlang.org/), a JVM language by JetBrains which **is fully interoperable with Java**, i.e., it can be used seamlessly from Java while allowing us to write [less code](https://www.ideamotive.co/blog/a-complete-kotlin-guide-for-java-developers) with the same functionality.

# Setup without sources

When using [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/) build tool, you can simply add our library as a dependency from [JitPack](https://jitpack.io/) repository.

The version `main-SNAPSHOT` always points to the newest main branch, which is our development branch.
If you want to target a stable release, change it to a specific version.
Versions 0.3.1 and lower do not work as JitPack was not configured at the time.

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

* Make sure you have installed [plugins](https://www.jetbrains.com/help/idea/enabling-cucumber-support-in-project.html#cucumber-plugin) `Gherkin` and `Cucumber for Java`
* Right-click [.feature file](https://github.com/iv4xr-project/iv4xrDemo-space-engineers/tree/se-dev/src/jvmTest/resources/features) in IDEA and select "Run".

## Using Eclipse

Eclipse does not support Kotlin multiplatform projects and so far we haven't been able to configure it to run with Kotlin JVM. We recommend using the project with JetBrains [IDEA](https://www.jetbrains.com/idea/download/).

## Naming convention

The project contains plugin in C# and JVM client in Kotlin. Those 2 languages have different naming conventions.

- Properties and methods in C# usually begin with uppercase ("`PascalCase`"), but they begin with lowercase in Kotlin ("`camelCase`").
- Interfaces in C# sometimes have "I" prefix to signal interface. We usually refer to interface without the "I" in the documentation unless directly referencing a class.
- If you see reference to variable or property, always interpret it in context of the particular language.
- Since the server is implemented in C#, JSON-RPC protocol uses C# conventions and all method names and parameters are in `PascalCase`.

# Basic Space Engineers engine info

## Units and position

- One big block size is 2.5 game meters. Some vectors are sent in meters, some are sent in "cubes".
- [Block size](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-block/size.html) is in large cubes so for example 1x1x1 large block is 2.5x2.5x2.5 in meters.
- Use enum [CubeSize](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-cube-size/index.html) and/or [LARGE_BLOCK_CUBE_SIDE_SIZE](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-l-a-r-g-e_-b-l-o-c-k_-c-u-b-e_-s-i-d-e_-s-i-z-e.html), [SMALL_BLOCK_CUBE_SIDE_SIZE](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-s-m-a-l-l_-b-l-o-c-k_-c-u-b-e_-s-i-d-e_-s-i-z-e.html) constants for converting.
- 5 small blocks to one big block. (So small block cube is 0.5x0.5x0.5 meters.)
- Engineer character can fit into space of 2x3x2 in small blocks (1x1.5x1 meters), however the size in the code is 1x1.8x1.
- The position of character is at its bottom, the camera is not. The offset vector between the bottom of character and the camera is:
  (x=0, y=1.6369286, z=0). Use [Character.DISTANCE_CENTER_CAMERA](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/-companion/-d-i-s-t-a-n-c-e_-c-e-n-t-e-r_-c-a-m-e-r-a.html) constant (or you can use the difference between position and camera position).
- Block `position` is always between `minPosition` and `maxPosition`, 
but it doesn't always have to be in the center of the block (or sometimes it's identical to `minPosition`).
To locate the center of the block, use midway between `minPosition` and `maxPosition` (extension function [centerPosition](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model.extensions/center-position.html)).

## Character and camera orientation

- Character forward vector is identical to camera forward vector - when moving to a side, both forward vectors are changed.
- Character up vector differs from camera up vector when walking. Imagine character moving his head to look up rather than whole body.
- When jetpack is on, up vectors are identical. Imagine character rotating whole body to look up.
- This works for 3d person camera mode, unknown for other modes.
- There is possibility to move camera around character. What is happening with internal variables is not explored.


## Moving character


### Basic movement API

- Method [moveAndRotate](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html) accepts movement vector. Vector represents direction. 
- It's value defines type (speed) of movement. If it's less than 0.4, it is slow movement. If less than or equal to 1.6, it is walk. If over 1.6, it is sprint. This is relevant when actually walking (ex. not using jetpack).
- Use convenience extension methods `normalizeAsWalk`, `normalizeAsRun`, `normalizeAsSprint` to adjust vector size to your needs.
- Check [CharacterMovementType](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-movement-type/index.html) for more information and to check constants.
- There is also movement while in crouch.

### Movement types and speed

There are other movement types on Space Engineers, so this is not full list of possibilities.
Following table describes differences between movements and their speeds.

| Movement type | Max speed (m/s) | Movement vector threshold |
| --- | --- | --- |
| Crouch walk |   2  | ? |
| Walk        |   3  | 0.4 <  |
| Run         |   6  | 1.6 <= |
| Sprint      |  10  | 1.6 > |
| Jetpack     | 110  | ? |

### Continuous movement

Calling [`moveAndRotate`](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html) will behave in a similar fashion as a single keyboard stroke. To keep moving, the command has to be sent repeatedly, behaving as key constantly being pressed. This is quite inconvenient for the code and not very deterministic since commands are sent rapidly over TCP without any kind of time synchronization.

For that reason, `moveAndRotate` has `ticks` parameter with the default value of 1, which determines the number of ticks for the command to be active (equivalent to the key being pressed). One second has 60 ticks. To stop the movement preemptively before the specified number of ticks elapses, call `moveAndRotate` with 0 ticks (or supersede the movement by sending a new command).

This movement is quite deterministic when repeating exactly the same scenario with exact positions and movement values. Sometimes the values are still slightly off, especially when the scenario is loaded for the first time.


## Rotating character

TODO: Explore how roll and rotation vector size affects rotation.

## Blocks

Blocks are the main concept of the game. Some data and behaviour is better documented to avoid confusion.
Some of these concepts are based on observations and are not verified by code so if you find inconsistency in anything, please raise issue.

### DefinitionId, id

Block type itself has a unique combination of id and type (now represented by the DefinitionId class). Blocks are the same type, if they have same DefinitionId.

Previously we used only block type, which wasn't unique enough and caused problems in some cases. Now DefinitionId will be used for defining block type.
That means some API changes:

- Admin.Block.PlaceAt (done)
- Items.setToolbarItem (not yet, expected to change in the future)

Created block instance has its own ID (this is different id from block definition id). This ID is a unique random integer (the current implementation exposes an internal block ID, and the exact guarantees of uniqueness are not known – we expect it to be unique at least within the grid, hopefully within the whole scenario – please report any collisions).


### Block-specific instance properties

Basic properties that are common for all block types are defined in the interface Block. In this sense, we are talking about mutable properties, which can have different values for every instance of a block (such as position or integrity).

Some blocks have extra properties related to their functionality. For example, all door blocks have a boolean `opened` property. Door in combination with "use" functionality can allow the client to open a door and check the door state.

Generators have a property which represents output, etc. Functional and Terminal blocks are very common.

When calling JSON API, you always get an instance of `Block`, but if blocks have extra properties, it will be a subclass with extra properties. The returned list is polymorphic and each block has instance depending on its type.
You can use standard Java/Kotlin keywords like "instanceof" or "is" to check for subclasses and cast. Check `PolymorphicBlocksTest` for a Kotlin example.

The class hierarchy is not flat. You can explore all block hierarchy in new definitions BlockHierarchy API, which returns relations between blocks.
Subclassing is done only for classes which have extra properties. Otherwise, only "Block" is returned (or very often FunctionalBlock or TerminalBlock).
Information about class hierarchy and what classes will be used for different blocks is also cached in `src/jvmMain/resources` directory as JSON files.

If there is a property or a block type functionality missing in the API, and you'd like it to be added, please file an issue to let us know.


### Definitions

Each block type has its own definition properties, which contains basic immutable properties shared among all block of the same type.
To list all available definitions, call [Definitions.BlockDefinitions](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-definitions/block-definitions.html). Individual properties are documented in the code of `BlockDefinition` class.

### Small vs large cube blocks

Most of the blocks are cube blocks, which have cube size 1x1x1. Cube blocks can be small or big.
Small blocks have 0.5 meters. Big blocks have 2.5 meters, so 5 times bigger, 125 small blocks fit into one big block.

Large blocks usually have "Large" prefix, their small counterpart has "Small" prefix.

### Targeting a block

When trying to grind a block down or weld it up, there are a few things to keep in mind:
- CharacterObservation has property [targetBlock](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-observation/target-block.html). Property is not null only, if there is currently a block in front of character in close range and cursor is pointing directly at it.
- Different grinders and welders have different range so distance from block matters for different tools.
- Sometimes targeting is not as simple as walking to the block. Cursor has to directly target the model and if model has for example hollow places, if you point at hollow space, you are not targeting the block. This is especially tricky when trying to target block automatically by bot.
- In rare cases targeting block model doesn't work either (wheel models) and it requires some moving around. Seems like game glitch.

One way to fix this targeting issue is to try to move cursor around a bit until targetBlock is the desired block.
Another approach is to try to use mount points. Each block has mount points, which are areas, that can be used to connect to other blocks. Getting to position of mount point has higher chance of that part being solid and flat and to successfully target the block. Iterating over all mount points increases chance even further. This still doesn't have to be success in all cases, but works for most.
There are extension methods to calculate mount point position and orientations of blocks ex: [mountPointToRealWorldPosition](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model.extensions/mount-point-to-real-world-position.html)

### Using blocks

A block can have multiple [use objects](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-block/use-objects.html) – active parts that can do something when used. For instance, it can be a chair, on which you can sit. But for example door blocks have door themselves, but also a terminal. Button blocks can have up to 4 buttons and each button is different. Information about block's use objects is in the property `UseObjects`. 

It is possible to "use" blocks. Same as the "F" key in the game. To do that, use the [Character.Use](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/use.html) API call.

This requires cursor to point exactly at the active part of the block to work. If there's active use object, it will be in [CharacterObservation.TargetUseObject](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-observation/target-use-object.html).

There is currently no reliable way to point at specific use object, but there is also admin hack that allows using block without the need of precise targeting.
[Admin.Character.Use](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character-admin/use.html)


### Compound blocks

Building some blocks actually doesn't build single block, but a pair of blocks, that are tightly bound together.
They behave as normal 2 separate blocks.

If block A spawns together blocks A and B, trying to build block B either:
- Doesn't work at all.
- Spawns only block B.

At least how it was tested so far.

That is normal game behaviour. When placing block programmatically using hack placeAt, only single block is created and second block is also possible to place even if it's not possible normally.

### Welding and grinding

Using different welders and grinders gives different results.
They have different speed and different reach. Below is a formula, that tries to help with determining grinding/welding speed.

#### Shared constants:

```
ToolCooldownMs = 250 //not sure if this ever changes, never did for me
```

#### Welder constants and formula


| Welder | SpeedMultiplier | DistanceMultiplier|
| --- | --- | --- |
| Welder  | 1   | 1   |
| Welder2 | 1.5 | 1.2 |
| Welder3 | 2   | 1.4 |
| Welder4 | 5   | 1.6 |


```
WELDER_AMOUNT_PER_SECOND = 1 //constant
WelderSpeedMultiplier = 2 //default, configurable in scenario between 0.5 and 5
```
Final formula:
```
WelderSpeedMultiplier * SpeedMultiplier * WELDER_AMOUNT_PER_SECOND * ToolCooldownMs / 1000.0
```

#### Angle grinder constants and formula

| Grinder | SpeedMultiplier | DistanceMultiplier|
| --- | --- | --- |
| AngleGrinder  | 1   | 1   |
| AngleGrinder2 | 1.5 | 1.2 |
| AngleGrinder3 | 2   | 1.4 |
| AngleGrinder4 | 5   | 1.6 |


```
GRINDER_AMOUNT_PER_SECOND = 2 //constant
GrinderSpeedMultiplier = 2 //default, configurable in scenario between 0.5 and 5
```
Final formula:
```
GrinderSpeedMultiplier * SpeedMultiplier * GRINDER_AMOUNT_PER_SECOND * ToolCooldownMs / 1000.0
```


## Advanced/plugin customization


### Adding new block-specific fields

Since the code is polymorphic and everything is static on the code level, we generate the code based on configuration and source files. All the code is generated by BlockMappingGeneratorRunner, and it generates both the code for C# side and Kotlin side. It generates classes for Block, BlockDefinitions and their mappings and serializers. Generating blocks and block definitions are very similar, defining fields and interfaces is the same for both.

Mapping for Block interface is done manually on the C# side and not generated, because we often manually retrieve data and convert to different structures. Mapping for BlockDefinitions is done automatically, because we expect it to be simple field passing.

A few tips:
- Commit or stash your changes so that you can easily revert changes in case generated code does not compile.
- If the code doesn't compile after running the generator, find out why, fix, revert the code, then run the generator again.

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

Most of the plugin commands by default control the main local character, which is also bound to the camera and input controls.
It is possible to add more characters into the game by using Admin.Character.Create.
To switch between characters, use Admin.Character.Switch, passing character ID. Main character ID is always se0.

A few points:
- This feature is a bit experimental, they may be glitches and unexpected behaviour, please send reports if you run into anything.
- Toolbar is shared between characters.
- Only main character can place blocks normally (but can place blocks through admin commands).
- Created characters do not sustain scenario saves (do blocks?).

### Commands unaffected by character switch

- Session.LoadScenario
- Observer.TakeScreenshot
- Blocks.Place (always places for the main character - use Admin.Blocks.PlaceInGrid/PlaceAt to place blocks for other characters)


### TODO
- Remove character
- Continuous movement
- Explore what happens when saved (to blocks? to character)
- Character, player id mappings.
