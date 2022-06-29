# Moving the character

## Basic movement API

- The method [moveAndRotate]( https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html)
accepts a movement vector (among other parameters). The movement vector represents direction and speed.
- The size of the vector defines type (speed) of the movement. If it's less than 0.4, it is a slow walk. If less than or equal to 1.6,
  it is run (game default). If over 1.6, it is sprint. This is relevant when actually walking (not using jetpack).
- Use convenience extension methods `normalizeAsWalk`, `normalizeAsRun`, `normalizeAsSprint` to adjust a vector size to
  your needs.
- Check [CharacterMovementType](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-character-movement-type/index.html)
for more information and to see the relevant constants.
- Movement is possible even while crouching.
- The sprint speed only works when running forward.
- This method only works on the character, doesn't work when controlling vehicles.

## Movement types and speed

There are other movement types in Space Engineers, so this is not a full list of possibilities. The following table list movement types, their speeds and what size the movement vector should be to choose this type of movement. The sprint speed is 10 m/s only when moving forward.

| Movement type | Max speed (m/s) | Movement vector threshold |
| --- | --- | --- |
| Crouch walk |   2  | ? |
| Walk        |   3  | 0.4 <  |
| Run         |   6  | 1.6 <= |
| Sprint      |  10  | 1.6 > |
| Jetpack     | 110  | ? |

## Continuous movement

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

- Rotating around is done by the second `rotate3` parameter
  of [moveAndRotate](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/move-and-rotate.html).
  Use [Vec2F constants](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-vec2-f/-companion/index.html)
  for rotating in specific directions.
- The `roll` parameter is used for rotating in the remaining Z axis (keys `Q` and `E`). Use -1 or 1 values.

We didn't explore how roll and rotation vector size affects rotation.

## Input API

[Input](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-input/index.html)
API allows recording of keyboard and mouse input from the player and saving it into snapshots per every game frame.
Those snapshots can then be sent to the game and emulate this input.

Inputs can be any keys that the game uses so this API is potentially very general and allows to do things otherwise impossible by
the current API.


## Wrapper around moveAndRotate and Input API

Both moveAndRotate and input API can be used to same things – moving the character, but these APIs are very different.

A wrapper
interface [CharacterMovement](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.movement/-character-movement/index.html)
has been created to unify those two methods for movement. This wrapper uses enums to specify directions and orientations.

[ReplayMovement](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.movement/-replay-movement/index.html)
implementation moves character by sending key presses emulating keyboard movement.
This implementation works on vehicles too.

[VectorMovement](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.movement/-vector-movement/index.html)
implementation is sending vectors necessary for the movement.

The API is identical, but the behaviour doesn't have to be always same. For example rotating can be at different speeds.
Use vectorMultiplier constructor parameter
of [VectorMovement](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.movement/-vector-movement/index.html)
to adjust vector values if necessary.
