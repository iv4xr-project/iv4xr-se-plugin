package spaceEngineers.model

const val LARGE_BLOCK_CUBE_SIDE_SIZE = 2.5f
const val SMALL_BLOCK_CUBE_SIDE_SIZE = 0.5f

enum class CubeSize {
    Large {
        override val value: Float = LARGE_BLOCK_CUBE_SIDE_SIZE
    },
    Small {
        override val value: Float = SMALL_BLOCK_CUBE_SIDE_SIZE
    };

    abstract val value: Float
}
