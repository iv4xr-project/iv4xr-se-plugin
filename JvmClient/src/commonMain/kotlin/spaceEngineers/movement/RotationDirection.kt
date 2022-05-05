package spaceEngineers.movement

import spaceEngineers.model.Vec2F


fun RotationDirection.toKeyboardSnapshot(): KeyboardSnapshot {
    return KeyboardSnapshot(
        pressedKeys = keycode.toList(),
        text = emptyList()
    )
}


enum class RotationDirection(val vector: Vec2F, val keycode: Set<Int>) {
    LEFT(Vec2F.ROTATE_LEFT, setOf(0x25)),
    RIGHT(Vec2F.ROTATE_RIGHT, setOf(0x27)),
    DOWN(Vec2F.ROTATE_DOWN, setOf(0x28)),
    UP(Vec2F.ROTATE_UP, setOf(0x26));

}
