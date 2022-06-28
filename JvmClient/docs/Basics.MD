# Basic Space Engineers engine info

## Units and position

- The unit of distance in the game is one meter. Speed is in meters per second.
- One big block size is 2.5 game meters. Some vectors are sent in meters, some are sent in "cubes".
- [Block size](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-block/size.html)
  is in large cubes so for example 1x1x1 large block is 2.5x2.5x2.5 in meters.
- Use
  enum [CubeSize](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-cube-size/index.html)
  and/or [LARGE_BLOCK_CUBE_SIDE_SIZE](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-l-a-r-g-e_-b-l-o-c-k_-c-u-b-e_-s-i-d-e_-s-i-z-e.html), [SMALL_BLOCK_CUBE_SIDE_SIZE](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model/-s-m-a-l-l_-b-l-o-c-k_-c-u-b-e_-s-i-d-e_-s-i-z-e.html)
  constants for converting.
- 5 small blocks to one big block. (So small block cube is 0.5x0.5x0.5 meters.)
- Engineer character can fit into space of 2x3x2 in small blocks (1x1.5x1 meters), however the size in the code is
  1x1.8x1.
- The position of character is at its bottom, the camera is not. The offset vector between the bottom of character and
  the camera in 1st person view is:
  (x=0, y=1.6369286, z=0).
  Use [Character.DISTANCE_CENTER_CAMERA](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.controller/-character/-companion/-d-i-s-t-a-n-c-e_-c-e-n-t-e-r_-c-a-m-e-r-a.html)
  constant (or you can use the difference between position and camera position).
- Block `position` is always between `minPosition` and `maxPosition`, but it doesn't always have to be in the center of
  the block (or sometimes it's identical to `minPosition`). To locate the center of the block, use midway
  between `minPosition` and `maxPosition` (extension
  function [centerPosition](https://iv4xr-project.github.io/iv4xr-se-plugin/space-engineers-api/spaceEngineers.model.extensions/center-position.html))
  .

## Character and camera orientation

- The character **forward** vector is identical to the camera forward vector; when moving to a side, both forward vectors are
  changed.
- The character **up** vector differs from the camera up vector when walking. Imagine character moving his head to look up rather
  than the whole body.
- When jetpack is on, up vectors are identical. Imagine character rotating the whole body to look up.
- This works for the 3rd person camera mode, unknown for other modes.
- There is a possibility to move the camera around character. What is happening with internal variables has not been explored.

## Blocks

The description of [Blocks](Blocks.MD) is in a separate file.
