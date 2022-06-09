# Blocks

Blocks are the main concept of the game. Some data and behaviour is better documented to avoid confusion. Some of these
concepts are based on observations and are not verified by code so if you find inconsistency in anything, please raise
an [issue](https://github.com/iv4xr-project/iv4xr-se-plugin/issues/new).

## DefinitionId, id

Block type itself has a unique combination of id and type (represented by
the [DefinitionId](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-definition-id/index.html)
class). Blocks are the same type, if they have same `DefinitionId`.

Created block instance has its own ID (this is different id from block definition id). This ID is a unique random
integer (the current implementation exposes an internal block ID, and the exact guarantees of uniqueness are not known –
we expect it to be unique at least within the grid, hopefully within the whole scenario – please report any collisions).

## Block-specific instance properties

Basic properties that are common for all block types are defined in the interface Block. In this sense, we are talking
about mutable properties, which can have different values for every instance of a block (such as position or integrity).

Some blocks have extra properties related to their functionality. For example, all door blocks have a boolean `opened`
property. Door in combination
with [Character.use](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/use.html)
functionality can allow the client to open a door and check the door state.

Generators have a property which represents output, etc. Functional and Terminal blocks are very common.

When calling JSON API, you always get an instance of `Block`, but if blocks have extra properties, it will be a subclass
with extra properties. The returned list is polymorphic and each block has instance depending on its type. You can use
standard Java/Kotlin keywords like "instanceof" or "is" to check for subclasses and cast. Check `PolymorphicBlocksTest`
for a Kotlin example.

The class hierarchy is not flat. You can explore all block hierarchy in new definitions BlockHierarchy API, which
returns relations between blocks. Subclassing is done only for classes which have extra properties. Otherwise, only "
Block" is returned (or very often FunctionalBlock or TerminalBlock). Information about class hierarchy and what classes
will be used for different blocks is also cached in `src/jvmMain/resources` directory as JSON files.

If there is a property or a block type functionality missing in the API, and you'd like it to be added, please file an
issue to let us know.

## Definitions

Each block type has its own definition properties, which contains basic immutable properties shared among all block of
the same type. To list all available definitions,
call [Definitions.BlockDefinitions](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-definitions/block-definitions.html)
. Individual properties are documented in the code of `BlockDefinition` class.

## Small vs large cube blocks

Most of the blocks are cube blocks, which have cube size 1x1x1. Cube blocks can be small or big. Small blocks have 0.5
meters. Big blocks have 2.5 meters, so 5 times bigger, 125 small blocks fit into one big block.

Large blocks usually have "Large" prefix, their small counterpart has "Small" prefix.

## Targeting a block

When trying to grind a block down or weld it up, there are a few things to keep in mind:

- CharacterObservation has
  property [targetBlock](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-observation/target-block.html)
  . Property is not null only, if there is currently a block in front of character in close range and cursor is pointing
  directly at it.
- Different grinders and welders have different range so distance from block matters for different tools.
- Sometimes targeting is not as simple as walking to the block. Cursor has to directly target the model and if model has
  for example hollow places, if you point at hollow space, you are not targeting the block. This is especially tricky
  when trying to target block automatically by a bot.
- In rare cases targeting block model doesn't work either (wheel models) and it requires some moving around. Seems like
  a game glitch.

One way to fix this targeting issue is to try to move cursor around a bit until targetBlock is the desired block.
Another approach is to try to use mount points. Each block has mount points, which are areas, that can be used to
connect to other blocks. Getting to position of mount point has higher chance of that part being solid and flat and to
successfully target the block. Iterating over all mount points increases chance even further. This still doesn't have to
be success in all cases, but works for most. There are extension methods to calculate mount point position and
orientations of blocks
ex: [mountPointToRealWorldPosition](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model.extensions/mount-point-to-real-world-position.html)

## Using blocks

A block can have
multiple [use objects](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-block/use-objects.html)
– active parts that can do something when used. For instance, it can be a chair, on which you can sit. But for example
door blocks have door themselves, but also a terminal. Button blocks can have up to 4 buttons and each button is
different. Information about block's use objects is in the property `UseObjects`.

It is possible to "use" blocks. Same as the "F" key in the game. To do that, use
the [Character.Use](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/use.html)
API call.

This requires cursor to point exactly at the active part of the block to work. If there's active use object, it will be
in [CharacterObservation.TargetUseObject](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-observation/target-use-object.html)
.

There is currently no reliable way to point at specific use object, but there is also admin hack that allows using block
without the need of precise targeting.
[Admin.Character.Use](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character-admin/use.html)

## Compound blocks

Building some blocks actually doesn't build single block, but a pair of blocks, that are tightly bound together. They
behave as normal 2 separate blocks.

If block A spawns together blocks A and B, trying to build block B either:

- Doesn't work at all.
- Spawns only block B.

At least how it was tested so far.

That is normal game behaviour. When placing block programmatically using hack placeAt, only single block is created and
second block is also possible to place even if it's not possible normally.

## Welding and grinding

Using different welders and grinders gives different results. They have different speed and different reach. Below is a
formula, that tries to help with determining grinding/welding speed.

### Shared constants:

```
ToolCooldownMs = 250 //not sure if this ever changes, never did for me
```

### Welder constants and formula

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

### Angle grinder constants and formula

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
